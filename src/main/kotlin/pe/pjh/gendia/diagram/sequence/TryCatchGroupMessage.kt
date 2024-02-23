package pe.pjh.gendia.diagram.sequence

class TryCatchGroupMessage(
    override val callee: Participant,
    val tryMessage: LogicMessage,
    val catchMessages: List<ConditionalLogicMessage>,
    ) : GroupMessage(callee) {
    override fun getCode(depth: Int): String {
        TODO("Not yet implemented")
    }

}