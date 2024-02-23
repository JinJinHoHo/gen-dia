package pe.pjh.gendia.diagram

object TabUtil {
    fun textLine(depthCount: Int, t: String): String {
        var text = ""
        repeat(depthCount) { text += "\t" }
        return "$text$t\n"
    }
}