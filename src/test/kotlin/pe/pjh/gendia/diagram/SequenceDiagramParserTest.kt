package pe.pjh.gendia.diagram

import com.intellij.psi.PsiFile
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

        val files: Array<PsiFile> = myFixture.configureByFiles("sequence/SimpleNoReturn.java")

        val sd: SequenceDiagramParser = SequenceDiagramParser(
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

        val files: Array<PsiFile> = myFixture.configureByFiles("sequence/SimpleNoReturn.java")

        val sd: SequenceDiagramParser = SequenceDiagramParser(
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

        val files: Array<PsiFile> = myFixture.configureByFiles("sequence/SimpleNoReturn.java")

        val sd: SequenceDiagramParser = SequenceDiagramParser(
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

        val files: Array<PsiFile> = myFixture.configureByFiles("sequence/SimpleReturn.java")

        val sd: SequenceDiagramParser = SequenceDiagramParser(
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

    fun testGenerate3() {

        val files: Array<PsiFile> = myFixture.configureByFiles("sequence/SimpleLoop.java")

        val sd: SequenceDiagramParser = SequenceDiagramParser(
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
        TestCase.assertEquals(
            """
            autonumber
            actor User
            participant User
            participant sequence.SimpleLoop
            User->>sequence.SimpleLoop:testRun1
            sequence.SimpleLoop-->>User:String
            User->>sequence.SimpleLoop:testRun2
            sequence.SimpleLoop-->>User:String
            """.trimIndent(),
            code.trimIndent()
        )
    }

}