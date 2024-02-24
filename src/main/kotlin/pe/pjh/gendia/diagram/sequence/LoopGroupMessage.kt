package pe.pjh.gendia.diagram.sequence

import com.intellij.psi.PsiCodeBlock
import com.intellij.psi.PsiForStatement
import com.intellij.psi.PsiStatement
import pe.pjh.gendia.diagram.TabUtil

class LoopGroupMessage(
    override val callee: Participant,
    private val name: String,
    psiForStatement: PsiForStatement,

    ) : GroupMessage(callee) {

    init {
        val psiStatement: PsiStatement? = psiForStatement.body
        if (psiStatement != null) {
            val psiCodeBlock: PsiCodeBlock = psiStatement.children[0] as PsiCodeBlock
            subMessageParsing(psiCodeBlock)
        }
    }

    override fun getCodeLine(depth: Int): String {

        //서브 코드에 노츨되는 항목이 없으면 빈값 반환.
        if (subMessages.isEmpty()) return ""

        //
        var code = TabUtil.textLine(depth, "loop $name")
        code += super.getCodeLine(depth + 1)
        code += TabUtil.textLine(depth, "end")
        return code
    }


}