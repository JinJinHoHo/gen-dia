package pe.pjh.gendia.diagram.sequence

import com.intellij.psi.PsiExpression
import com.intellij.psi.PsiIfStatement

class IfElseConditionalMultipleMessage(
    override val caller: Participant,
    override val callee: Participant,
    override val name: String?,
    psiIfStatement: PsiIfStatement,
) : ConditionalMultipleMessage(caller, callee, name) {

    init {
        //루프는 단일 그룹메시지

        var tempPsiIfStatement: PsiIfStatement? = psiIfStatement
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

    override fun mark(): ConditionalMarkType {
        return ConditionalMarkType.Alt
    }

    override fun toString(): String {
        return """${caller.name}->${callee.name}:${name}"""
    }

}