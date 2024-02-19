package pe.pjh.gendia.diagram

import com.intellij.openapi.project.Project

object DiagramParamFactory {
    fun dia(project: Project, diagramGenInfo: DiagramGenInfo): DiagramParsingProcess {
        val diagramParsingProcess: DiagramParsingProcess = when (diagramGenInfo.diagramType) {

            DiagramType.classDiagram -> ClassDiagramParser(project, diagramGenInfo)

            DiagramType.sequenceDiagram -> SequenceDiagramParser(project, diagramGenInfo)
        }

        diagramParsingProcess.collection()

        return diagramParsingProcess;
    }
}