package pe.pjh.gendia

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInspection.util.IntentionFamilyName
import com.intellij.codeInspection.util.IntentionName
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiUtilBase
import com.intellij.util.IncorrectOperationException
import org.intellij.plugins.markdown.lang.MarkdownLanguage
import pe.pjh.gendia.diagram.*
import pe.pjh.gendia.diagram.sequence.SequenceDiagramConfig
import java.util.*

class DiagramGenAction : IntentionAction {

    override fun getText(): @IntentionName String {
        return "Update Diagram"
    }

    override fun getFamilyName(): @IntentionFamilyName String {
        return "Update Diagram"
    }

    override fun isAvailable(project: Project, editor: Editor, psiFile: PsiFile): Boolean {

        val psiElement = PsiUtilBase.getElementAtCaret(editor) ?: return false

        try {
            getType(psiElement)
            return true
        } catch (e: IllegalStateException) {
            return false
        }
    }


    @Throws(IncorrectOperationException::class)
    override fun invoke(project: Project, editor: Editor, psiFile: PsiFile) {

        val psiElement = PsiUtilBase.getElementAtCaret(editor) ?: return

        val (uMLType: UMLType, diagramType: DiagramType) = getType(psiElement)

        val commentList = arrayListOf<String>()
        psiElement.parent.children
            .filter { it !is PsiWhiteSpace }
            .map { it.text.trim() }
            .filter { it.indexOf("##") != -1 }
            .mapTo(commentList) { it.removePrefix("##").trim() }

        var sp = ""
        commentList.forEach {
            val keyValue = it.split(":")
            if ("startpoint" == keyValue[0].lowercase(Locale.getDefault())) {
                sp = keyValue[1].trim()
            }
        }
        SequenceDiagramConfig(
            uMLType,
            diagramType,
            sp
        )
    }

    override fun startInWriteAction(): Boolean {
        return false
    }


    private fun getType(psiElement: PsiElement): Pair<UMLType, DiagramType> {

        val language = psiElement.language

        if (language !is MarkdownLanguage) throw IllegalStateException()

        val psiElementParent = psiElement.parent

        //테스트 화면에 org.intellij.plugins.markdown.lang.psi.impl.MarkdownCodeFence 사용시 에러 발생됨. 차후 확인시 수정 필요.
        if (psiElementParent.toString() != "PsiElement(Markdown:Markdown:CODE_FENCE)") {
            throw IllegalStateException()
        }

        //하단 소스 재정리 필요.
        val psiElementLang = psiElementParent.children[1]

        var codeLang: String? = null
        if (psiElementLang.toString() == "PsiElement(Markdown:Markdown:FENCE_LANG)")
            codeLang = psiElementLang.text

        if (codeLang == null) throw IllegalStateException()

        val umlType = UMLType.valueOf(codeLang)

        val diagramType = DiagramType.valueOf(psiElementParent.children[3].text)

        return Pair(umlType, diagramType)
    }
}