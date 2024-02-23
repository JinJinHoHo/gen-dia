package pe.pjh.gendia.diagram

import pe.pjh.gendia.diagram.sequence.SequenceDiagramParam

data class DiagramGenInfo(
    val umlType: UMLType,
    val diagramType: DiagramType,
    val diagramParam: SequenceDiagramParam
)
