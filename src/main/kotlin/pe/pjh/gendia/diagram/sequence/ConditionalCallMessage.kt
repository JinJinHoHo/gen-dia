package pe.pjh.gendia.diagram.sequence

class ConditionalCallMessage(
    override val caller: Participant,
    override val callee: Participant,
    override val callMsg: String,
    override val callMsgType: MessageArrowType,
    val expression: String
) : CallMessage(caller, callee, callMsg, callMsgType) {

}