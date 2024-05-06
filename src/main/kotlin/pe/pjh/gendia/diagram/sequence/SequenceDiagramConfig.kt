package pe.pjh.gendia.diagram.sequence

import pe.pjh.gendia.diagram.DiagramGenConfig
import pe.pjh.gendia.diagram.DiagramType
import pe.pjh.gendia.diagram.UMLType

data class SequenceDiagramConfig(
    val startPoint: String,
    override val umlType: UMLType,
    override val diagramType: DiagramType,
) : DiagramGenConfig(umlType, diagramType) {

    var autonumber: Boolean = true
    var web: Boolean = false
    var actorName: String = "User"
    var messageLabelType: MessageLabelType = MessageLabelType.METHOD_COMMENT
}