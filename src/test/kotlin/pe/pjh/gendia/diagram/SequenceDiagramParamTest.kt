package pe.pjh.gendia.diagram

import junit.framework.TestCase
import pe.pjh.gendia.diagram.sequence.SequenceDiagramParam


class SequenceDiagramParamTest {

//    @DisplayName("파라미터 파싱 테스트")
    fun testParam() {
        val aa = SequenceDiagramParam("pe.pjh.ws.application.service.AppInitializer.startUp")

        TestCase.assertEquals(
            "pe.pjh.ws.application.service.AppInitializer.startUp", aa.startPoint
        )
    }
}
