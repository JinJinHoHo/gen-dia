package pe.pjh.gendia.diagram.sequence.message

import pe.pjh.gendia.diagram.sequence.SequenceDiagramConfig

interface Message {

    fun getCodeLine(depth: Int, config: SequenceDiagramConfig): String

    override fun toString(): String

}