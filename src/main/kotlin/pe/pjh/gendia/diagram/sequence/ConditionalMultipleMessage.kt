package pe.pjh.gendia.diagram.sequence

import com.intellij.psi.PsiExpression
import com.intellij.psi.PsiIfStatement
import com.intellij.psi.PsiStatement
import pe.pjh.gendia.diagram.TabUtil

abstract class ConditionalMultipleMessage(
    override val callee: Participant,
    open val name: String?,
) : MultipleBlockMessage(callee) {


    protected fun conditionalBlock(
        psiStatement: PsiStatement?,
        expression: String?,
        callee: Participant
    ) {

        if (psiStatement == null) return

        blockMessages.add(ExpressionBlockMessage(callee, psiStatement, "${name}", expression))
    }

    abstract fun mark(): ConditionalMarkType

    override fun getCodeLine(depth: Int): String {

        if (blockMessages.isEmpty()) return ""

        val markType = mark()
        var code = TabUtil.textLine(depth, "Note right of ${callee.name}: ${name}")


        blockMessages.forEachIndexed { index, it ->
            run {
                if (!it.subMessages.isEmpty()) {
                    val title = if (it is ExpressionBlockMessage) it.getExpression() else name
                    code += TabUtil.textLine(
                        depth,
                        if (index == 0) "${markType.mark1} ${title}" else "${markType.mark2} ${title}"
                    )
                    code += it.getCodeLine(depth + 1)
                }
            }
        }

        return code + TabUtil.textLine(depth, markType.mark3)
    }
}