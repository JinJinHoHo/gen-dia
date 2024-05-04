package pe.pjh.gendia.diagram.sequence

import com.intellij.psi.PsiClass

data class ClassParticipant(val psiClass: PsiClass) : Participant(psiClass.qualifiedName.toString())
