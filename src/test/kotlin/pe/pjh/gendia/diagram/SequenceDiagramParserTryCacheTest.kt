package pe.pjh.gendia.diagram

import junit.framework.TestCase


//
class SequenceDiagramParserTryCacheTest : SeqBasePlatform() {

    /**
     * IfElse 구문 테스트 타입1
     */
    fun testGenerateByTryCache1() {


        val sd = fileAndParamInit(
            "testData/sequence/TryCacheConditionalTestSample.java",
            "testData.sequence.TryCacheConditionalTestSample.testRun1"
        )
        sd.collection();
        sd.analysis();

        val code = sd.generate();
        println(code)

        val expected = """
            autonumber
        
            actor User
        
            participant User
            participant sequence.ConditionalTestSample
        
            User->>sequence.ConditionalTestSample:testRun1
            alt 분기 처리 타입1
                sequence.ConditionalTestSample->>sequence.ConditionalTestSample:서브 메소드 콜:newMethod
            end
            """

        TestCase.assertEquals(
            expected.replace(Regex("([\\t\\s]+)"), "").trimIndent(),
            code.replace(Regex("([\\t\\s]+)"), "").trimIndent()
        )
    }

}