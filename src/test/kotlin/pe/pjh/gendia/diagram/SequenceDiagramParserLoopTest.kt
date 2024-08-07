package pe.pjh.gendia.diagram

import junit.framework.TestCase


//
class SequenceDiagramParserLoopTest : SeqBasePlatform() {

    /**
     * 루프 구문 테스트
     */
    fun testGenerateByLoop() {

        val sd = fileAndParamInit(
            "testData.sequence.SimpleLoop.testRun1,sequence.SimpleLoop.testRun2",
            "testData/sequence/SimpleLoop.java"
        )
        sd.collection()
        sd.analysis()

        val code = sd.generate()
        println(code)

        val expected = """
    autonumber

	actor User

	participant User
	participant sequence.SimpleLoop

	User->>sequence.SimpleLoop:testRun1
	sequence.SimpleLoop-->>User:String
	User->>sequence.SimpleLoop:testRun2
	loop 루프 테스트
		sequence.SimpleLoop->>sequence.SimpleLoop:서브 메소드 콜:newMethod
	end
	sequence.SimpleLoop-->>User:String
            """

        TestCase.assertEquals(
            expected.replace(Regex("([\\t\\s]+)"), "").trimIndent(),
            code.replace(Regex("([\\t\\s]+)"), "").trimIndent()
        )
    }


    /**
     * 루프 구문 테스트
     */
    fun testGenerateByInBlock() {

        val sd = fileAndParamInit(
            "testData.sequence.SimpleInBlock.testRun1",
            "testData/sequence/SimpleInBlock.java"

        )
        sd.collection()
        sd.analysis()

        val code = sd.generate()
        println(code)

        val expected = """
	autonumber

	actor User

	participant User
	participant sequence.SimpleInBlock

	User->>sequence.SimpleInBlock:testRun1
	sequence.SimpleInBlock->>sequence.SimpleInBlock:서브 메소드 콜:newMethod
	sequence.SimpleInBlock->>sequence.SimpleInBlock:서브 메소드 콜:newMethod
	sequence.SimpleInBlock-->>User:String
            """

        TestCase.assertEquals(
            expected.replace(Regex("([\\t\\s]+)"), "").trimIndent(),
            code.replace(Regex("([\\t\\s]+)"), "").trimIndent()
        )
    }

}