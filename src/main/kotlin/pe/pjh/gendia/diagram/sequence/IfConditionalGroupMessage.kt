package pe.pjh.gendia.diagram.sequence

import com.intellij.psi.PsiIfStatement

class IfConditionalGroupMessage(
    override val callee: Participant,
    val name: String,
    val psiIfStatement: PsiIfStatement,
) : GroupMessage(callee) {

}