package pe.pjh.gendia.diagram


import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import pe.pjh.gendia.diagram.SequenceDiagramParam

class SequenceDiagramParamTest{

    @Test
    @DisplayName("파라미터 파싱 테스트")
    fun test() {
        val aa = SequenceDiagramParam(arrayListOf("startPoint:pe.pjh.ws.application.service.AppInitializer.startUp"));
        Assertions.assertEquals("pe.pjh.ws.application.service.AppInitializer.startUp", aa.startPoint)
    }
}
