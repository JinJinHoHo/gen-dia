package pe.pjh.gendia.diagram

import com.intellij.openapi.project.Project
import pe.pjh.gendia.diagram.sequence.SequenceDiagramParser

object DiagramParamFactory {
    fun dia(project: Project, diagramGenInfo: DiagramGenInfo): DiagramParsingProcess {
        val diagramParsingProcess: DiagramParsingProcess = when (diagramGenInfo.diagramType) {

            DiagramType.ClassDiagram -> ClassDiagramParser(project, diagramGenInfo)

            DiagramType.SequenceDiagram -> SequenceDiagramParser(project, diagramGenInfo)
        }

        diagramParsingProcess.collection()

        return diagramParsingProcess
    }
}