package pe.pjh.gendia.diagram.sequence

/**
 * 기본 메시지
 */
open class CallMessage(
    open val caller: Participant,
    open val callee: Participant,
    open val comment: String?,
    open val callMsgType: MessageArrowType
) : Message {

    private var expression: String = ""

    constructor(
        caller: Participant, callee: Participant,
        comment: String?, expression: String,
        callMsgType: MessageArrowType
    ) : this(caller, callee, comment, callMsgType) {
        this.expression = expression
    }

    override fun getCode(): String {
        if (expression.isEmpty()) {
            return """${caller.name}${callMsgType.expression}${callee.name}:${comment}"""
        }
        return """${caller.name}${callMsgType.expression}${callee.name}:${comment}[${expression}]"""
    }
}