package pe.pjh.gendia.diagram

import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiManager
import com.intellij.psi.search.GlobalSearchScope

class SequenceDiagramParser(project: Project, diagramGenInfo: DiagramGenInfo) : DiagramParsingProcess {

    private val diagramParam: SequenceDiagramParam = diagramGenInfo.diagramParam
    private val project: Project = project

    override fun collection() {
        diagramParam


        // 사용중인 project 객체.
        val fullQualifiedClassName: String = "com.example.MyClass" // 원하는 클래스의 전체 경로

        val psiManager: PsiManager = PsiManager.getInstance(project)
        val javaPsiFacade: JavaPsiFacade = JavaPsiFacade.getInstance(psiManager.getProject())
        var psiClass: PsiClass? = javaPsiFacade.findClass(
            fullQualifiedClassName,
            GlobalSearchScope.allScope(project)
        )
    }

    override fun analysis() {
        TODO("Not yet implemented")
    }

    override fun generate() {
        TODO("Not yet implemented")
    }
}