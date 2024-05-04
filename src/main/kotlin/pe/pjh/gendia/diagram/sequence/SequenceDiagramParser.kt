package pe.pjh.gendia.diagram.sequence

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import pe.pjh.gendia.diagram.DiagramGenInfo
import pe.pjh.gendia.diagram.DiagramParsingProcess
import pe.pjh.gendia.diagram.TabUtil

class SequenceDiagramParser(
    project: Project, diagramGenInfo: DiagramGenInfo,
) : DiagramParsingProcess {

    private var parserContext = ParserContext.getInstanceAndInit(project)

    private val diagramConfig: SequenceDiagramParam = diagramGenInfo.diagramParam

    private val messageModels = mutableListOf<Message>()

    var startPointPsiMethods: MutableList<PsiMethod> = mutableListOf()


    override fun collection() {

        diagramConfig.startPoint
            .split(",")
            .forEach {

                //클래스 명 추출
                val fullQualifiedClassName: String = it.substringBeforeLast(".")

                //메소드명 추출
                val methodName: String = it.substringAfterLast(".")

                //PsiClass 추출
                val psiClass: PsiClass = parserContext.findClass(fullQualifiedClassName)
                    ?: throw RuntimeException("$fullQualifiedClassName 클래스 없음.")

                //PsiMethod 추출
                val m: Array<PsiMethod> = psiClass.findMethodsByName(methodName, false)
                if (m.isEmpty()) throw RuntimeException("$methodName 메소드 없음.")

                //todo [예정] 오버로드된 메소드 처리 방향 고민 필요.
                startPointPsiMethods.add(m[0])
            }
    }

    override fun analysis() {
        val actor = parserContext.getParticipant(diagramConfig.actorName)

        startPointPsiMethods.forEach {
            val psiClass: PsiClass = it.containingClass ?: return
            messageModels.add(
                MethodBlockMessage(
                    actor, parserContext.getParticipant(psiClass), it, null
                )
            )
        }
    }

    override fun generate(): String {
        var code = ""

        //기본 설정 영역
        if (diagramConfig.autonumber) {
            code += TabUtil.textLine(1, "autonumber")
            code += "\n"
        }

        //Participant/Actor 영역
        parserContext.participantMap.forEach { (t, u) -> code += TabUtil.textLine(1, u.getMessage()) }
        code += "\n"

        //Messages 영역
        messageModels.forEach {
            code += it.getCodeLine(1)
        }
        return code
    }
}