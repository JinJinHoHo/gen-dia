package pe.pjh.gendia.diagram

import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import pe.pjh.gendia.diagram.sequence.SequenceDiagramConfig
import pe.pjh.gendia.diagram.sequence.SequenceDiagramParser

object DiagramParamFactory {
    fun dia(project: Project, diagramGenConfig: DiagramGenConfig): DiagramParsingProcess {
        val diagramParsingProcess: DiagramParsingProcess = when (diagramGenConfig.diagramType) {

            DiagramType.ClassDiagram -> ClassDiagramParser(project, diagramGenConfig)

            DiagramType.SequenceDiagram -> SequenceDiagramParser(project, diagramGenConfig as SequenceDiagramConfig)
        }

        if (DumbService.isDumb(project)) {

        }

        diagramParsingProcess.collection()

        return diagramParsingProcess
        // 캐싱이 완료되었을 때 실행되는 코드를 여기에 작성합니다.
    }
}