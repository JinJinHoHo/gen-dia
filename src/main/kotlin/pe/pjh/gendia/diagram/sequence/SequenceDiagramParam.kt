package pe.pjh.gendia.diagram.sequence

class SequenceDiagramParam(startPoint: String) {

    var startPoint: String = ""
    var autonumber: Boolean = true
    var web: Boolean = false
    var actorName: String = "User"


    init {
        this.startPoint = startPoint
    }
}