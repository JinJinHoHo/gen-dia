package pe.pjh.gendia.diagram.sequence

import com.intellij.psi.*
import pe.pjh.gendia.diagram.UndefindOperationException

/**
 * 메시지 그룹(하위로 n개의 메시지를 갖고 있음)
 */
open class BlockMessage(
    private val caller: Participant,
    private val callee: Participant
) : Message {

    val subMessages: MutableList<Message> = mutableListOf()

    constructor(
        caller: Participant,
        callee: Participant,
        psiElement: PsiElement, comment: String?
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


    fun addMessage(psiElement: PsiElement, comment: String?) {

        println("${psiElement}--${psiElement.text}:'${comment}'")
        when (psiElement) {

            is PsiCodeBlock -> addCodeBlockMessage(psiElement, comment)

            is PsiBlockStatement -> addCodeBlockMessage(psiElement.codeBlock, comment)

            is PsiMethod -> {
                val codeBlock = psiElement.body
                if (codeBlock != null) addCodeBlockMessage(codeBlock, comment)
            }

            is PsiConditionalLoopStatement ->
                subMessages.add(ConditionalLoopMultipleMessage(caller, callee, psiElement, comment))

            is PsiIfStatement -> subMessages.add(IfElseConditionalMultipleMessage(caller, callee, comment, psiElement))

            is PsiTryStatement -> subMessages.add(
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
                psiElement.declaredElements.forEach {
                    println((it as PsiLocalVariable).initializer)
                }
            }

            else -> {
                println(psiElement.text)
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
                is PsiComment -> {

                    if (it.text.indexOf("//+") != -1) {
                        //코멘트 내용 추출
                        val tempComment = it.text.substringAfterLast("//+")

                        //코멘트 다음 라인 psiElement
                        val psiElement: PsiElement = it.nextSibling.nextSibling
                        addMessage(psiElement, tempComment)
                        index = psiElementList.indexOf(psiElement)
                    }
                }

                else -> {
                    if (it.lastChild is PsiBlockStatement) {
                        //하위에 코드블록이 있을 경우 재귀호출
                        addCodeBlockMessage((it.lastChild as PsiBlockStatement).codeBlock, comment)
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
        if (method != null) {
            //클래스 내부 메소드 호출시.
            subMessages.add(MethodBlockMessage(callee, callee, method, comment))
        } else {
            //외부 클래스 메소드 호출시
            addMessage((psiCall as PsiMethodCallExpression), comment)
        }
    }


    private fun addReferenceExpressionMessage(psiElement: PsiReferenceExpression, comment: String?) {
        var temp = psiElement.resolve()
        if (temp == null) {
            temp = psiElement.qualifierExpression
            if (temp != null) {
                addMessage(temp, comment)
                return
            }
        }

        //static 메소드 호출시 들어오는지 확인 필요.
        val parentPsiElement = psiElement.parent
        subMessages.add(
            CallMessage(
                callee,
                callee,
                comment,
                parentPsiElement.text,
                MessageArrowType.SolidLineWithArrowhead
            )
        )
    }
}