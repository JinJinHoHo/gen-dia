package pe.pjh.gendia.diagram.sequence

data class ActorParticipant(val actorName: String) : Participant(actorName){
    override fun getMessage(): String {
        return "actor  $name"
    }
}
