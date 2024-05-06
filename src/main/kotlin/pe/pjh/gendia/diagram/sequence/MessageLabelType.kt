package pe.pjh.gendia.diagram.sequence

/**
 * 메시지 라벨 출력 유형.
 */
enum class MessageLabelType {
    /**
     * 주석 노출
     */
    COMMENT,

    /**
     * 메소드명 노출
     */
    METHOD,

    /**
     * 주석 + 메소드명 노출
     */
    METHOD_COMMENT
}
