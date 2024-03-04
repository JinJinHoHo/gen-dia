package pe.pjh.gendia.diagram.sequence

import com.intellij.psi.PsiBlockStatement
import com.intellij.psi.PsiConditionalLoopStatement
import pe.pjh.gendia.diagram.TabUtil
import pe.pjh.gendia.diagram.UMLType

/**
 * For, While 조건 루프
 */
class ConditionalLoopMultipleMessage(
    override val callee: Participant,
    psiConditionalLoopStatement: PsiConditionalLoopStatement,
    private val comment: String?,

    ) : MultipleBlockMessage(callee) {


    init {

        //루프는 단일 그룹메시지.
        val blockMessage = BlockMessage(callee)
        blockMessages.add(blockMessage)

        val psiStatement = psiConditionalLoopStatement.body as PsiBlockStatement
        blockMessage.addMessage(psiStatement, comment)
    }

    override fun getCodeLine(depth: Int): String {

        val groupMessage = blockMessages.getOrNull(0) ?: return ""

        //서브 코드에 노츨되는 항목이 없으면 빈값 반환.
        if (groupMessage.subMessages.isEmpty()) return ""

        //
        var code = TabUtil.textLine(depth, "loop $comment")
        code += groupMessage.getCodeLine(depth + 1)
        code += TabUtil.textLine(depth, "end")
        return code
    }
}