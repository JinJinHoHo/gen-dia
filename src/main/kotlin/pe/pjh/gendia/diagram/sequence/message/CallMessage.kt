package pe.pjh.gendia.diagram.sequence.message

import org.slf4j.LoggerFactory
import pe.pjh.gendia.diagram.sequence.MessageArrowType
import pe.pjh.gendia.diagram.sequence.MessageLabelType
import pe.pjh.gendia.diagram.sequence.SequenceDiagramConfig
import pe.pjh.gendia.diagram.sequence.participant.BaseParticipant
import kotlin.jvm.java

/**
 * 기본 메시지
 */
open class CallMessage(
    open val caller: BaseParticipant,
    open val callee: BaseParticipant,
    open val methodName: String?,
    open val comment: String?,
    open val expression: String?,
    open val callMsgType: MessageArrowType,
) : Message {

    companion object {
        private val logger = LoggerFactory.getLogger(CallMessage::class.java)
    }

    override fun getCode(config: SequenceDiagramConfig): String {
        var code: String? = null
        when (config.messageLabelType) {
            MessageLabelType.METHOD_COMMENT -> {
                code = """${caller.name}${callMsgType.expression}${callee.name}:${comment}"""
                if (methodName != null) code += """ / $methodName"""
            }

            MessageLabelType.COMMENT -> {
                code = """${caller.name}${callMsgType.expression}${callee.name}:${comment}"""
            }

            MessageLabelType.METHOD -> {
                code = """${caller.name}${callMsgType.expression}${callee.name}:${methodName}"""
            }
        }

        if (expression != null && expression!!.isNotEmpty()) {
            code += """[$expression]"""
        }

        if (logger.isDebugEnabled) logger.debug(code)

        return code
    }

    override fun toString(): String {
        return """${caller.name}${callMsgType.expression}${callee.name}:${comment}"""
    }
}