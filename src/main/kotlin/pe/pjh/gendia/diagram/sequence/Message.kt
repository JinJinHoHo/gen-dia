package pe.pjh.gendia.diagram.sequence

import pe.pjh.gendia.diagram.TabUtil

interface Message {

    fun getCode(): String

    fun getCodeLine(depth: Int): String {
        return TabUtil.textLine(depth, getCode())
    }

    override fun toString(): String

}