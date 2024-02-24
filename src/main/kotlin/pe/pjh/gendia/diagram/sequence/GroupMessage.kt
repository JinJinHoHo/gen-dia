package pe.pjh.gendia.diagram.sequence

import com.intellij.psi.*

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

    protected fun subMessageParsing(psiCodeBlock: PsiCodeBlock?) {

        if (psiCodeBlock == null) return

        val psiComment: List<PsiComment> = psiCodeBlock
            .children
            .filterIsInstance<PsiComment>()
            .filter { it.text.indexOf("//+") != -1 }

        psiComment.forEach {

            //코멘트 내용 추출
            val comment = it.text.substringAfterLast("//+")

            //코멘트 다음 라인 psiElement
            val psiElement: PsiElement = it.nextSibling.nextSibling

            when (psiElement) {

                is PsiForStatement -> {
                    subMessages.add(LoopGroupMessage(callee, comment, psiElement))
                }

                is PsiExpressionStatement -> {

                    //psiCall 추출
                    val psiCall: PsiCall? = psiElement.children
                        .filterIsInstance<PsiCall>()
                        .getOrNull(0)

                    val method: PsiMethod? = psiCall?.resolveMethod()
                    if (method != null) subMessages.add(MethodGroupMessage(callee, callee, method, comment))
                }

                else -> {
                    println("+++++++++++=")
                    println(psiElement)
                    println("+++++++++++=")
                }

            }
        }
    }
}