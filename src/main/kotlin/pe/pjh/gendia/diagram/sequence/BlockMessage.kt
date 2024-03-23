package pe.pjh.gendia.diagram.sequence

import com.intellij.psi.*
import pe.pjh.gendia.diagram.UMLType
import pe.pjh.gendia.diagram.UndefindOperationException

/**
 * 메시지 그룹(하위로 n개의 메시지를 갖고 있음)
 */
open class BlockMessage(
    val caller: Participant,
    val callee: Participant
) : Message {

    val subMessages: MutableList<Message> = mutableListOf()

    constructor(
        caller: Participant,
        callee: Participant,
        psiElement: PsiElement, comment: String?
    ) : this(caller, callee) {
        addMessage(psiElement, comment)
    }

    override fun getCode(): String {
        TODO("Not yet implemented")
    }

    override fun getCodeLine(depth: Int): String {
        var code = ""
        subMessages.forEach {
            val subCode = it.getCodeLine(depth)
            if (subCode.isNotEmpty()) code += subCode
        }
        return code
    }

    private fun addSubMessage(psiCodeBlock: PsiCodeBlock?, comment: String?) {

        if (psiCodeBlock == null) return

        val psiElementList: List<PsiElement> = psiCodeBlock
            .children
            .filter { it !is PsiWhiteSpace }

        var index = 0
        do {
            when (val it = psiElementList.get(index)) {
                is PsiComment -> {

                    if (it.text.indexOf("//+") != -1) {
                        //코멘트 내용 추출
                        val tempComment = it.text.substringAfterLast("//+")

                        //코멘트 다음 라인 psiElement
                        val psiElement: PsiElement = it.nextSibling.nextSibling
                        addMessage(psiElement, tempComment)
                        index = psiElementList.indexOf(psiElement)
                    }
                }

                else -> {
                    if (it.lastChild is PsiBlockStatement) {
                        //하위에 코드블록이 있을 경우 재귀호출
                        addSubMessage((it.lastChild as PsiBlockStatement).codeBlock, comment)
                    }
                }
            }
            index++
        } while (index < psiElementList.size)
    }

    fun addMessage(psiElement: PsiElement, comment: String?) {
        println("${psiElement}--${psiElement.text}:'${comment}'")
        when (psiElement) {

            is PsiCodeBlock -> addSubMessage(psiElement, comment)

            is PsiBlockStatement -> addSubMessage(psiElement.codeBlock, comment)

            is PsiMethod -> addSubMessage(psiElement.body, comment)

            is PsiConditionalLoopStatement ->
                subMessages.add(ConditionalLoopMultipleMessage(caller, callee, psiElement, comment))

            is PsiIfStatement -> subMessages.add(IfElseConditionalMultipleMessage(caller, callee, comment, psiElement))

            is PsiTryStatement -> subMessages.add(
                TryCacheConditionalMultipleMessage(
                    caller,
                    callee,
                    comment,
                    psiElement
                )
            )

            is PsiMethodCallExpression -> {
                val psiMethod: PsiMethod? = psiElement.resolveMethod()
                if (psiMethod != null) {
                    addMessage(psiMethod, comment)
                } else {
                    addMessage(psiElement.methodExpression, comment)
                }
            }

            is PsiReferenceExpression -> {
                var temp = psiElement.resolve();
                if (temp != null) {
//                    addMessage(temp, comment)
                    //(temp as PsiFieldImpl).containingClass
                    subMessages.add(
                        CallMessage(
                            callee,
                            callee,
                            comment,
                            psiElement.text,
                            MessageArrowType.SolidLineWithArrowhead
                        )
                    )
                } else {
                    temp = psiElement.qualifierExpression;
                    if (temp != null) {
                        addMessage(temp, comment)
                    } else {
                        subMessages.add(
                            CallMessage(
                                callee,
                                callee,
                                comment,
                                psiElement.text,
                                MessageArrowType.SolidLineWithArrowhead
                            )
                        )
                    }
                }
            }

            is PsiExpressionStatement -> {

                //psiCall 추출
                val psiCall: PsiCall? = psiElement.children
                    .filterIsInstance<PsiCall>()
                    .getOrNull(0)

                val method: PsiMethod? = psiCall?.resolveMethod()
                if (method != null) {
                    //클래스 내부 메소드 호출시.
                    subMessages.add(MethodBlockMessage(callee, callee, method, comment))
                } else {
                    //외부 클래스 메소드 호출시
                    addMessage((psiCall as PsiMethodCallExpression), comment)
                }
            }

            is PsiDeclarationStatement -> {
                val psiElements: Array<PsiElement> = psiElement.declaredElements;
                psiElements.forEach {
                    println((it as PsiLocalVariable).initializer)
                }
            }

            else -> {
                println(psiElement.text)
                throw UndefindOperationException("Not Statement ${psiElement}")
            }
        }
    }
}