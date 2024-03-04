package pe.pjh.gendia.diagram.sequence

import pe.pjh.gendia.diagram.TabUtil
import pe.pjh.gendia.diagram.UMLType

interface Message {

    fun getCode(): String

    fun getCodeLine(depth: Int): String {
        return TabUtil.textLine(depth, getCode())
    }

}