package pe.pjh.gendia.diagram.sequence

enum class ConditionalMarkType(val mark1: String,val mark2: String,val mark3: String) {
    Alt("alt", "else", "end"),
    Par("par", "and", "end"),
    Critical("critical", "option", "end");


}