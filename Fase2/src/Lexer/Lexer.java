/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
*/

package Lexer;

import AST.CompilerError;
import java.util.*;
import AST.*;

public class Lexer {

    public Lexer(char[] input, CompilerError error) {
        this.input = input;
        // add an end-of-file label to make it easy to do the lexer
        input[input.length - 1] = '\0';
        // number of the current line
        lineNumber = 1;
        tokenPos = 0;
        this.error = error;
    }
    //Hash para armazenar palavras reservadas a linguagem
    static public Hashtable<String, Symbol> keywordsTable;

    static {
        keywordsTable = new Hashtable<String, Symbol>();

        keywordsTable.put("and", Symbol.AND);
        keywordsTable.put("boolean", Symbol.BOOLEAN);
        keywordsTable.put("break", Symbol.BREAK);
        keywordsTable.put("elif", Symbol.ELIF);
        keywordsTable.put("else", Symbol.ELSE);
        keywordsTable.put("end", Symbol.END);
        keywordsTable.put("True", Symbol.TRUE);
        keywordsTable.put("False", Symbol.FALSE);
        keywordsTable.put("float", Symbol.FLOAT);
        keywordsTable.put("for", Symbol.FOR);
        keywordsTable.put("if", Symbol.IF);
        keywordsTable.put("in", Symbol.IN);
        keywordsTable.put("inrange", Symbol.INRANGE);
        keywordsTable.put("int", Symbol.INT);
        keywordsTable.put("not", Symbol.NOT);
        keywordsTable.put("notin", Symbol.NOTIN);
        keywordsTable.put("or", Symbol.OR);
        keywordsTable.put("string", Symbol.STRING);
        keywordsTable.put("while", Symbol.WHILE);
        keywordsTable.put("print", Symbol.PRINT);
        keywordsTable.put("program", Symbol.PROGRAM);

    }

    public void nextToken() {
        stringValue = null;
        numberValueInt = MaxValueInteger -1;
        numberValueFloat = MaxValueInteger -1;
        charValue = '\0';
        char ch;
        int flag = 0;
        //andar no vetor input contando numero de linhas
        while ((ch = input[tokenPos]) == ' ' || ch == '\t' || ch == '\n') {
            if (ch == '\n') {
                lineNumber++;
            }
            tokenPos++;
        }

        if (ch == '\0') {
            token = Symbol.EOF;
        } else {
            //ignora comentarios
            if (input[tokenPos] == '#') {
                while (input[tokenPos] != '\0' && input[tokenPos] != '\n') {
                    tokenPos++;
                }
                nextToken();
            } else {
                //caso o char ch seja uma letra, é necessario armazenar tudo em uma String
                if (Character.isLetter(ch)) {
                    StringBuffer ident = new StringBuffer();
                    while (Character.isLetter(input[tokenPos]) || Character.isDigit(input[tokenPos])) {
                        ident.append(input[tokenPos]);
                        tokenPos++;
                        if (input[tokenPos] == '-' || input[tokenPos] == '@' || input[tokenPos] == '_'
                                || input[tokenPos] == '$' || input[tokenPos] == '#' || input[tokenPos] == '+') {
                            error.signal("Character " + input[tokenPos] + " invalid");
                        }
                    }
                    //converte ident para uma variavel do tipo String
                    stringValue = ident.toString();

                    Symbol value = keywordsTable.get(stringValue);
                   
                    if (value == null) {
                        //significa que não se trata de uma palavra reservada
                        token = Symbol.IDENT;
                    } else {

                        token = value;
                    }
                } else if (Character.isDigit(ch)) {
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
                            if (input[tokenPos] == '=') {
                                tokenPos++;
                                token = Symbol.LE;
                            } else if (input[tokenPos] == '>') {
                                tokenPos++;
                                token = Symbol.NEQ;
                            } else {
                                token = Symbol.LT;
                            }
                            break;
                        case '>':
                            if (input[tokenPos] == '=') {
                                tokenPos++;
                                token = Symbol.GE;
                            } else {
                                token = Symbol.GT;
                            }
                            break;
                        case '=':
                            if (input[tokenPos] == '=') {
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
                        case '[':
                            token = Symbol.LEFTCOL;
                            break;
                        case ']':
                            token = Symbol.RIGHTCOL;
                            break;
                        case '{':
                            token = Symbol.LEFTKEY;
                            break;
                        case '}':
                            token = Symbol.RIGHTKEY;
                            break;
                        case '^':
                            token = Symbol.POW;
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
                        case '.':
                            token = Symbol.POINT;
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
                                while (input[tokenPos] != '\'' && input[tokenPos]!='\0') {
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
                        default:
                            error.signal("Invalid Character: ’" + ch + "’");
                    }
                }
            }
            lastTokenPos = tokenPos - 1;
        }
    }

    // return the line number of the last token got with getToken()
    public int getLineNumber() {
        return lineNumber;
    }

    public String getCurrentLine() {
        int i = lastTokenPos;
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
    //  input[lastTokenPos] is the last character of the last token
    private int lastTokenPos;
    // program given as input - source code
    private char[] input;

    // number of current line. Starts with 1
    private int lineNumber;
    private int flaFloat;
    private CompilerError error;
    public static final int MaxValueInteger = 32768;

}
