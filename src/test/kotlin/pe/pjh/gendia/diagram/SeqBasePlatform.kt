package pe.pjh.gendia.diagram

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import pe.pjh.gendia.diagram.sequence.SequenceDiagramConfig
import pe.pjh.gendia.diagram.sequence.SequenceDiagramParser


//
open class SeqBasePlatform : BasePlatformTestCase() {

    fun fileAndParamInit(
        startPoint: String,
        vararg cacheFiles: String,
    ): SequenceDiagramParser {

        return fileAndParamInit(
            SequenceDiagramConfig(
                UMLType.Mermaid,
                DiagramType.SequenceDiagram,
                startPoint,
            ), *cacheFiles
        )
    }

    fun fileAndParamInit(
        sequenceDiagramConfig: SequenceDiagramConfig,
        vararg cacheFiles: String,
    ): SequenceDiagramParser {

//        PsiManager.getInstance(myFixture.project).dropPsiCaches()

        myFixture.configureByFiles(*cacheFiles)

        return SequenceDiagramParser(
            project,
            sequenceDiagramConfig
        )
    }

    override fun getTestDataPath(): String {
        return "src/test/java"
    }
}