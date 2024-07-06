package pe.pjh.gendia.diagram.sequence.participant

import com.intellij.psi.PsiClass

data class ClassParticipant(val psiClass: PsiClass, override val alias: String?) :
    BaseParticipant(psiClass.qualifiedName.toString(), alias)
