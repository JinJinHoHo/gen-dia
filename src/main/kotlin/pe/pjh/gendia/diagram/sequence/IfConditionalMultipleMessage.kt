package pe.pjh.gendia.diagram.sequence

import com.intellij.psi.PsiBlockStatement
import com.intellij.psi.PsiExpressionStatement
import com.intellij.psi.PsiIfStatement
import pe.pjh.gendia.diagram.TabUtil

class IfConditionalMultipleMessage(
    override val callee: Participant,
    val name: String,
    psiIfStatement: PsiIfStatement,
) : MultipleBlockMessage(callee) {

    init {
        println(psiIfStatement.thenBranch)
        println(psiIfStatement.condition)


        //루프는 단일 그룹메시지.
        val ifBlockMessage = BlockMessage(callee)
        blockMessages.add(ifBlockMessage)


        when (psiIfStatement.thenBranch) {
            is PsiBlockStatement -> {
                ifBlockMessage.subMessageParsing((psiIfStatement.thenBranch as PsiBlockStatement).codeBlock)
            }

            is PsiExpressionStatement -> {
                ifBlockMessage.addMessageByPsiType(psiIfStatement.thenBranch as PsiExpressionStatement, name)
            }
        }

        val elseBlockMessage = BlockMessage(callee)
        blockMessages.add(elseBlockMessage)
        when (psiIfStatement.elseBranch) {
            is PsiBlockStatement -> {
                elseBlockMessage.subMessageParsing((psiIfStatement.elseBranch as PsiBlockStatement).codeBlock)
            }

            is PsiExpressionStatement -> {
                elseBlockMessage.addMessageByPsiType(psiIfStatement.elseBranch as PsiExpressionStatement, name)
            }
        }
    }

    override fun getCodeLine(depth: Int): String {

        if (blockMessages.isEmpty()) return ""

        var code = ""

        blockMessages.forEach {

            if (it.subMessages.isEmpty()) return@forEach

            code += TabUtil.textLine(
                depth,
                if (code.isEmpty()) "alt $name" else "else $name"
            )
            code += it.getCodeLine(depth + 1)
            code += TabUtil.textLine(depth, "end")
        }
        return code
    }
}