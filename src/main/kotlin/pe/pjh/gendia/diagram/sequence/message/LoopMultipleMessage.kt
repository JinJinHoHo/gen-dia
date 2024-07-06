package pe.pjh.gendia.diagram.sequence.message

import com.intellij.psi.PsiBlockStatement
import com.intellij.psi.PsiConditionalLoopStatement
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import pe.pjh.gendia.diagram.TabUtil
import pe.pjh.gendia.diagram.sequence.SequenceDiagramConfig
import pe.pjh.gendia.diagram.sequence.participant.BaseParticipant

/**
 * For, While 조건 루프
 */
class LoopMultipleMessage(
    override val caller: BaseParticipant,
    override val callee: BaseParticipant,
    psiConditionalLoopStatement: PsiConditionalLoopStatement,
    private val comment: String?,

    ) : MultipleBlockMessage(caller, callee) {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(LoopMultipleMessage::class.java)
    }

    init {

        //루프는 단일 그룹메시지.
        val blockMessage = BlockMessage(caller, callee)
        blockMessages.add(blockMessage)

        val psiStatement = psiConditionalLoopStatement.body as PsiBlockStatement
        blockMessage.addMessage(psiStatement, comment)
    }

    override fun getCodeLine(depth: Int, config: SequenceDiagramConfig): String {

        val groupMessage = blockMessages.getOrNull(0) ?: return ""

        //서브 코드에 노츨되는 항목이 없으면 빈값 반환.
        if (groupMessage.subMessages.isEmpty()) return ""

        //
        var code = TabUtil.textLine(depth, "loop $comment")
        code += groupMessage.getCodeLine(depth + 1, config)
        code += TabUtil.textLine(depth, "end")

        if (logger.isDebugEnabled) logger.debug(code)

        return code
    }

    override fun toString(): String {
        return """${caller.name}->${callee.name}:${comment}"""
    }
}