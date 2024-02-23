package pe.pjh.gendia.diagram.sequence

import com.intellij.psi.PsiClass

class JavaParticipant(psiClass: PsiClass) : Participant(psiClass.getQualifiedName().toString()) {


}
