package pe.pjh.gendia.diagram.sequence

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import pe.pjh.gendia.diagram.DiagramParsingProcess
import pe.pjh.gendia.diagram.TabUtil
import pe.pjh.gendia.diagram.sequence.message.CallMessage
import pe.pjh.gendia.diagram.sequence.message.Message
import pe.pjh.gendia.diagram.sequence.message.MethodBlockMessage
import pe.pjh.gendia.diagram.sequence.participant.BaseParticipant

class SequenceDiagramParser(
    project: Project,
    val config: SequenceDiagramConfig,
) : DiagramParsingProcess {

    private var parserContext = ParserContext.getInstanceAndInit(project, config)

    private val messageModels = mutableListOf<Message>()

    var startPointPsiMethods: MutableList<PsiMethod> = mutableListOf()

    override fun collection() {

        config.startPoint
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
        val actor = parserContext.getParticipant(config.actorName)
        config.web

        startPointPsiMethods.forEach {
            val psiClass: PsiClass = it.containingClass ?: return
            if (config.web) {
                val callee: BaseParticipant = parserContext.getParticipant(psiClass)
                messageModels.add(
                    MethodBlockMessage(
                        actor, callee, it,
                        "Request",
                        MethodBlockMessage.ReturnFunction {
                            CallMessage(
                                callee, actor, null, "Response", null,
                                MessageArrowType.DottedLineWithArrowhead
                            )
                        }
                    )
                )
            } else {
                messageModels.add(
                    MethodBlockMessage(actor, parserContext.getParticipant(psiClass), it, "Call", null)
                )
            }
        }
    }

    override fun generate(): String {
        var code = ""

        //기본 설정 영역
        if (config.autonumber) {
            code += TabUtil.textLine(1, "autonumber")
            code += "\n"
        }

        //Participant/Actor 영역
        code += parserContext.generateMessageCode()
        code += "\n"


        //Messages 영역
        code += messageModels.map {
            it.getCodeLine(1, config)
        }.joinToString("\n")
        return code
    }
}