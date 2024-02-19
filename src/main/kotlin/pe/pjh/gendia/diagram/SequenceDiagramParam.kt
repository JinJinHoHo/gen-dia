package pe.pjh.gendia.diagram

class SequenceDiagramParam(paramStringDatas: ArrayList<String>) {

    var startPoint: String = ""

    init {
        paramStringDatas.forEach {
            val keyValue = it.split(":");
            if ("startpoint".equals(keyValue[0].toLowerCase())) {
                startPoint = keyValue[1].trim()
            }
        }
    }

}