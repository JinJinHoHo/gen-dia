package pe.pjh.gendia.diagram.sequence

open class CallMessage(
    open val caller: Participant,
    open val callee: Participant,
    open val callMsg: String,
    open val callMsgType: MessageArrowType
) : Message {

    override fun getCode(): String {
        return """${caller.name}${callMsgType.expression}${callee.name}:$callMsg"""
    }
}