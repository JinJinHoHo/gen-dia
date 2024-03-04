package pe.pjh.gendia.diagram.sequence

import com.intellij.psi.*
import pe.pjh.gendia.diagram.UndefindOperationException

/**
 * 메시지 그룹(하위로 n개의 메시지를 갖고 있음)
 */
open class BlockMessage(val callee: Participant) : Message {

    val subMessages: MutableList<Message> = mutableListOf()

    constructor(
        callee: Participant,
        psiElement: PsiElement, comment: String?
    ) : this(callee){
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

    private fun addSubMessage(psiCodeBlock: PsiCodeBlock?, comment: String?) {

        if (psiCodeBlock == null) return

        val psiElementList: List<PsiElement> = psiCodeBlock
            .children
            .filter { it !is PsiWhiteSpace }

        var index = 0
        do {
            when (val it = psiElementList.get(index)) {
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
                        addSubMessage((it.lastChild as PsiBlockStatement).codeBlock, comment)
                    }
                }
            }
            index++
        } while (index < psiElementList.size)
    }

    fun addMessage(psiElement: PsiElement, comment: String?) {
        when (psiElement) {

            is PsiBlockStatement -> {
                addSubMessage(psiElement.codeBlock, comment)
            }

            is PsiMethod -> {
                addSubMessage(psiElement.body, comment)
            }

            is PsiConditionalLoopStatement -> {
                subMessages.add(ConditionalLoopMultipleMessage(callee, psiElement, comment))
            }

            is PsiExpressionStatement -> {

                //psiCall 추출
                val psiCall: PsiCall? = psiElement.children
                    .filterIsInstance<PsiCall>()
                    .getOrNull(0)

                val method: PsiMethod? = psiCall?.resolveMethod()
                if (method != null) subMessages.add(MethodBlockMessage(callee, callee, method, comment))
                else {
                    subMessages.add(
                        CallMessage(
                            callee,
                            callee,
                            (psiCall as PsiMethodCallExpression).methodExpression.text,
                            MessageArrowType.SolidLineWithArrowhead
                        )
                    )
                }

            }

            is PsiIfStatement -> {
                subMessages.add(IfConditionalMultipleMessage(callee, comment, psiElement))
            }

            else -> {
                throw UndefindOperationException("Not Statement ${psiElement}")
            }
        }
    }
}