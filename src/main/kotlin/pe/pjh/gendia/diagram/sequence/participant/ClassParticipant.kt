package pe.pjh.gendia.diagram.sequence.participant

import com.intellij.psi.PsiClass
import kotlin.toString

data class ClassParticipant(val psiClass: PsiClass, override val alias: String?) : BaseParticipant(psiClass.qualifiedName.toString(), alias)
