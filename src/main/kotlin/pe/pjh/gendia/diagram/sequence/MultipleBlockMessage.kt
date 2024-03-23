package pe.pjh.gendia.diagram.sequence

import pe.pjh.gendia.diagram.UMLType

/**
 * 그룹메시지를 n개가 필요 할때 선언. if-else, switch trycatch 등등 사용해서 사용.
 */
open class MultipleBlockMessage(
    open val caller: Participant,
    open val callee: Participant
) : Message {

    val blockMessages = mutableListOf<BlockMessage>()
    override fun getCode(): String {
        TODO("Not yet implemented")
    }

}