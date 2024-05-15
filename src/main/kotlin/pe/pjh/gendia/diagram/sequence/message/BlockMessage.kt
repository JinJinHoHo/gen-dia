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

    open fun addSubMessage(message: Message) {

        if (logger.isDebugEnabled) logger.debug(message.toString())
        subMessages.add(message)
    }

    fun addMessage(psiElement: PsiElement, comment: String?) {

        when (psiElement) {

            is PsiCodeBlock -> addCodeBlock(psiElement, comment)

            is PsiBlockStatement -> addCodeBlock(psiElement.codeBlock, comment)

            is PsiConditionalLoopStatement -> addSubMessage(
                LoopMultipleMessage(
                    caller,
                    callee,
                    psiElement,
                    comment
                )
            )

            is PsiIfStatement -> addSubMessage(IfElseConditionalMultipleMessage(caller, callee, comment, psiElement))

            is PsiTryStatement -> addSubMessage(TryCacheConditionalMultipleMessage(caller, callee, comment, psiElement))

            is PsiMethodCallExpression -> addPsiMethodCallExpression(psiElement, comment)

            is PsiReferenceExpression -> addPsiReferenceExpression(psiElement, comment)

            is PsiExpressionStatement -> addPsiExpressionStatement(psiElement, comment)

            is PsiReturnStatement -> addPsiReturnStatement(psiElement, comment)

            is PsiDeclarationStatement -> {
                psiElement.declaredElements
                    .map { (it as PsiLocalVariable).initializer }
                    .forEach {
                        if (it != null) addMessage(it, comment)
                        else {
                            if (logger.isDebugEnabled)
                                logger.debug("확인 필요 라인. {}", it.toString())
                        }
                    }
            }

            is PsiMethod -> addPsiMethod(psiElement, comment)

            //실행 코드가 코멘트일 경우 처리 제외.
            is PsiComment -> {
                if (logger.isDebugEnabled) logger.debug("코멘트 무시 ${psiElement.text}")
                return
            }

            //원시형 무시 처리 메소드 위주에 플로우 처리 중심으로 구현.
            is PsiLiteralExpression -> {
                if (logger.isDebugEnabled) logger.debug("원시형 무시 ${psiElement.text}")
                return
            }

            else -> {
                if (logger.isDebugEnabled) logger.debug(psiElement.text)
                throw UndefindOperationException("Not Statement $psiElement")
            }
        }
    }

    open fun addPsiMethodCallExpression(psiElement: PsiMethodCallExpression, comment: String?) {
        val psiMethod: PsiMethod? = psiElement.resolveMethod()
        if (psiMethod == null) {
            logger.debug("{} (addMessage.PsiMethodCallExpression.methodExpression)", psiElement.text)
            addMessage(psiElement.methodExpression, comment)
            return
        }

        addMessage(psiMethod, comment)
    }

    open fun addPsiReturnStatement(psiReturnStatement: PsiReturnStatement, comment: String?) {
        val psiExpression: PsiExpression? = psiReturnStatement.returnValue;
        if (psiExpression != null) {
            addMessage(psiExpression, comment)
        } else {
            logger.debug("addPsiReturnStatement {}", psiReturnStatement.text)
        }

    }

    open fun addCodeBlock(psiCodeBlock: PsiCodeBlock, comment: String?) {

        val psiElementList: List<PsiElement> = psiCodeBlock
            .children
            .filter { it !is PsiWhiteSpace }

        var index = 0
        do {
            //todo 정리 예정 코드. addMessage 에 병합 하는 방향으로.
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
                        addCodeBlock((it.lastChild as PsiBlockStatement).codeBlock, comment)
                        continue
                    } else {
                        if (logger.isDebugEnabled) logger.debug("{} 확인 필요 라인.(addCodeBlockMessage)", it.text)
                    }
                }
            }
            index++
        } while (index < psiElementList.size)
    }

    open fun addPsiMethod(psiElement: PsiMethod, comment: String?) {
        //addMessage를 PsiMethod 호출 하는 경우는 없어야 함.(MethodBlockMessage 통해서 처리 되도록 변경 필요)
        if (logger.isDebugEnabled)
            logger.debug("{} (addMessage.PsiMethod) 호출 경우 확인.", psiElement.text)

        //클래스 메소드는 처리 제외.
        if (psiElement.hasModifierProperty("static")) {
            if (logger.isDebugEnabled) logger.debug("${psiElement.name} 제외 처리")
            return
        }

        var psiClass: PsiClass? = psiElement.containingClass
        if (psiClass != null) {
            if (caller == callee && callee is ClassParticipant && psiClass == callee.psiClass) {
                // 인스턴스 내에 메소드를 호출후, 호출된 메소드가 인스턴스 내 메소드를 재호출시 다이어그램 플로우를 인지 하기 힘들어 제안함.
                if (logger.isDebugEnabled) logger.debug("인스턴스 내에서 2중으로 함수 호출시 중단.")
                return
            }
            if (callee is ClassParticipant && psiClass != callee.psiClass) {
                addSubMessage(
                    MethodBlockMessage(
                        callee,
                        ParserContext.getInstance().getParticipant(psiClass),
                        psiElement,
                        comment,
                        null
                    )
                )
                return
            }

            addSubMessage(MethodBlockMessage(callee, callee, psiElement, comment, null))
            return
        }

        //클래스 내부 메소드 호출시.
        throw UndefindOperationException("Not Statement $psiElement -> ${psiElement.text}")
//        if (logger.isDebugEnabled) logger.debug("addPsiExpressionStatement 실행 여부 확인. {}", psiElement)
//        addSubMessage(MethodBlockMessage(callee, callee, psiElement, comment, null))
    }

    open fun addPsiExpressionStatement(psiElement: PsiExpressionStatement, comment: String?) {
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

        addMessage(method, comment)

//        throw UndefindOperationException("Not Statement $psiElement -> ${psiElement.text}")
    }

    open fun addPsiReferenceExpression(psiElement: PsiReferenceExpression, comment: String?) {

        var temp: PsiElement? = psiElement.resolve()
        if (temp == null) {
            temp = psiElement.qualifierExpression
            if (temp != null) {
                addMessage(temp, comment)
                return
            }
            if (logger.isDebugEnabled) logger.debug("{} 확인 필요 라인.(addPsiReferenceExpression.qualifierExpression)", temp)
            return
        }

        if (temp !is PsiField) {
            if (logger.isDebugEnabled) logger.debug("{} 테스트시 캐시 확인 필요.(addPsiReferenceExpression.PsiField)", temp)
            return
        }

        val psiType: PsiType = temp.type
        if (psiType !is PsiClassType) {
            if (logger.isDebugEnabled) logger.debug("{} 확인 필요 라인.(addPsiReferenceExpression)", psiType)
            return
        }

        val parserContext = ParserContext.getInstance()
        val psiClass: PsiClass? = psiType.resolve()
        if (psiClass == null) {
            if (logger.isDebugEnabled) logger.debug(
                "{} 테스트시 캐시 확인 필요.(addPsiReferenceExpression.PsiClass)",
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