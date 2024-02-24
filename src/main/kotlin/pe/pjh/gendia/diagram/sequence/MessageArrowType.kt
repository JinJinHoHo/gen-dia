package pe.pjh.gendia.diagram.sequence

enum class MessageArrowType(expression: String) {

    SolidLineWithoutArrow("->"),
    DottedLineWithoutArrow("-->"),

    SolidLineWithArrowhead("->>"),
    DottedLineWithArrowhead("-->>"),

    SolidLineWithACrossAtTheEnd("-x"),
    DottedLineWithACrossAtTheEnd("--x"),

    SolidLineWithAnOpenArrowAtTheEnd("-)"),
    DottedLineWithAnOpenArrowAtTheEnd("--)");

    val expression = expression
        get() = field
}