package pe.pjh.gendia.diagram.sequence.participant

open class BaseParticipant(open val name: String, open val alias: String?) {
    open fun getMessage(): String {
        var code = "participant $name"
        if (alias != null) {
            code += " as $alias"
        }
        return code
    }
}
