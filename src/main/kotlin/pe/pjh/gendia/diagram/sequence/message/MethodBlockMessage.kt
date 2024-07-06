package pe.pjh.gendia.diagram.sequence.message

import com.intellij.psi.PsiCodeBlock
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiType
import org.slf4j.LoggerFactory
import pe.pjh.gendia.diagram.sequence.MessageArrowType
import pe.pjh.gendia.diagram.sequence.SequenceDiagramConfig
import pe.pjh.gendia.diagram.sequence.participant.BaseParticipant

class MethodBlockMessage(
    caller: BaseParticipant,
    callee: BaseParticipant,
    psiMethod: PsiMethod,
    comment: String?,
    returnFunction: ReturnFunction?,
) : BlockMessage(caller, callee) {

    companion object {
        private val logger = LoggerFactory.getLogger(MethodBlockMessage::class.java)
    }

    //노출 명칭 처리.
    private val callMessage: Message = CallMessage(
        caller, callee,
        psiMethod.name, comment, null, MessageArrowType.SolidLineWithArrowhead
    )

    private var returnMessage: Message? = null

    init {
        val codeBlock: PsiCodeBlock? = psiMethod.body
        if (codeBlock != null) addMessage(codeBlock, null)

        //반환 가능 한 값이 있을 경우
        val returnType: PsiType? = psiMethod.returnType
        if (returnFunction == null) {
            //요청자와 요청을 받은자가 다른 경우만 처리.(동일한경우 자기자신을 호출하는 불필요한 메시지가 추가됨.
            if (caller != callee) {
                if (returnType != null && "void" != returnType.presentableText) {
                    returnMessage = CallMessage(
                        callee, caller,
                        null,
                        returnType.presentableText,
                        null,
                        MessageArrowType.DottedLineWithArrowhead
                    )
                }
            }
        } else {
            //web - req/res 처리시 사용됨.
            returnMessage = returnFunction.invoke(returnType)
        }
    }

    override fun getCodeLine(depth: Int, config: SequenceDiagramConfig): String {
        var code = callMessage.getCodeLine(depth, config) + super.getCodeLine(depth, config)

        val subCode: String? = returnMessage?.getCodeLine(depth, config)
        if (!subCode.isNullOrEmpty()) code += subCode
        return code
    }

    fun interface ReturnFunction {
        fun invoke(returnType: PsiType?): Message
    }
}