package pe.pjh.gendia.diagram.sequence.message

import com.intellij.psi.PsiCodeBlock
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiType
import pe.pjh.gendia.diagram.sequence.MessageArrowType
import pe.pjh.gendia.diagram.sequence.SequenceDiagramConfig
import pe.pjh.gendia.diagram.sequence.participant.BaseParticipant
import kotlin.text.isNullOrEmpty

class MethodBlockMessage(
    caller: BaseParticipant,
    callee: BaseParticipant,
    psiMethod: PsiMethod,
    comment: String?,
) : BlockMessage(caller, callee) {

    //노출 명칭 처리.
    private val callMessage: CallMessage =
        CallMessage(caller, callee, psiMethod.name, comment, null, MessageArrowType.SolidLineWithArrowhead)
    private var backMessage: CallMessage? = null


    init {
        val codeBlock: PsiCodeBlock? = psiMethod.body
        if (codeBlock != null) {
            addMessage(codeBlock, null)
        }

        //반환 가능 한 값이 있을 경우
        val returnType: PsiType? = psiMethod.returnType
        if (returnType != null && "void" != returnType.presentableText) {
            backMessage = CallMessage(
                callee, caller,
                null,
                returnType.presentableText,
                null,
                MessageArrowType.DottedLineWithArrowhead
            )
        }
    }

    override fun getCodeLine(depth: Int, config: SequenceDiagramConfig): String {
        var code = callMessage.getCodeLine(depth, config) + super.getCodeLine(depth, config)

        val subCode: String? = backMessage?.getCodeLine(depth, config)
        if (!subCode.isNullOrEmpty()) code += subCode
        return code
    }
}