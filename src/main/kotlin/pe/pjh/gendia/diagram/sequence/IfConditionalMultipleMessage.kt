package pe.pjh.gendia.diagram.sequence

import com.intellij.psi.PsiExpression
import com.intellij.psi.PsiIfStatement
import com.intellij.psi.PsiStatement
import pe.pjh.gendia.diagram.TabUtil

class IfConditionalMultipleMessage(
    override val callee: Participant,
    val name: String?,
    psiIfStatement: PsiIfStatement,
) : MultipleBlockMessage(callee) {

    init {
        //루프는 단일 그룹메시지

        var tempPsiIfStatement: PsiIfStatement? = psiIfStatement;
        do {
            if (tempPsiIfStatement?.thenBranch != null) {
                val expression: PsiExpression? = tempPsiIfStatement.condition
                conditionalBlock(tempPsiIfStatement.thenBranch, expression?.text, callee)
            }

            tempPsiIfStatement = if (tempPsiIfStatement?.elseBranch is PsiIfStatement) {
                tempPsiIfStatement.elseBranch as PsiIfStatement
            } else {
                conditionalBlock(tempPsiIfStatement?.elseBranch, "else", callee)
                null
            }
        } while (tempPsiIfStatement != null)

    }

    private fun conditionalBlock(
        psiStatement: PsiStatement?,
        expression: String?,
        callee: Participant
    ) {

        if (psiStatement == null) return

        blockMessages.add(ExpressionBlockMessage(callee, psiStatement, "${name}", expression))
    }

    override fun getCodeLine(depth: Int): String {

        if (blockMessages.isEmpty()) return ""
        var code = TabUtil.textLine(depth, "Note right of ${callee.name}: ${name}")

        blockMessages.forEachIndexed { index, it ->
            run {
                if (!it.subMessages.isEmpty()) {
                    val title = if (it is ExpressionBlockMessage) it.getExpression() else name
                    code += TabUtil.textLine(
                        depth,
                        if (index == 0) "alt ${title}" else "else ${title}"
                    )
                    code += it.getCodeLine(depth + 1)
                }
            }
        }

        return code + TabUtil.textLine(depth, "end")
    }
}