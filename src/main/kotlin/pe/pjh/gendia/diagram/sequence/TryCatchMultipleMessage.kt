package pe.pjh.gendia.diagram.sequence

class TryCatchMultipleMessage(
    override val callee: Participant,
    val tryMessage: CallMessage,
    ) : MultipleBlockMessage(callee) {
    override fun getCode(): String {
        TODO("Not yet implemented")
    }

}