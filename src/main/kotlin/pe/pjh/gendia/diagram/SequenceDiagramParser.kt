package pe.pjh.gendia.diagram

import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiMethod
import com.intellij.psi.search.GlobalSearchScope

class SequenceDiagramParser(
    protected val project: Project, diagramGenInfo: DiagramGenInfo
) : DiagramParsingProcess {

    private val psiManager: PsiManager = PsiManager.getInstance(project)
    protected val diagramParam: SequenceDiagramParam = diagramGenInfo.diagramParam

    var startPointPsiMethods: MutableList<PsiMethod> = mutableListOf()
        get() = field


    override fun collection() {

        val javaPsiFacade: JavaPsiFacade = JavaPsiFacade.getInstance(psiManager.getProject())

        diagramParam.startPoint.split(",").forEach {

            //클래스 명 추출
            val fullQualifiedClassName: String = it.substringBeforeLast(".")

            //메소드명 추출
            val methodName: String = it.substringAfterLast(".")


            //PsiClass 추출
            val psiClass: PsiClass? = javaPsiFacade.findClass(
                fullQualifiedClassName, GlobalSearchScope.allScope(project)
            )
            if (psiClass == null) throw RuntimeException(fullQualifiedClassName + " 클래스 없음.")

            //PsiMethod 추출
            val m: Array<PsiMethod> = psiClass.findMethodsByName(methodName, false);
            if (m.isEmpty()) throw RuntimeException(methodName + " 메소드 없음.")


            startPointPsiMethods.add(m.get(0))
        }
    }

    override fun analysis() {
        TODO("Not yet implemented")
    }

    override fun generate() {
        TODO("Not yet implemented")
    }


}