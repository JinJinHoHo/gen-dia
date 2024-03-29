package pe.pjh.gendia.diagram.sequence

import com.intellij.psi.PsiElement
import pe.pjh.gendia.diagram.TabUtil

abstract class ConditionalMultipleMessage(
    override val caller: Participant,
    override val callee: Participant,
    open val name: String?,
) : MultipleBlockMessage(caller, callee) {


    protected fun conditionalBlock(
        psiElement: PsiElement?,
        expression: String?,
        callee: Participant
    ) {

        if (psiElement == null) return

        blockMessages.add(ExpressionBlockMessage(caller, callee, psiElement, "${name}", expression))
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