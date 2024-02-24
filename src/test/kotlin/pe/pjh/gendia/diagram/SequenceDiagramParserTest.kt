package pe.pjh.gendia.diagram

import com.intellij.psi.PsiJavaFile
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import junit.framework.TestCase
import pe.pjh.gendia.diagram.sequence.SequenceDiagramParam
import pe.pjh.gendia.diagram.sequence.SequenceDiagramParser


//
class SequenceDiagramParserTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String {
        return "src/test/testData"
    }

    fun testCollection() {

        myFixture.configureByFiles("sequence/SimpleNoReturn.java")

        val sd = SequenceDiagramParser(
            project,
            DiagramGenInfo(
                UMLType.mermaid,
                DiagramType.sequenceDiagram,
                SequenceDiagramParam("sequence.SimpleNoReturn.testRun")
            )
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

        myFixture.configureByFiles("sequence/SimpleNoReturn.java")

        val sd = SequenceDiagramParser(
            project,
            DiagramGenInfo(
                UMLType.mermaid,
                DiagramType.sequenceDiagram,
                SequenceDiagramParam("sequence.SimpleNoReturn.testRun")
            )
        )

        sd.collection();

        sd.analysis();
    }

    fun testGenerate1() {

        myFixture.configureByFiles("sequence/SimpleNoReturn.java")

        val sd = SequenceDiagramParser(
            project,
            DiagramGenInfo(
                UMLType.mermaid,
                DiagramType.sequenceDiagram,
                SequenceDiagramParam("sequence.SimpleNoReturn.testRun")
            )
        )

        sd.collection();

        sd.analysis();

        val code = sd.generate();
        println(code)
        TestCase.assertEquals(
            """
            autonumber
            actor User
            participant User
            participant sequence.SimpleNoReturn
            User->>sequence.SimpleNoReturn:testRun
            """.trimIndent(),
            code.trimIndent()
        )
    }


    fun testGenerate2() {

        myFixture.configureByFiles("sequence/SimpleReturn.java")

        val sd = SequenceDiagramParser(
            project,
            DiagramGenInfo(
                UMLType.mermaid,
                DiagramType.sequenceDiagram,
                SequenceDiagramParam("sequence.SimpleReturn.testRun")
            )
        )

        sd.collection();

        sd.analysis();

        val code = sd.generate();
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

        myFixture.configureByFiles("sequence/SimpleLoop.java")

        val sd = SequenceDiagramParser(
            project,
            DiagramGenInfo(
                UMLType.mermaid,
                DiagramType.sequenceDiagram,
                SequenceDiagramParam("sequence.SimpleLoop.testRun1,sequence.SimpleLoop.testRun2")
            )
        )

        sd.collection();

        sd.analysis();

        val code = sd.generate();
        println(code)

        val expected ="""
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
	sequence.SimpleLoop->>sequence.SimpleLoop:서브 메소드 콜:newMethod
	sequence.SimpleLoop-->>User:String
            """

        TestCase.assertEquals(
            expected.replace(Regex("([\\t\\s]+)"),"").trimIndent(),
            code.replace(Regex("([\\t\\s]+)"),"").trimIndent()
        )
    }


    /**
     * 루프 구문 테스트
     */
    fun testGenerateByInBlock() {

        myFixture.configureByFiles("sequence/SimpleInBlock.java")

        val sd = SequenceDiagramParser(
            project,
            DiagramGenInfo(
                UMLType.mermaid,
                DiagramType.sequenceDiagram,
                SequenceDiagramParam("sequence.SimpleInBlock.testRun1")
            )
        )

        sd.collection();

        sd.analysis();

        val code = sd.generate();
        println(code)

        val expected ="""
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
            expected.replace(Regex("([\\t\\s]+)"),"").trimIndent(),
            code.replace(Regex("([\\t\\s]+)"),"").trimIndent()
        )
    }
}