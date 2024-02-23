package pe.pjh.gendia.diagram

interface DiagramParsingProcess {
    //    파일 수집
    fun collection()

    fun analysis()

    fun generate(): String

//    파일 분석
//    문법 생성
}