package pe.pjh.gendia.diagram

import com.intellij.psi.PsiJavaFile
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import junit.framework.TestCase
import org.junit.Test
import pe.pjh.gendia.diagram.sequence.SequenceDiagramParam
import pe.pjh.gendia.diagram.sequence.SequenceDiagramParser


//
open class SeqBasePlatform : BasePlatformTestCase() {

    fun fileAndParamInit(file: String, startPoint: String): SequenceDiagramParser {
        myFixture.configureByFiles(file)

        return SequenceDiagramParser(
            project,
            DiagramGenInfo(
                UMLType.mermaid,
                DiagramType.sequenceDiagram,
                SequenceDiagramParam(startPoint)
            )
        )
    }


    override fun getTestDataPath(): String {
        return "src/test/testData"
    }
}