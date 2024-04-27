package pe.pjh.gendia.diagram.sequence

enum class MessageArrowType(val expression: String) {

    SolidLineWithoutArrow("->"),
    DottedLineWithoutArrow("-->"),

    SolidLineWithArrowhead("->>"),
    DottedLineWithArrowhead("-->>"),

    SolidLineWithACrossAtTheEnd("-x"),
    DottedLineWithACrossAtTheEnd("--x"),

    SolidLineWithAnOpenArrowAtTheEnd("-)"),
    DottedLineWithAnOpenArrowAtTheEnd("--)");

}