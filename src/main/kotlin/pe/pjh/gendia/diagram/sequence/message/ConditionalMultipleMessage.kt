package pe.pjh.gendia.diagram.sequence.message

import com.intellij.psi.PsiElement
import org.slf4j.LoggerFactory
import pe.pjh.gendia.diagram.TabUtil
import pe.pjh.gendia.diagram.sequence.ConditionalMarkType
import pe.pjh.gendia.diagram.sequence.SequenceDiagramConfig
import pe.pjh.gendia.diagram.sequence.participant.BaseParticipant
import kotlin.collections.forEachIndexed
import kotlin.collections.isNotEmpty
import kotlin.jvm.java
import kotlin.run

abstract class ConditionalMultipleMessage(
    override val caller: BaseParticipant,
    override val callee: BaseParticipant,
    open val name: String?,
) : MultipleBlockMessage(caller, callee) {

    companion object {
        private val logger = LoggerFactory.getLogger(ConditionalMultipleMessage::class.java)
    }

    protected fun conditionalBlock(
        psiElement: PsiElement?,
        expression: String?,
        callee: BaseParticipant,
    ) {

        if (psiElement == null) return

        blockMessages.add(ExpressionBlockMessage(caller, callee, psiElement, "$name", expression))
    }

    abstract fun mark(): ConditionalMarkType

    override fun getCodeLine(depth: Int, config: SequenceDiagramConfig): String {

        if (blockMessages.isEmpty()) return ""

        val markType = mark()
        var code = TabUtil.textLine(depth, "Note right of ${callee.name}: $name")

        blockMessages.forEachIndexed { index, it ->
            run {
                if (it.subMessages.isNotEmpty()) {
                    val title = if (it is ExpressionBlockMessage) it.expression else name
                    code += TabUtil.textLine(
                        depth,
                        if (index == 0) "${markType.mark1} $title" else "${markType.mark2} $title"
                    )
                    code += it.getCodeLine(depth + 1, config)
                }
            }
        }
        code += TabUtil.textLine(depth, markType.mark3)

        if (logger.isDebugEnabled) logger.debug(code)

        return code
    }
}