package pe.pjh.gendia.diagram.sequence.participant

data class ActorParticipant(val actorName: String) : BaseParticipant(actorName, null) {
    override fun getMessage(): String {
        return "actor  $name"
    }
}
