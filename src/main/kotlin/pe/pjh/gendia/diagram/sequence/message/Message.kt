package pe.pjh.gendia.diagram.sequence.message

import pe.pjh.gendia.diagram.TabUtil
import pe.pjh.gendia.diagram.sequence.SequenceDiagramConfig

interface Message {

    fun getCode(config: SequenceDiagramConfig): String

    fun getCodeLine(depth: Int, config: SequenceDiagramConfig): String {
        return TabUtil.textLine(depth, getCode(config))
    }

    override fun toString(): String

}