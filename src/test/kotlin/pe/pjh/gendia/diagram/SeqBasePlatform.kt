package pe.pjh.gendia.diagram

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import pe.pjh.gendia.diagram.sequence.SequenceDiagramParam
import pe.pjh.gendia.diagram.sequence.SequenceDiagramParser


//
open class SeqBasePlatform : BasePlatformTestCase() {

    fun fileAndParamInit(file: String, startPoint: String): SequenceDiagramParser {
        myFixture.configureByFiles(file)

        return SequenceDiagramParser(
            project,
            DiagramGenInfo(
                UMLType.Mermaid,
                DiagramType.SequenceDiagram,
                SequenceDiagramParam(startPoint)
            )
        )
    }


    override fun getTestDataPath(): String {
        return "src/test/java"
    }
}