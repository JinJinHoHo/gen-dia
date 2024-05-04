package pe.pjh.gendia.diagram.sequence

import org.slf4j.LoggerFactory

/**
 * 기본 메시지
 */
open class CallMessage(
    open val caller: Participant,
    open val callee: Participant,
    open val comment: String?,
    open val callMsgType: MessageArrowType,
) : Message {

    companion object {
        private val logger = LoggerFactory.getLogger(CallMessage::class.java)
    }

    private var expression: String = ""

    constructor(
        caller: Participant,
        callee: Participant,
        comment: String?, expression: String,
        callMsgType: MessageArrowType,
    ) : this(caller, callee, comment, callMsgType) {
        this.expression = expression
    }

    override fun getCode(): String {
        val code = if (expression.isEmpty()) {
            """${caller.name}${callMsgType.expression}${callee.name}:${comment}"""
        } else {
            """${caller.name}${callMsgType.expression}${callee.name}:${comment}[${expression}]"""
        }

        if (logger.isDebugEnabled) logger.debug(code)

        return code
    }

    override fun toString(): String {
        return """${caller.name}${callMsgType.expression}${callee.name}:${comment}"""
    }
}