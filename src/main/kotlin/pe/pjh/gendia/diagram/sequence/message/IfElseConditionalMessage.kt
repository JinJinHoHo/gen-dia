package pe.pjh.gendia.diagram.sequence.message

import com.intellij.psi.PsiExpression
import com.intellij.psi.PsiIfStatement
import pe.pjh.gendia.diagram.sequence.ConditionalMarkType
import pe.pjh.gendia.diagram.sequence.participant.BaseParticipant

class IfElseConditionalMessage(
    override val caller: BaseParticipant,
    override val callee: BaseParticipant,
    override val name: String?,
    psiIfStatement: PsiIfStatement,
) : ConditionalMessage(caller, callee, name) {

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
                conditionalBlock(tempPsiIfStatement?.elseBranch, null, callee)
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