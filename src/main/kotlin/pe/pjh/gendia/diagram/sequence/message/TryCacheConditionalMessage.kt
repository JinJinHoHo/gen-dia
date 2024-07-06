package pe.pjh.gendia.diagram.sequence.message

import com.intellij.psi.PsiTryStatement
import pe.pjh.gendia.diagram.sequence.ConditionalMarkType
import pe.pjh.gendia.diagram.sequence.participant.BaseParticipant

class TryCacheConditionalMessage(
    override val caller: BaseParticipant,
    override val callee: BaseParticipant,
    override val name: String?,
    psiTryStatement: PsiTryStatement,
) : ConditionalMessage(caller, callee, name) {

    init {
        //루프는 단일 그룹메시지

        val tempPsiTryStatement: PsiTryStatement = psiTryStatement
        conditionalBlock(tempPsiTryStatement.tryBlock, "", callee)

        tempPsiTryStatement.catchBlocks.forEach {
            conditionalBlock(it, "", callee)
        }
        conditionalBlock(tempPsiTryStatement.finallyBlock, "", callee)

    }

    override fun mark(): ConditionalMarkType {
        return ConditionalMarkType.Critical
    }

    override fun toString(): String {
        return """${caller.name}->${callee.name}:${name}"""
    }

}