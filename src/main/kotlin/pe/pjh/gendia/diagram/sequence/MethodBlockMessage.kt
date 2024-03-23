package pe.pjh.gendia.diagram.sequence

import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiType

class MethodBlockMessage(
    caller: Participant,
    callee: Participant,
    psiMethod: PsiMethod,
    name: String?
) : BlockMessage(caller, callee) {

    private val callMessage: CallMessage
    private var backMessage: CallMessage? = null


    init {

        //노출 명칭 처리.
        val title: String = if (name.isNullOrBlank()) {
            psiMethod.name
        } else {
            "${name}:${psiMethod.name}"
        }
        callMessage = CallMessage(caller, callee, title, MessageArrowType.SolidLineWithArrowhead)

        addMessage(psiMethod, null)

        //반환 가능 한 값이 있을 경우
        val returnType: PsiType? = psiMethod.returnType
        if (returnType != null && "void" != returnType.presentableText) {
            backMessage = CallMessage(
                callee, caller,
                returnType.presentableText, MessageArrowType.DottedLineWithArrowhead
            )
        }
    }

    override fun getCodeLine(depth: Int): String {
        var code = callMessage.getCodeLine(depth)
        code += super.getCodeLine(depth)

        val subCode: String? = backMessage?.getCodeLine(depth)
        if (!subCode.isNullOrEmpty()) code += subCode
        return code
    }
}