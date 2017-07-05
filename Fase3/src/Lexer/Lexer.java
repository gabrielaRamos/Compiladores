package Lexer;

import java.util.*;

//import AuxComp.*;
public class Lexer {

    public Lexer(char[] input, CompilerError error) {
        this.input = input;
// add an end-of-file label to make it easy to do the lexer
        input[input.length - 1] = '\0';
// number of the current line
        lineNumber = 1;
        tokenPos = 0;
        lastTokenPos = 0;
        beforeLastTokenPos = 0;
        this.error = error;
    }

    public void skipBraces() {
// skip any of the symbols [ ] { } ( )
        if (token == Symbol.CURLYLEFTBRACE || token == Symbol.CURLYRIGHTBRACE
                || token == Symbol.LEFTSQBRACKET || token == Symbol.RIGHTSQBRACKET) {
            nextToken();
        }
        if (token == Symbol.EOF) {
            error.signal("Unexpected EOF");
        }
    }

    public void skipPunctuation() {
// skip any punctuation symbols
        while (token != Symbol.EOF
                && (token == Symbol.COLON
                || token == Symbol.COMMA
                || token == Symbol.SEMICOLON)) {
            nextToken();
        }
        if (token == Symbol.EOF) {
            error.signal("Unexpected EOF");
        }
    }

    public void skipTo(Symbol[] arraySymbol) {
// skip till one of the characters of arraySymbol appears in the input
        while (token != Symbol.EOF) {
            int i = 0;
            while (i < arraySymbol.length) {
                if (token == arraySymbol[i]) {
                    return;
                } else {
                    i++;
                }
            }
            nextToken();
        }
        if (token == Symbol.EOF) {
            error.signal("Unexpected EOF");
        }
    }

    public void skipToNextStatement() {
        while (token != Symbol.EOF
                && token != Symbol.ELSE && token != Symbol.ENDIF
                && token != Symbol.END && token != Symbol.SEMICOLON) {
            nextToken();
        }
        if (token == Symbol.SEMICOLON) {
            nextToken();
        }
    }
// contains the keywords
    static private Hashtable<String, Symbol> keywordsTable;
// this code will be executed only once for each program execution

    static {
        keywordsTable = new Hashtable<String, Symbol>();
        keywordsTable.put("end", Symbol.END);
        keywordsTable.put("if", Symbol.IF);
        keywordsTable.put("then", Symbol.THEN);
        keywordsTable.put("else", Symbol.ELSE);
        keywordsTable.put("endif", Symbol.ENDIF);
        keywordsTable.put("boolean", Symbol.BOOLEAN);
        keywordsTable.put("char", Symbol.CHAR);
        keywordsTable.put("void", Symbol.VOID);
        keywordsTable.put("True", Symbol.TRUE);
        keywordsTable.put("False", Symbol.FALSE);
        keywordsTable.put("and", Symbol.AND);
        keywordsTable.put("or", Symbol.OR);
        keywordsTable.put("not", Symbol.NOT);
        keywordsTable.put("for", Symbol.FOR);
        keywordsTable.put("while", Symbol.WHILE);
        keywordsTable.put("return", Symbol.RETURN);
        keywordsTable.put("break", Symbol.BREAK);
        keywordsTable.put("def", Symbol.DEF);
        keywordsTable.put("float", Symbol.FLOAT);
        keywordsTable.put("inrange", Symbol.INRANGE);
        keywordsTable.put("int", Symbol.INT);
        keywordsTable.put("print", Symbol.PRINT);
        keywordsTable.put("program", Symbol.PROGRAM);
        keywordsTable.put("string", Symbol.STRING);
    }

    public void nextToken() {
        stringValue = null;
        numberValueInt = MaxValueInteger - 1;
        numberValueFloat = MaxValueInteger - 1;
        charValue = '\0';
        char ch;
        int flag = 0;
        while ((ch = input[tokenPos]) == ' ' || ch == '\r' || ch == '\t' || ch == '\n') {

            // count the number of lines
            if (ch == '\n') {
                lineNumber++;
            }
            tokenPos++;
        }
        if (ch == '\0') {
            token = Symbol.EOF;
        } else if (input[tokenPos] == '#') {
// comment found
            while (input[tokenPos] != '\0' && input[tokenPos] != '\n') {
                tokenPos++;
            }
            nextToken();
        } else {
            if (Character.isLetter(ch)) {
// get an identifier or keyword
                StringBuffer ident = new StringBuffer();
                while (Character.isLetter(input[tokenPos])
                        || Character.isDigit(input[tokenPos])) {
                    ident.append(input[tokenPos]);
                    tokenPos++;
                    if (input[tokenPos] == '-' || input[tokenPos] == '@' || input[tokenPos] == '_'
                            || input[tokenPos] == '$' || input[tokenPos] == '#' || input[tokenPos] == '+') {
                        error.signal("Character " + input[tokenPos] + " invalid");
                    }
                }
                stringValue = ident.toString();
// if identStr is in the list of keywords, it is a keyword !
                Symbol value = keywordsTable.get(stringValue);
                if (value == null) {
                    token = Symbol.IDENT;
                } else {
                    token = value;
                }
            } else if (Character.isDigit(ch)) {
// get a number
                StringBuffer number = new StringBuffer();
                //para caso o numero seja float
                while ((Character.isDigit(input[tokenPos]) || input[tokenPos] == '.') && flag <= 1) {
                    if (input[tokenPos] == '.') {
                        flag++;
                    }
                    number.append(input[tokenPos]);

                    tokenPos++;

                }
                if (flag > 1) {
                    error.signal("Number followed by two points");
                }
                if (Character.isLetter(input[tokenPos])) {
                    error.signal("Number followed by a letter");
                } else {
                    token = Symbol.NUMBER;
                    this.flaFloat = flag;
                    try {
                        //numero é um float
                        if (flag == 1) {

                            numberValueFloat = Float.valueOf(number.toString());
                        } else {
                            numberValueInt = Integer.valueOf(number.toString()).intValue();
                        }
                    } catch (NumberFormatException e) {
                        error.signal("Number out of limits");
                    }

                    if (numberValueInt >= MaxValueInteger) {
                        error.signal("Number out of limits");
                    }
                }
            } else {
                tokenPos++;
                switch (ch) {
                    case '+':
                        token = Symbol.PLUS;
                        break;
                    case '-':
                        token = Symbol.MINUS;
                        break;
                    case '*':
                        token = Symbol.MULT;
                        break;
                    case '/':
                        token = Symbol.DIV;
                        break;
                    case '%':
                        token = Symbol.REMAINDER;
                        break;
                    case '<':
                        if (input[tokenPos]
                                == '=') {
                            tokenPos++;
                            token = Symbol.LE;
                        } else if (input[tokenPos]
                                == '>') {
                            tokenPos++;
                            token = Symbol.NEQ;
                        } else {
                            token = Symbol.LT;
                        }
                        break;
                    case '>':
                        if (input[tokenPos]
                                == '=') {
                            tokenPos++;
                            token = Symbol.GE;
                        } else {
                            token = Symbol.GT;
                        }
                        break;
                    case '=':
                        if (input[tokenPos]
                                == '=') {
                            tokenPos++;
                            token = Symbol.EQ;
                        } else {
                            token = Symbol.ASSIGN;
                        }
                        break;
                    case '(':
                        token = Symbol.LEFTPAR;
                        break;
                    case ')':
                        token = Symbol.RIGHTPAR;
                        break;
                    case ',':
                        token = Symbol.COMMA;
                        break;
                    case ';':
                        token = Symbol.SEMICOLON;
                        break;
                    case ':':
                        token = Symbol.COLON;
                        break;
                    case '\'':
                        if (input[tokenPos + 1] == '\'') {
                            token = Symbol.CHARACTER;
                            charValue = input[tokenPos];
                            tokenPos++;
                            if (input[tokenPos] != '\'') {
                                error.signal("Illegal literal character" + input[tokenPos - 1]);
                            }
                        } else {

                            token = Symbol.STRING;
                            StringBuffer str = new StringBuffer();
                            while (input[tokenPos] != '\'' && input[tokenPos] != '\0') {
                                str.append(input[tokenPos]);

                                tokenPos++;
                            }
                            stringValue = str.toString();
                            if (input[tokenPos] != '\'') {
                                error.signal("Illegal literal character" + input[tokenPos - 1]);
                            }

                        }

                        tokenPos++;
                        break;
// the next four symbols are not used by the language
// but are returned to help the error treatment
                    case '{':
                        token = Symbol.CURLYLEFTBRACE;
                        break;

                    case '}':
                        token = Symbol.CURLYRIGHTBRACE;
                        break;

                    case '[':
                        token = Symbol.LEFTSQBRACKET;
                        break;
                    case ']':
                        token = Symbol.RIGHTSQBRACKET;
                        break;
                    case '^':
                        token = Symbol.POW;
                        break;
                    default:
                        error.signal("Invalid Character: ’" + ch + "’");
                }
            }
        }
        System.out.println(token);
        beforeLastTokenPos = lastTokenPos;
        lastTokenPos = tokenPos - 1;
    }// return the line number of the last token got with getToken()

    public int getLineNumber() {
        return lineNumber;
    }

    public int getLineNumberBeforeLastToken() {
        return getLineNumber(beforeLastTokenPos);
    }

    private int getLineNumber(int index) {
// return the line number in which the character input[index] is
        int i, n, size;
        n = 1;
        i = 0;
        size = input.length;
        while (i < size && i < index) {
            if (input[i] == '\n') {
                n++;
            }
            i++;
        }
        return n;
    }

    public String getCurrentLine() {
        return getLine(lastTokenPos);
    }

    public String getLineBeforeLastToken() {
        return getLine(beforeLastTokenPos);
    }

    private String getLine(int index) {
// get the line that contains input[index]. Assume input[index] is at a token, not
// a white space or newline
        int i = index;
        if (i == 0) {
            i = 1;
        } else if (i >= input.length) {
            i = input.length;
        }
        StringBuffer line = new StringBuffer();
// go to the beginning of the line
        while (i >= 1 && input[i] != '\n') {
            i--;
        }
        if (input[i] == '\n') {
            i++;
        }
// go to the end of the line putting it in variable line
        while (input[i] != '\0' && input[i] != '\n' && input[i] != '\r') {
            line.append(input[i]);
            i++;
        }
        return line.toString();
    }

    public String getStringValue() {
        return stringValue;
    }

    public int isIntOrFloat() {
        return this.flaFloat;
    }

    public int getNumberValueInt() {
        return numberValueInt;
    }

    public float getNumberValueFloat() {
        return numberValueFloat;
    }

    public char getCharValue() {
        return charValue;
    }
// current token
    public Symbol token;
    private String stringValue;
    private int numberValueInt;
    private float numberValueFloat;
    private char charValue;
    private int tokenPos;
// input[lastTokenPos] is the last character of the last token found
    private int lastTokenPos;
// input[beforeLastTokenPos] is the last character of the token before the last
// token found
    private int beforeLastTokenPos;
// program given as input - source code
    private char[] input;
// number of current line. Starts with 1
    private int lineNumber;
    private int flaFloat;
    private CompilerError error;
    public static final int MaxValueInteger = 32768;
}
