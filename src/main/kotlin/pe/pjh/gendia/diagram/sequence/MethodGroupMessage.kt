package pe.pjh.gendia.diagram.sequence

import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiType

class MethodGroupMessage(
    caller: Participant,
    override val callee: Participant,
    psiMethod: PsiMethod
) : GroupMessage(callee) {

    private val callMessage: LogicMessage =
        LogicMessage(caller, callee, psiMethod.name, MessageArrowType.SolidLineWithArrowhead)
    private var backMessage: LogicMessage? = null


    init {
        val returnType: PsiType? = psiMethod.returnType

        extracted(psiMethod.body)

        //반환 가능 한 값이 있을 경우
        if (returnType != null && "void" != returnType.presentableText) {
            backMessage = LogicMessage(
                callee, caller,
                returnType.presentableText, MessageArrowType.DottedLineWithArrowhead
            )
        }
    }

    override fun getCode(depth: Int): String {
        var code = callMessage.getCode(depth)
        code += super.getCode(depth)

        val subCode: String? = backMessage?.getCode(depth)
        if (!subCode.isNullOrEmpty()) code += subCode
        return code
    }


}