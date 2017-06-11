/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
*/
package Lexer;

public enum Symbol {

    EOF("eof"),
    IDENT("Ident"),
    NUMBER("Number"),
    PLUS("+"),
    MINUS("-"),
    MULT("*"),
    DIV("/"),
    LT("<"),
    LE("<="),
    GT(">"),
    GE(">="),
    NEQ("!="),
    EQ("=="),
    ASSIGN("="),
    LEFTPAR("("),
    RIGHTPAR(")"),
    LEFTCOL("["),
    RIGHTCOL("]"),
    LEFTKEY("{"),
    RIGHTKEY("}"),
    POW("^"),
    SEMICOLON(";"),
    POINT("."),
    VAR("var"),
    BEGIN("begin"),
    END("end"),
    IF("if"),
    THEN("then"),
    ELSE("else"),
    ENDIF("endif"),
    COMMA(","),
    READ("read"),
    WRITE("write"),
    COLON(":"),
    BOOLEAN("boolean"),
    CHAR("char"),
    CHARACTER("character"),
    TRUE("true"),
    FALSE("false"),
    OR("||"),
    AND("&&"),
    REMAINDER("%"),
    NOT("!"),
    BREAK("break"),
    ELIF("elif"),
    FLOAT("float"),
    FOR("for"),
    IN("in"),
    INRANGE("inrange"),
    INT("int"),
    NOTIN("notin"),
    STRING("string"),
    WHILE("while"),
    PRINT("print"),
    PROGRAM("program");

    Symbol(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }

    private String name;

}
