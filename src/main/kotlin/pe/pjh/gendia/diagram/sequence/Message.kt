package pe.pjh.gendia.diagram.sequence

interface Message {
    fun getCode(depth:Int):String
}