package pe.pjh.gendia.diagram.sequence

import com.intellij.psi.*
import com.intellij.psi.impl.source.tree.java.PsiMethodCallExpressionImpl

open class GroupMessage(open val callee: Participant) : Message {

    val subMessages = mutableListOf<Message>()
    override fun getCode(depth: Int): String {
        var code = ""
        subMessages.forEach {
            val subCode = it.getCode(depth)
            if (subCode.isNotEmpty()) code += subCode
        }
        return code
    }


    protected fun extracted(psiCodeBlock: PsiCodeBlock?) {
        if (psiCodeBlock == null) return

        val psiComment: List<PsiComment> = psiCodeBlock
            .children
            .filterIsInstance<PsiComment>()
            .filter { it.text.indexOf("//+") != -1 }

        psiComment.forEach {
            val psiElement: PsiElement = it.nextSibling.nextSibling
            val comment = it.text.substringAfterLast("//+")

            when (psiElement) {
                is PsiForStatement -> {
                    subMessages.add(LoopGroupMessage(callee, comment, psiElement))
                }

                is PsiExpressionStatement -> {
                    val methodCallExpressionImpl: PsiMethodCallExpressionImpl? =
                        psiElement.children.filterIsInstance<PsiMethodCallExpressionImpl>().getOrNull(0)
                    val method: PsiMethod? = methodCallExpressionImpl?.resolveMethod()
                    if (method != null) {
                        subMessages.add(MethodGroupMessage(callee, callee, method))
                    }

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