package pe.pjh.gendia.diagram

import junit.framework.TestCase
import org.junit.Test


class SequenceDiagramParamTest {

    @Test
//    @DisplayName("파라미터 파싱 테스트")
    fun test() {
        val aa = SequenceDiagramParam("pe.pjh.ws.application.service.AppInitializer.startUp")

        TestCase.assertEquals(
            "pe.pjh.ws.application.service.AppInitializer.startUp", aa.startPoint
        )
    }
}
