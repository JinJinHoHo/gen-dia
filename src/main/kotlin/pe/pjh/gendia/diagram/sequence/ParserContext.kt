package pe.pjh.gendia.diagram.sequence

import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.search.GlobalSearchScope
import pe.pjh.gendia.diagram.DiagramGenConfig
import pe.pjh.gendia.diagram.TabUtil
import pe.pjh.gendia.diagram.sequence.participant.ActorParticipant
import pe.pjh.gendia.diagram.sequence.participant.BaseParticipant
import pe.pjh.gendia.diagram.sequence.participant.ClassParticipant

class ParserContext private constructor(
    private val project: Project,
    private var config: DiagramGenConfig,
) : AutoCloseable {
    companion object {
        private var instance: ParserContext? = null

        fun getInstanceAndInit(project: Project, config: DiagramGenConfig): ParserContext {
            if (instance == null) {
                instance = ParserContext(project, config)
            }
            return instance!!
        }

        fun getInstance(): ParserContext {
            if (instance == null) {
                throw throw RuntimeException("getInstanceAndInit 실행해서 초기화 필요.")
            }
            return instance!!
        }
    }

    //    private val psiManager: PsiManager = PsiManager.getInstance(project)
    private var javaPsiFacade: JavaPsiFacade = JavaPsiFacade.getInstance(project)
    val baseParticipantMap: MutableMap<String, BaseParticipant> = mutableMapOf()

    fun getParticipant(actorName: String): BaseParticipant {
        return baseParticipantMap.getOrPut(actorName) {
            ActorParticipant(actorName)
        }
    }

    fun getParticipant(psiClass: PsiClass): BaseParticipant {
        return baseParticipantMap.getOrPut(psiClass.qualifiedName.toString()) {
            ClassParticipant(psiClass)
        }
    }

    fun findClass(fullQualifiedClassName: String): PsiClass? {
        return javaPsiFacade.findClass(
            fullQualifiedClassName, GlobalSearchScope.allScope(project)
        )
    }

    fun generateMessageCode(): String {
        return baseParticipantMap.map { (_, u) -> TabUtil.textLine(1, u.getMessage()) }.joinToString("")
    }

    override fun close() {
        instance = null
        baseParticipantMap.clear()
    }
}