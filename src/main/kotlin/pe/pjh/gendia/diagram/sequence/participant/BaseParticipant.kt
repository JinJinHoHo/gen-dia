package pe.pjh.gendia.diagram.sequence.participant

open class BaseParticipant(open val name: String) {
    open fun getMessage(): String {
        return "participant $name"
    }
}
