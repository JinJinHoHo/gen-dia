package pe.pjh.gendia.diagram.sequence

/**
 * 그룹메시지를 n개가 필요 할때 선언. if-else, switch trycatch 등등 사용해서 사용.
 */
open class GroupBlockMessage(open val callee: Participant) : Message {

    val groupMessages = mutableListOf<GroupMessage>()
    override fun getCode(): String {
        TODO("Not yet implemented")
    }

}