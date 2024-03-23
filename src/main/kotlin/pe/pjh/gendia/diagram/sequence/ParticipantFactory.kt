package pe.pjh.gendia.diagram.sequence

object ParticipantFactory {

    val participantMap = mutableMapOf<String, Participant>()

    fun getParticipant(name: String): Participant {
        return participantMap.getOrPut(name, defaultValue = {
            Participant(name)
        })
    }
}