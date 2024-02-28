package pe.pjh.gendia.diagram

import com.intellij.psi.PsiJavaFile
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import junit.framework.TestCase
import org.junit.Test
import pe.pjh.gendia.diagram.sequence.SequenceDiagramParam
import pe.pjh.gendia.diagram.sequence.SequenceDiagramParser


//
class SequenceDiagramParserLoopTest : SeqBasePlatform() {

    /**
     * 루프 구문 테스트
     */
    fun testGenerateByLoop() {

        val sd = fileAndParamInit(
            "sequence/SimpleLoop.java",
            "sequence.SimpleLoop.testRun1,sequence.SimpleLoop.testRun2"
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
            "sequence/SimpleInBlock.java",
            "sequence.SimpleInBlock.testRun1"
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