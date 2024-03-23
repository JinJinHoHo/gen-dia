package pe.pjh.gendia.diagram.sequence

import com.intellij.psi.*
import pe.pjh.gendia.diagram.UndefindOperationException

/**
 * 메시지 그룹(하위로 n개의 메시지를 갖고 있음)
 */
class ExpressionBlockMessage(
    caller: Participant,
    callee: Participant,
    psiElement: PsiElement,
    comment: String?,
    private val expression: String?
) : BlockMessage(caller, callee, psiElement, comment) {

    fun getExpression(): String? {
        return expression
    }
}