package pe.pjh.gendia.diagram.sequence

import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiMethod
import com.intellij.psi.search.GlobalSearchScope
import pe.pjh.gendia.diagram.DiagramGenInfo
import pe.pjh.gendia.diagram.DiagramParsingProcess
import pe.pjh.gendia.diagram.TabUtil

class SequenceDiagramParser(
    private val project: Project, diagramGenInfo: DiagramGenInfo
) : DiagramParsingProcess {

    //IDE 관련 기본 설정 변수.
    private val psiManager: PsiManager = PsiManager.getInstance(project)

    //IDE 관련 자바 설정 변수.
    private val javaPsiFacade: JavaPsiFacade = JavaPsiFacade.getInstance(psiManager.project)

    private val diagramParam: SequenceDiagramParam = diagramGenInfo.diagramParam

    var startPointPsiMethods: MutableList<PsiMethod> = mutableListOf()
        get() = field

    private val participantMap = mutableMapOf<String, Participant>()

    private val messages = mutableListOf<Message>()

    override fun collection() {

        diagramParam.startPoint.split(",").forEach {

            //클래스 명 추출
            val fullQualifiedClassName: String = it.substringBeforeLast(".")

            //메소드명 추출
            val methodName: String = it.substringAfterLast(".")


            //PsiClass 추출
            val psiClass: PsiClass? = javaPsiFacade.findClass(
                fullQualifiedClassName, GlobalSearchScope.allScope(project)
            )
            if (psiClass == null) throw RuntimeException("$fullQualifiedClassName 클래스 없음.")

            //PsiMethod 추출
            val m: Array<PsiMethod> = psiClass.findMethodsByName(methodName, false)
            if (m.isEmpty()) throw RuntimeException("$methodName 메소드 없음.")

            //todo 오버로드된 메소드 처리 방향 고민 필요.
            startPointPsiMethods.add(m[0])
        }
    }

    override fun analysis() {
        val actor = Participant(diagramParam.actorName)
        participantMap[actor.name] = actor

        startPointPsiMethods.forEach {
            val psiClass: PsiClass = it.containingClass ?: return
            val clazz = JavaParticipant(psiClass)
            participantMap[clazz.name] = clazz

            messages.add(MethodGroupMessage(actor, clazz, it, null))
        }
    }

    override fun generate(): String {
        var code = ""

        //기본 설정 영역
        if (diagramParam.autonumber){
            code += TabUtil.textLine(1, "autonumber")
            code += "\n"
        }

        //액터 설정 영역
        code += TabUtil.textLine(1, "actor " + diagramParam.actorName)
        code += "\n"

        //Participant 영역
        participantMap.forEach { (t, u) ->code += TabUtil.textLine(1, "participant " + u.name)}
        code += "\n"

        //Messages 영역
        messages.forEach {
            code += it.getCodeLine(1)
        }
        return code
    }


}