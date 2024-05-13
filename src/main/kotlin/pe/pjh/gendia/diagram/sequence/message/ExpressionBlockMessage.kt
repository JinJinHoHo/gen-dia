package pe.pjh.gendia.diagram.sequence.message

import com.intellij.psi.PsiElement
import pe.pjh.gendia.diagram.sequence.participant.BaseParticipant

/**
 * 메시지 그룹(하위로 n개의 메시지를 갖고 있음)
 */
class ExpressionBlockMessage(
    caller: BaseParticipant,
    callee: BaseParticipant,
    psiElement: PsiElement,
    comment: String?,
    val expression: String?,
) : BlockMessage(caller, callee, psiElement, comment)