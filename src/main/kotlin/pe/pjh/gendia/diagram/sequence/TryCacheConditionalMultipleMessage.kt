package pe.pjh.gendia.diagram.sequence

import com.intellij.psi.PsiTryStatement

class TryCacheConditionalMultipleMessage(
    override val caller: Participant,
    override val callee: Participant,
    override val name: String?,
    psiTryStatement: PsiTryStatement,
) : ConditionalMultipleMessage(caller, callee, name) {

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

}