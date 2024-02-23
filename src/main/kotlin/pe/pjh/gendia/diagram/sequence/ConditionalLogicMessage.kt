package pe.pjh.gendia.diagram.sequence

class ConditionalLogicMessage(
    override val caller: Participant,
    override val callee: Participant,
    override val callMsg: String,
    override val callMsgType: MessageArrowType,
    val expression: String
) : LogicMessage(caller, callee, callMsg, callMsgType) {

    override fun getCode(depth: Int): String {
        TODO("Not yet implemented")
    }

}