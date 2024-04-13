package pe.pjh.gendia.diagram

import com.intellij.psi.PsiJavaFile
import junit.framework.TestCase


//
class SequenceDiagramParserTest : SeqBasePlatform() {


    fun testCollection() {

        val sd = fileAndParamInit(
            "testData/sequence/SimpleBase.java",
            "testData.sequence.SimpleNoReturn.testRun"
        )

        sd.collection();
        sd.startPointPsiMethods.forEach {
            val clz = it.parent

            val packageName: String = (clz.getContainingFile() as PsiJavaFile).getPackageName()
            println(clz)
            println(packageName)
            println(it.toString())
        }
        TestCase.assertEquals(1, sd.startPointPsiMethods.size)

    }

    fun testAnalysis() {

        val sd = fileAndParamInit(
            "testData/sequence/SimpleBase.java",
            "testData.sequence.SimpleNoReturn.testRun"
        )

        sd.collection()
        sd.analysis()
    }

    fun testGenerate1() {


        val sd = fileAndParamInit(
            "testData/sequence/SimpleBase.java",
            "testData.sequence.SimpleNoReturn.testRun"
        )

        sd.collection()
        sd.analysis()

        val code = sd.generate()
        println(code)
        TestCase.assertEquals(
            """
            autonumber
            actor User
            participant User
            participant sequence.SimpleNoReturn
            User->>sequence.SimpleNoReturn:testRun
            """.replace(Regex("([\\t\\s]+)"), ""),
            code.replace(Regex("([\\t\\s]+)"), "")
        )
    }


    fun testGenerate2() {
        val sd = fileAndParamInit(
            "testData/sequence/SimpleReturn.java",
            "testData.sequence.SimpleReturn.testRun"
        )

        sd.collection()
        sd.analysis()

        val code = sd.generate()
        println(code)
        TestCase.assertEquals(
            """
	autonumber

	actor User

	participant User
	participant sequence.SimpleReturn

	User->>sequence.SimpleReturn:testRun
	sequence.SimpleReturn-->>User:String
            """.trimIndent(),
            code.trimIndent()
        )
    }

    /**
     * 루프 구문 테스트
     */
    fun testGenerateByLoop() {

        val sd = fileAndParamInit(
            "testData/sequence/SimpleLoop.java",
            "testData.sequence.SimpleLoop.testRun1,sequence.SimpleLoop.testRun2"
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
            "testData/sequence/SimpleInBlock.java",
            "testData.sequence.SimpleInBlock.testRun1"
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