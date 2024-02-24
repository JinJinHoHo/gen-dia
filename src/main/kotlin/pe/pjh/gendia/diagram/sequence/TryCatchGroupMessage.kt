package pe.pjh.gendia.diagram.sequence

class TryCatchGroupMessage(
    override val callee: Participant,
    val tryMessage: CallMessage,
    val catchMessages: List<ConditionalCallMessage>,
    ) : GroupMessage(callee) {
    override fun getCode(): String {
        TODO("Not yet implemented")
    }

}