package pe.pjh.gendia.diagram.sequence

import com.intellij.psi.*

/**
 * 메시지 그룹(하위로 n개의 메시지를 갖고 있음)
 */
open class GroupMessage(open val callee: Participant) : Message {

    val subMessages = mutableListOf<Message>()
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

    public fun subMessageParsing(psiCodeBlock: PsiCodeBlock?) {

        if (psiCodeBlock == null) return

        psiCodeBlock
            .children
            .filter { it !is PsiWhiteSpace }
            .forEach {
                when (it) {
                    is PsiComment -> {

                        if (it.text.indexOf("//+") == -1) return@forEach

                        //코멘트 내용 추출
                        val comment = it.text.substringAfterLast("//+")

                        //코멘트 다음 라인 psiElement
                        val psiElement: PsiElement = it.nextSibling.nextSibling

                        addMessageByPsiType(psiElement, comment)
                    }

                    else -> {

                        if (it.lastChild !is PsiBlockStatement) return@forEach

                        //하위에 코드블록이 있을 경우 재귀호출
                        subMessageParsing((it.lastChild as PsiBlockStatement).codeBlock)
                    }
                }
            }
    }

    private fun addMessageByPsiType(psiElement: PsiElement, comment: String) {
        when (psiElement) {

            is PsiConditionalLoopStatement -> {
                subMessages.add(ConditionalLoopGroupMessage(callee, comment, psiElement))
            }

            is PsiExpressionStatement -> {

                //psiCall 추출
                val psiCall: PsiCall? = psiElement.children
                    .filterIsInstance<PsiCall>()
                    .getOrNull(0)

                val method: PsiMethod? = psiCall?.resolveMethod()
                if (method != null) subMessages.add(MethodGroupMessage(callee, callee, method, comment))
            }

            is PsiIfStatement -> {
                subMessages.add(IfConditionalGroupMessage(callee, comment, psiElement))
            }

            else -> {
                println("+++++++++++=")
                println(psiElement)
                println("+++++++++++=")
            }

        }
    }
}