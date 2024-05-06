package pe.pjh.gendia.diagram.sequence.participant

data class ActorParticipant(val actorName: String) : BaseParticipant(actorName){
    override fun getMessage(): String {
        return "actor  $name"
    }
}
