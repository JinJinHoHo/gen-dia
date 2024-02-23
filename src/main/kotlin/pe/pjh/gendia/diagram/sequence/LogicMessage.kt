package pe.pjh.gendia.diagram.sequence

import pe.pjh.gendia.diagram.TabUtil

open class LogicMessage(
    open val caller: Participant,
    open val callee: Participant,
    open val callMsg: String,
    open val callMsgType: MessageArrowType
) : Message {

    override fun getCode(depth: Int): String {
        return TabUtil.textLine(depth, caller.name + callMsgType.expression + callee.name + ":" + callMsg)
    }

}