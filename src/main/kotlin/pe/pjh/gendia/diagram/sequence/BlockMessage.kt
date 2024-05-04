package pe.pjh.gendia.diagram.sequence

import com.intellij.psi.*
import org.slf4j.LoggerFactory
import pe.pjh.gendia.diagram.UndefindOperationException

/**
 * 메시지 그룹(하위로 n개의 메시지를 갖고 있음)
 */
open class BlockMessage(
    private val caller: Participant,
    private val callee: Participant,
) : Message {

    companion object {
        private val logger = LoggerFactory.getLogger(BlockMessage::class.java)
    }

    val subMessages: MutableList<Message> = mutableListOf()

    constructor(
        caller: Participant,
        callee: Participant,
        psiElement: PsiElement, comment: String?,
    ) : this(caller, callee) {
        addMessage(psiElement, comment)
    }

    override fun getCode(): String {
        TODO("Not yet implemented")
    }

    override fun getCodeLine(depth: Int): String {
        var code = ""
        subMessages.forEach {
            val subCode = it.getCodeLine(depth)
            if (subCode.isNotEmpty()) code += subCode
        }
        return code
    }

    override fun toString(): String {
        return """${caller.name}->${callee.name}"""
    }

    private fun addSubMessage(message: Message) {

        if (logger.isDebugEnabled) logger.debug(message.toString())
        subMessages.add(message)
    }


    fun addMessage(psiElement: PsiElement, comment: String?) {

        when (psiElement) {

            is PsiCodeBlock -> addCodeBlockMessage(psiElement, comment)

            is PsiBlockStatement -> addCodeBlockMessage(psiElement.codeBlock, comment)

            is PsiMethod -> {
                val codeBlock = psiElement.body
                if (codeBlock != null) addCodeBlockMessage(codeBlock, comment)
            }

            is PsiConditionalLoopStatement ->
                addSubMessage(ConditionalLoopMultipleMessage(caller, callee, psiElement, comment))

            is PsiIfStatement -> addSubMessage(IfElseConditionalMultipleMessage(caller, callee, comment, psiElement))

            is PsiTryStatement -> addSubMessage(
                TryCacheConditionalMultipleMessage(
                    caller,
                    callee,
                    comment,
                    psiElement
                )
            )

            is PsiMethodCallExpression -> {
                val psiMethod: PsiMethod? = psiElement.resolveMethod()
                if (psiMethod != null) {
                    addMessage(psiMethod, comment)
                } else {
                    addMessage(psiElement.methodExpression, comment)
                }
            }

            is PsiReferenceExpression -> addReferenceExpressionMessage(psiElement, comment)

            is PsiExpressionStatement -> addExpressionStatementMessage(psiElement, comment)

            is PsiDeclarationStatement -> {
                //todo [예정] 변수에 바로 대입되는 메소드 존재 여부 확인 로직 필요.
                if (logger.isDebugEnabled) {
                    logger.debug("{} (PsiDeclarationStatement)", psiElement.text)
                    psiElement.declaredElements.forEach {
                        logger.debug((it as PsiLocalVariable).initializer.toString())
                    }
                }
            }

            //실행 코드가 코멘트일 경우 처리 제외.
            is PsiComment -> {
                if (logger.isDebugEnabled) logger.debug("코멘트 무시 ${psiElement.text}")
                return
            }

            else -> {
                if (logger.isDebugEnabled) logger.debug(psiElement.text)
                throw UndefindOperationException("Not Statement $psiElement")
            }
        }
    }

    private fun addCodeBlockMessage(psiCodeBlock: PsiCodeBlock, comment: String?) {

        val psiElementList: List<PsiElement> = psiCodeBlock
            .children
            .filter { it !is PsiWhiteSpace }

        var index = 0
        do {
            when (val it = psiElementList[index]) {
                is PsiJavaToken -> {
                    if (logger.isDebugEnabled) logger.debug("제외 처리. {} (addCodeBlockMessage.PsiJavaToken)", it.text)
                }

                is PsiComment -> {

                    if (it.text.indexOf("//+") != -1) {
                        //코멘트 내용 추출
                        val tempComment = it.text.substringAfterLast("//+")

                        //todo [예정] 코드 뒤에 인라인 형식으로 된 소스 처리 방식 추가 필요.
                        //코멘트 다음 라인 psiElement
                        val psiElement: PsiElement = it.nextSibling.nextSibling
                        addMessage(psiElement, tempComment)
                        index = psiElementList.indexOf(psiElement)
                    }
                }

                else -> {
                    //하위에 코드블록이 있을 경우 재귀호출
                    if (it.lastChild is PsiBlockStatement) {
                        addCodeBlockMessage((it.lastChild as PsiBlockStatement).codeBlock, comment)
                        continue
                    } else {
                        if (logger.isDebugEnabled) logger.debug("{} 확인 필요 라인.(addCodeBlockMessage)", it.text)
                    }

                }
            }
            index++
        } while (index < psiElementList.size)
    }


    private fun addExpressionStatementMessage(psiElement: PsiExpressionStatement, comment: String?) {
        //psiCall 추출
        val psiCall: PsiCall? = psiElement.children
            .filterIsInstance<PsiCall>()
            .getOrNull(0)

        val method: PsiMethod? = psiCall?.resolveMethod()

        //외부 클래스 메소드 호출시
        if (method == null) {
            addMessage((psiCall as PsiMethodCallExpression), comment)
            return
        }

        //클래스 메소드는 처리 제외.
        if (method.hasModifierProperty("static")) {
            if (logger.isDebugEnabled) logger.debug("${method.name} 제외 처리")
            return
        }

        val psiClass: PsiClass? = method.containingClass
        if (psiClass != null) {
            if (callee is ClassParticipant && psiClass != callee.psiClass) {
                val parserContext = ParserContext.getInstance()
                addSubMessage(MethodBlockMessage(callee, parserContext.getParticipant(psiClass), method, comment))
                return
            }
            addSubMessage(MethodBlockMessage(callee, callee, method, comment))
            return
        }

        //클래스 내부 메소드 호출시.
        if (logger.isDebugEnabled) logger.debug("addExpressionStatementMessage 실행 여부 확인. {}", method)
        addSubMessage(MethodBlockMessage(callee, callee, method, comment))

    }


    private fun addReferenceExpressionMessage(psiElement: PsiReferenceExpression, comment: String?) {
        var temp = psiElement.resolve()
        if (temp == null) {
            temp = psiElement.qualifierExpression
            if (temp != null) {
                addMessage(temp, comment)
                return
            }
        } else if (temp is PsiField) {
            val psiType: PsiType = temp.type
            if (psiType is PsiClassType) {

                val parserContext = ParserContext.getInstance()

                val psiClass: PsiClass? = psiType.resolve()
                if (psiClass != null) {
                    addSubMessage(
                        CallMessage(
                            callee,
                            parserContext.getParticipant(psiClass),
                            comment,
                            psiElement.parent.text,
                            MessageArrowType.SolidLineWithArrowhead
                        )
                    )
                    return
                }
            }
            if (logger.isDebugEnabled) logger.debug("{} 확인 필요 라인.(addReferenceExpressionMessage)", psiType)
            return
        }

        if (logger.isDebugEnabled) logger.debug("{} 확인 필요 라인.(addReferenceExpressionMessage)", temp)

    }
}