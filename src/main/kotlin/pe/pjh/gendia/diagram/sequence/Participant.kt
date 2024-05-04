package pe.pjh.gendia.diagram.sequence

open class Participant(open val name: String) {
    open fun getMessage(): String {
        return "participant $name"
    }
}
