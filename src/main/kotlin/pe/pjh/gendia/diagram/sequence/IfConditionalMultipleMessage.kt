package pe.pjh.gendia.diagram.sequence

import com.intellij.psi.PsiBinaryExpression
import com.intellij.psi.PsiBlockStatement
import com.intellij.psi.PsiExpression
import com.intellij.psi.PsiExpressionStatement
import com.intellij.psi.PsiIfStatement
import com.intellij.psi.PsiStatement
import pe.pjh.gendia.diagram.TabUtil

class IfConditionalMultipleMessage(
    override val callee: Participant,
    val name: String,
    psiIfStatement: PsiIfStatement,
) : MultipleBlockMessage(callee) {

    init {
        println("thenBranch ${psiIfStatement.thenBranch}")
        println("elseBranch ${psiIfStatement.elseBranch}")


        //루프는 단일 그룹메시지

        var tempPsiIfStatement: PsiIfStatement? = psiIfStatement;
        do {
            if (tempPsiIfStatement?.thenBranch != null) {
                val expression: PsiExpression? = tempPsiIfStatement.condition
                putCase(tempPsiIfStatement.thenBranch, expression?.text, callee)
            }

            tempPsiIfStatement = if (tempPsiIfStatement?.elseBranch is PsiIfStatement) {
                tempPsiIfStatement.elseBranch as PsiIfStatement
            } else {
                putCase(tempPsiIfStatement?.elseBranch, "", callee)
                null
            }
        } while (tempPsiIfStatement != null)

    }

    private fun putCase(
        psiStatement: PsiStatement?,
        expression: String?,
        callee: Participant
    ) {

        if (psiStatement == null) return;

        val blockMessage = BlockMessage(callee)

        when (psiStatement) {
            is PsiBlockStatement -> {
                blockMessage.subMessageParsing(psiStatement.codeBlock)
            }

            is PsiExpressionStatement -> {
                blockMessage.addMessageByPsiType(psiStatement, "${name}:${expression}")
            }
        }
        blockMessages.add(blockMessage)
    }

    override fun getCodeLine(depth: Int): String {

        if (blockMessages.isEmpty()) return ""

        var code = ""

        blockMessages.forEach {

            if (it.subMessages.isEmpty()) return@forEach

            code += TabUtil.textLine(
                depth, if (code.isEmpty()) "alt $name" else "else $name"
            )
            code += it.getCodeLine(depth + 1)
        }
        code += TabUtil.textLine(depth, "end")
        return code
    }
}