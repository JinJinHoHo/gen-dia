package pe.pjh.gendia.diagram.sequence.message

import pe.pjh.gendia.diagram.sequence.SequenceDiagramConfig
import pe.pjh.gendia.diagram.sequence.participant.BaseParticipant

/**
 * 그룹메시지를 n개가 필요 할때 선언. if-else, switch trycatch 등등 사용해서 사용.
 */
abstract class MultipleBlockMessage(
    open val caller: BaseParticipant,
    open val callee: BaseParticipant
) : Message {

    val blockMessages = mutableListOf<BlockMessage>()
    override fun getCode(config: SequenceDiagramConfig): String {
        TODO("Not yet implemented")
    }

}