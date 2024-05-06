package pe.pjh.gendia.diagram

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import pe.pjh.gendia.diagram.sequence.SequenceDiagramConfig
import pe.pjh.gendia.diagram.sequence.SequenceDiagramParser


//
open class SeqBasePlatform : BasePlatformTestCase() {

    fun fileAndParamInit(startPoint: String, vararg files: String): SequenceDiagramParser {
        myFixture.configureByFiles(*files)

        return SequenceDiagramParser(
            project,
            SequenceDiagramConfig(
                startPoint,
                UMLType.Mermaid,
                DiagramType.SequenceDiagram,
            )
        )
    }


    override fun getTestDataPath(): String {
        return "src/test/java"
    }
}