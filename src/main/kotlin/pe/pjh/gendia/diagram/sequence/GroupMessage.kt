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