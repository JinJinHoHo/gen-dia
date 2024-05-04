package pe.pjh.gendia.diagram.sequence

import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiManager
import com.intellij.psi.search.GlobalSearchScope

class ParserContext private constructor(
    private val project: Project,
) {
    companion object {
        private var instance: ParserContext? = null

        fun getInstanceAndInit(project: Project): ParserContext {
            if (instance == null) {
                instance = ParserContext(project)
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
    private val javaPsiFacade: JavaPsiFacade = JavaPsiFacade.getInstance(project)
    val participantMap: MutableMap<String, Participant> = mutableMapOf()

    fun getParticipant(actorName: String): Participant {
        val actor = ActorParticipant(actorName)
        participantMap[actorName] = actor
        return actor
    }

    fun getParticipant(psiClass: PsiClass): Participant {
        val clazz = ClassParticipant(psiClass)
        participantMap[clazz.name] = clazz
        return clazz
    }

    fun findClass(fullQualifiedClassName: String): PsiClass? {
        return javaPsiFacade.findClass(
            fullQualifiedClassName, GlobalSearchScope.allScope(project)
        )
    }
}