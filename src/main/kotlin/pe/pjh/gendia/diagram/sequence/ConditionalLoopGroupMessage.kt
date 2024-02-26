package pe.pjh.gendia.diagram.sequence

import com.intellij.psi.PsiCodeBlock
import com.intellij.psi.PsiConditionalLoopStatement
import com.intellij.psi.PsiStatement
import pe.pjh.gendia.diagram.TabUtil

/**
 * For, While 조건 루프
 */
class ConditionalLoopGroupMessage(
    override val callee: Participant,
    private val name: String,
    psiConditionalLoopStatement: PsiConditionalLoopStatement,

    ) : GroupBlockMessage(callee) {


    init {

        //루프는 단일 그룹메시지.
        val groupMessage = GroupMessage(callee)
        groupMessages.add(GroupMessage(callee))


        val psiStatement: PsiStatement? = psiConditionalLoopStatement.body
        if (psiStatement != null) {
            val psiCodeBlock: PsiCodeBlock = psiStatement.children[0] as PsiCodeBlock
            groupMessage.subMessageParsing(psiCodeBlock)
        }
    }

    override fun getCodeLine(depth: Int): String {

        val groupMessage = groupMessages.getOrNull(0)
        if(groupMessage ==null) return ""

        //서브 코드에 노츨되는 항목이 없으면 빈값 반환.
        if (groupMessage.subMessages.isEmpty()) return ""

        //
        var code = TabUtil.textLine(depth, "loop $name")
        code += groupMessage.getCodeLine(depth + 1)
        code += TabUtil.textLine(depth, "end")
        return code
    }
}