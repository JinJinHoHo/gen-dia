package pe.pjh.gendia.diagram.sequence.message

import com.intellij.psi.*
import org.slf4j.LoggerFactory
import pe.pjh.gendia.diagram.UndefindOperationException
import pe.pjh.gendia.diagram.sequence.MessageArrowType
import pe.pjh.gendia.diagram.sequence.ParserContext
import pe.pjh.gendia.diagram.sequence.SequenceDiagramConfig
import pe.pjh.gendia.diagram.sequence.participant.BaseParticipant
import pe.pjh.gendia.diagram.sequence.participant.ClassParticipant
import kotlin.collections.filter
import kotlin.collections.filterIsInstance
import kotlin.collections.forEach
import kotlin.collections.getOrNull
import kotlin.jvm.java
import kotlin.text.indexOf
import kotlin.text.isNotEmpty
import kotlin.text.substringAfterLast
import kotlin.toString

/**
 * 메시지 그룹(하위로 n개의 메시지를 갖고 있음)
 */
open class BlockMessage(
    private val caller: BaseParticipant,
    private val callee: BaseParticipant,
) : Message {

    companion object {
        private val logger = LoggerFactory.getLogger(BlockMessage::class.java)
    }

    val subMessages: MutableList<Message> = mutableListOf()

    constructor(
        caller: BaseParticipant,
        callee: BaseParticipant,
        psiElement: PsiElement, comment: String?,
    ) : this(caller, callee) {
        addMessage(psiElement, comment)
    }

    override fun getCode(config: SequenceDiagramConfig): String {
        TODO("Not yet implemented")
    }

    override fun getCodeLine(depth: Int, config: SequenceDiagramConfig): String {
        var code = ""
        subMessages.forEach {
            val subCode = it.getCodeLine(depth, config)
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

            is PsiConditionalLoopStatement -> addSubMessage(
                ConditionalLoopMultipleMessage(
                    caller,
                    callee,
                    psiElement,
                    comment
                )
            )

            is PsiIfStatement -> addSubMessage(IfElseConditionalMultipleMessage(caller, callee, comment, psiElement))

            is PsiTryStatement -> addSubMessage(TryCacheConditionalMultipleMessage(caller, callee, comment, psiElement))

            is PsiMethodCallExpression -> {
                val psiMethod: PsiMethod? = psiElement.resolveMethod()
                if (psiMethod == null) {
                    logger.debug("{} (addMessage.PsiMethodCallExpression.methodExpression)", psiElement.text)
                    addMessage(psiElement.methodExpression, comment)
                    return
                }

                val parserContext = ParserContext.getInstance()
                var psiClass: PsiClass? = psiMethod.containingClass
                if (psiClass == null) throw UndefindOperationException("Not Statement $psiElement")

                addSubMessage(
                    MethodBlockMessage(
                        callee,
                        parserContext.getParticipant(psiClass),
                        psiMethod,
                        comment, null
                    )
                )
                return
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

            is PsiMethod -> {
                //addMessage를 PsiMethod 호출 하는 경우는 없어야 함.(MethodBlockMessage 통해서 처리 되도록 변경 필요)
                logger.debug("{} (addMessage.PsiMethod) 호출 경우 확인.", psiElement.text)
                val codeBlock: PsiCodeBlock? = psiElement.body
                if (codeBlock != null) addCodeBlockMessage(codeBlock, comment)
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
                addSubMessage(MethodBlockMessage(callee, parserContext.getParticipant(psiClass), method, comment, null))
                return
            }
            addSubMessage(MethodBlockMessage(callee, callee, method, comment, null))
            return
        }

        //클래스 내부 메소드 호출시.
        if (logger.isDebugEnabled) logger.debug("addExpressionStatementMessage 실행 여부 확인. {}", method)
        addSubMessage(MethodBlockMessage(callee, callee, method, comment, null))

    }

    private fun addReferenceExpressionMessage(psiElement: PsiReferenceExpression, comment: String?) {
        var temp: PsiElement? = psiElement.resolve()
        if (temp == null) {
            temp = psiElement.qualifierExpression
            if (temp != null) {
                addMessage(temp, comment)
                return
            }
            if (logger.isDebugEnabled) logger.debug(
                "{} 확인 필요 라인.(addReferenceExpressionMessage.qualifierExpression)",
                temp
            )
            return
        }
        if (temp !is PsiField) {
            if (logger.isDebugEnabled) logger.debug("{} 테스트시 캐시 확인 필요.(addReferenceExpressionMessage.PsiField)", temp)
            return
        }

        val psiType: PsiType = temp.type
        if (psiType !is PsiClassType) {
            if (logger.isDebugEnabled) logger.debug("{} 확인 필요 라인.(addReferenceExpressionMessage)", psiType)
            return
        }

        val parserContext = ParserContext.getInstance()
        val psiClass: PsiClass? = psiType.resolve()
        if (psiClass == null) {
            if (logger.isDebugEnabled) logger.debug(
                "{} 테스트시 캐시 확인 필요.(addReferenceExpressionMessage.PsiClass)",
                psiType
            )
            return
        }

        addSubMessage(
            CallMessage(
                callee,
                parserContext.getParticipant(psiClass),
                null,
                comment,
                psiElement.parent.text,
                MessageArrowType.SolidLineWithArrowhead
            )
        )
    }
}