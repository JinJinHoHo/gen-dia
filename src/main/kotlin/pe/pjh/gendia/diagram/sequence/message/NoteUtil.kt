package pe.pjh.gendia.diagram.sequence.message

import pe.pjh.gendia.diagram.sequence.participant.BaseParticipant

object NoteUtil {
    fun note(position: Position, caller: BaseParticipant, message: String): String {
        return "Note ${position.notation} ${caller.name}: $message"
    }

    enum class Position(var notation: String) {
        right("right of"), left("right of"), over("over");
    }
}

