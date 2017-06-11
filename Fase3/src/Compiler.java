/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
 */
import AST.Program;
import AST.*;
import java.util.*;
import Lexer.*;
import java.io.*;

public class Compiler {

    private Hashtable<String, Variable> symbolTable;
    private Lexer lexer;
    public CompilerError error;
    private boolean flagLoop;
    private boolean flagVetorAtomCol = false;

    public Program compile(char[] input, PrintWriter outError, String fileName) {
        symbolTable = new Hashtable<String, Variable>();
        error = new CompilerError(outError, fileName);
        lexer = new Lexer(input, error);
        error.setLexer(lexer);
        lexer.nextToken();
        return program();
    }
    //VERIFICAR SE É NECESSÁRIO USAR O TRY CATCH

    //Program ::= ’program’ Name ’:’ FuncDef {FuncDef} ’end’
    public Program program() {
<<<<<<< Updated upstream
        if (lexer.token == Symbol.PROGRAM) {
            lexer.nextToken();
            name();
            if (lexer.token == Symbol.COLON) {
                lexer.nextToken();
                funcDef();
                while (lexer.token == Symbol.DEF) {
                    funcDef();
                }
                if (lexer.token == Symbol.END) {
                    lexer.nextToken();
                    //Conferir se main foi declarada
                } else {
                    error.signal("end expected");
                }
            } else {
                error.signal(": expected");
            }
        } else {
            error.signal("Program Expected");
        }
=======

>>>>>>> Stashed changes
    }

    //Name ::= Letter{Letter | Digit}
    public Name name() {
        String str = lexer.getStringValue();

        if (str == null) {
            error.signal("Name expected");
        }
        lexer.nextToken();

        return new Name(str);
    }

    public void funcDef() {
        if (lexer.token == Symbol.DEF) {
            lexer.nextToken();
            name();
            if (lexer.token == Symbol.LEFTPAR) {
                lexer.nextToken();
                if (lexer.token == Symbol.BOOLEAN) {
                    argsList();
                }

                if (lexer.token == Symbol.RIGHTPAR) {
                    lexer.nextToken();

                    if (lexer.token == Symbol.COLON) {
                        lexer.nextToken();

                        type();

                        if (lexer.token == Symbol.CURLYLEFTBRACE) {
                            lexer.nextToken();
                            body();
                            if (lexer.token == Symbol.CURLYRIGHTBRACE) {
                                lexer.nextToken();
                            } else {
                                error.signal("} expected");
                            }
                        } else {
                            error.signal("{ expected");
                        }
                    } else {
                        error.signal(": expected");
                    }
                } else {
                    error.signal(") expected");
                }
            } else {
                error.signal("( expected");
            }
        }
    }

    public void argsList() {
        type();
        nameArray();

        while (lexer.token == Symbol.COMMA) {
            lexer.nextToken();
            type();
            nameArray();
        }
    }

    public void nameArray() {
        name();
        if (lexer.token == Symbol.LEFTSQBRACKET) {
            lexer.nextToken();
            numberExpr();
            if (lexer.token == Symbol.RIGHTSQBRACKET) {
                lexer.nextToken();
            } else {
                error.signal("] expected");
            }
        }
    }

    //Signal ::= ’+’ | ’-’
    public String signal() {
        String signal;
        if (lexer.token == Symbol.PLUS) {
            signal = "+";
            lexer.nextToken();
        } else if (lexer.token == Symbol.MINUS) {
            signal = "-";

            lexer.nextToken();
        } else {
            error.signal("+ or - expected");
            signal = null;
        }

        return signal;
    }

    //Body ::= [Declaration] {Stmt}    
    public Body body() {
        ArrayList<Statement> stmt = new ArrayList<Statement>();
        Declaration dec = null;

        if (lexer.token == Symbol.INT || lexer.token == Symbol.FLOAT || lexer.token == Symbol.STRING || lexer.token == Symbol.BOOLEAN || lexer.token == Symbol.IDENT) {
            dec = declaration();
        }

        while (lexer.token == Symbol.IDENT || lexer.token == Symbol.PRINT || lexer.token == Symbol.BREAK
                || lexer.token == Symbol.IF || lexer.token == Symbol.WHILE || lexer.token == Symbol.FOR) {

            stmt.add(stmt());
        }
        return new Body(dec, stmt);
    }

    //Declaration ::= Type IdList ’;’{ Type IdList’;’} 
    public Declaration declaration() {
        ArrayList<Variable> var = new ArrayList<Variable>();
        Type type;
        ArrayList<String> id = new ArrayList<String>();
        String variable;
        String parts[] = new String[2];

        type = type();
        id = idList();

        for (int i = 0; i < id.size(); i++) {
            variable = id.get(i);
            int pos = variable.indexOf("[");

            if (pos == -1) {
                Variable auxVar = new Variable(type, variable, 0);
                if (symbolTable.get(variable) == null) {
                    var.add(auxVar);

                    symbolTable.put(variable, auxVar);
                } else {
                    error.signal("Variable was already declared");
                }
            } else {

                parts = variable.split("\\[");

                String s = parts[1].substring(0, parts[1].length() - 1);
                int limit = Integer.valueOf(s.toString()).intValue();
                Variable auxVar = new Variable(type, parts[0], limit);

                if (symbolTable.get(parts[0]) == null) {
                    var.add(auxVar);
                    symbolTable.put(parts[0], auxVar);
                } else {
                    error.signal("Variable was already declared");
                }
            }

        }

        if (lexer.token == Symbol.SEMICOLON) {

            lexer.nextToken();
        } else {
            error.signal("; expected");
            return null;
        }

        while (lexer.token == Symbol.FLOAT || lexer.token == Symbol.INT || lexer.token == Symbol.CHAR || lexer.token == Symbol.BOOLEAN || lexer.token == Symbol.STRING) {
            type = type();
            id = idList();

            for (int i = 0; i < id.size(); i++) {
                variable = id.get(i);
                int pos = variable.indexOf("[");

                if (pos == -1) {

                    Variable auxVar = new Variable(type, variable, 0);

                    var.add(auxVar);

                    symbolTable.put(variable, auxVar);
                } else {

                    parts = variable.split("\\[");

                    String s = parts[1].substring(0, parts[1].length() - 1);
                    int limit = Integer.valueOf(s.toString()).intValue();
                    Variable auxVar = new Variable(type, parts[0], limit);
                    var.add(auxVar);
                    symbolTable.put(parts[0], auxVar);
                }
            }
            if (lexer.token == Symbol.SEMICOLON) {
                lexer.nextToken();

            } else {
                error.signal("; expected");
                return null;
            }
        }

        return new Declaration(var);
    }

    //Type ::= ’int’ | ’float’ | ’string’ | ’boolean’
    //TYPE VOID  
    public Type type() {
        Type result;

        if (lexer.token == Symbol.INT) {
            result = Type.integerType;
        } else if (lexer.token == Symbol.BOOLEAN) {
            result = Type.booleanType;
        } else if (lexer.token == Symbol.CHAR) {
            result = Type.charType;
        } else if (lexer.token == Symbol.FLOAT) {
            result = Type.floatType;
        } else if (lexer.token == Symbol.STRING) {
            result = Type.stringType;
        } else if (lexer.token == Symbol.VOID) {
            result = Type.voidType;
        } else {
            error.signal("Type expected");
            result = null;
        }
        lexer.nextToken();
        return result;
    }

//IdList ::= NameArray {’,’ NameArray}
    public void idList() {
        nameArray();
        while (lexer.token == Symbol.COMMA) {
            lexer.nextToken();
            nameArray();
        }
    }

    //Stmt ::= SimpleStmt | CompoundStmt
    public Statement stmt() {

        if (lexer.token == Symbol.IDENT || lexer.token == Symbol.PRINT || lexer.token == Symbol.BREAK) {
            return simpleStmt();

        } else if (lexer.token == Symbol.IF || lexer.token == Symbol.ELSE || lexer.token == Symbol.WHILE || lexer.token == Symbol.FOR) {

            return compoundStmt();

        } else {
            error.signal("STATEMENT expected");
            return null;
        }
    }

    //SimpleStmt ::= ExprStmt | PrintStmt | BreakStmt
    //ALTERAR
    public Statement simpleStmt() {
        if (lexer.token == Symbol.IDENT) {
            return exprStmt();

        } else if (lexer.token == Symbol.PRINT) {

            return printStmt();

        } else if (lexer.token == Symbol.BREAK) {
            if (flagLoop == false) {
                error.signal("Statement 'break' just can be used inside loops.");
            }
            return breakStmt();

        } else if (lexer.token == Symbol.RETURN) {
            return returnStmt();
            
        } else if (lexer.token == Symbol.FUNCTION) {
            return funcStmt();
        } else {
            error.signal("SIMPLE STATEMENT expected");
            return null;
        }
    }

   // ExprStmt ::= Name [ ‘[’Atom‘]’ ] ’=’ (OrTest | ’[’OrList’]’) ’;’
    public ExprStmt exprStmt() {

        name();
        if (lexer.token == Symbol.LEFTSQBRACKET) {
            lexer.nextToken();
           atom();

            if (lexer.token == Symbol.RIGHTSQBRACKET) {
                lexer.nextToken();
            } else {
                error.signal("] expected.");
            }
        }
        if (lexer.token == Symbol.ASSIGN) {
            lexer.nextToken();

            if (lexer.token == Symbol.NOT || lexer.token == Symbol.MINUS || lexer.token == Symbol.PLUS || lexer.token == Symbol.IDENT
                    || lexer.token == Symbol.STRING || lexer.token == Symbol.NUMBER || lexer.token == Symbol.TRUE || lexer.token == Symbol.FALSE) {

                orTest();

            } else if (lexer.token == Symbol.LEFTSQBRACKET) {
                lexer.nextToken();
                orList();

                if (lexer.token == Symbol.RIGHTSQBRACKET) {
                    lexer.nextToken();
                } else {
                    error.signal("] expected.");
                }
            } else {
                error.signal("orTest or ExprList exected.");
            }
            if (lexer.token == Symbol.SEMICOLON) {
                lexer.nextToken();

            } else {
                error.signal("; expected");
            }

        } else {
            error.signal("= expected.");
        }

        return null;
    }

    //OrTest ::= AndTest {’or’ AndTest}
    public orTest orTest() {
        ArrayList<AndTest> andT = new ArrayList<AndTest>();
        andT.add(andTest());
        while (lexer.token == Symbol.OR) {
            lexer.nextToken();
            andT.add(andTest());
        }
        return new orTest(andT);
    }

    //AndTest ::= NotTest {’and’ NotTest}
    public AndTest andTest() {
        ArrayList<NotTest> andT = new ArrayList<NotTest>();
        andT.add(notTest());
        while (lexer.token == Symbol.AND) {
            lexer.nextToken();
            andT.add(notTest());
        }
        return new AndTest(andT);
    }

    //NotTest ::= [’not’] Comparison
    public NotTest notTest() {
        String not = null;
        if (lexer.token == Symbol.NOT) {
            not = "not";
            lexer.nextToken();
            Comparison comp = comparison();
            if (comp.getType() == Type.stringType) {
                error.signal("Not is not allowed with strings");
            }
            return new NotTest(not, comp);
        }
        return new NotTest(not, comparison());

    }

    //Comparison ::= Expr [CompOp Expr]
    public Comparison comparison() {
        String op = null;
        Expr expr1, expr2 = null;
        Symbol comp;
        expr1 = expr();
        boolean flagExpr1 = flagVetorAtomCol;
        comp = lexer.token;
        if (comp == Symbol.LT || comp == Symbol.GT || comp == Symbol.EQ || comp == Symbol.GE || comp == Symbol.LE || comp == Symbol.NEQ) {
            op = compOp();
            expr2 = expr();
            boolean flagExpr2 = flagVetorAtomCol;

            if ((expr1.getType() != expr2.getType()) || ((flagExpr1 == true || flagExpr2 == true) && expr1.getType() == expr2.getType())) {
                error.signal("Comparison not allowed");
            }

        }
        return new Comparison(expr1, expr2, op);
    }

    //Expr ::= Term {(’+’ | ’-’) Term}
    public Expr expr() {
        ArrayList<Term> term = new ArrayList<Term>();
        ArrayList<Character> sinal = new ArrayList<Character>();
        term.add(term());
        while (lexer.token == Symbol.MINUS || lexer.token == Symbol.PLUS) {
            if (lexer.token == Symbol.MINUS) {
                sinal.add('-');
            } else if (lexer.token == Symbol.PLUS) {
                sinal.add('+');
            }
            lexer.nextToken();
            term.add(term());
        }
        for (int i = 0; i < term.size(); i++) {
            if (term.get(0).getType().getcName().equals(term.get(i).getType().getcName()) == false) {
                error.signal("Types are different");
            }
        }
        return new CompositeExpr(term, sinal);
    }

    //Term ::= Factor {(’*’ | ’/’) Factor}
    public Term term() {
        ArrayList<Factor> factor = new ArrayList<Factor>();
        ArrayList<Character> sinal = new ArrayList<Character>();

        factor.add(factor());

        while (lexer.token == Symbol.MULT || lexer.token == Symbol.DIV) {
            if (lexer.token == Symbol.MULT) {
                sinal.add('*');
            } else if (lexer.token == Symbol.DIV) {
                sinal.add('/');
            }
            lexer.nextToken();
            factor.add(factor());
        }
        for (int i = 0; i < factor.size(); i++) {
            if (factor.get(0).atom.type.getcName().equals(factor.get(i).atom.type.getcName()) == false) {
                error.signal("Types are different");
            }
        }
        return new Term(factor, sinal);
    }

    //Factor ::= [Signal] AtomExpr {’^’ Factor}
    public Factor factor() {

<<<<<<< Updated upstream
=======
        if (lexer.token == Symbol.PLUS || lexer.token == Symbol.MINUS) {
            lexer.nextToken();
        }

        atomExpr();

        while (lexer.token == Symbol.POW) {
            lexer.nextToken();
            factor();
        }

        return null;
>>>>>>> Stashed changes
    }

    //AtomExpr ::= Atom [Details]
    public Atom atomExpr() {

        atom();

        if (lexer.token == Symbol.LEFTSQBRACKET) {
            details();
        }

        return null;
    }

    //Atom ::= Name | Number | String | ’True’ | ’False’
    public Atom atom() {

<<<<<<< Updated upstream
=======
        if (lexer.token == Symbol.IDENT) {

            name();
        } else if (lexer.token == Symbol.NUMBER) {
            number();
        } else if (lexer.token == Symbol.STRING) {
            string();
        } else if (lexer.token == Symbol.TRUE) {
            //lexer.nextToken();
        } else if (lexer.token == Symbol.FALSE) {
            //lexer.nextToken();
        } else {
            error.signal("NUMBER or STRING or BOOLEAN expected.");
        }

        return null;

    }

    //Details ::= ‘[’(Number | Name)‘]’ | ’(’ [OrList] ’)’
    public Details details() {

        if (lexer.token == Symbol.LEFTSQBRACKET) {

            lexer.nextToken();

            if (lexer.token == Symbol.NUMBER) {
                number();
            } else if (lexer.token == Symbol.IDENT) {
                name();
            } else {
                error.signal("Number or name expected.");
            }

            if (lexer.token == Symbol.RIGHTSQBRACKET) {
                lexer.nextToken();
            } else {
                error.signal("] expected.");
            }
        }
        if (lexer.token == Symbol.LEFTPAR) {
            lexer.nextToken();

            if (lexer.token == Symbol.NOT || lexer.token == Symbol.MINUS || lexer.token == Symbol.PLUS || lexer.token == Symbol.IDENT
                    || lexer.token == Symbol.STRING || lexer.token == Symbol.NUMBER || lexer.token == Symbol.TRUE || lexer.token == Symbol.FALSE) {

                orList();
            }

            if (lexer.token == Symbol.RIGHTPAR) {
                lexer.nextToken();
            } else {
                error.signal(") expected.");
            }
        }

        return null;
>>>>>>> Stashed changes
    }

    //Number ::= [Signal] Digit{Digit} [’.’ Digit{Digit}]
    public numberExpr numberExpr() {
        int flag = 0, valueInt;
        float valueFloat;
        String sinal = null;

        if (lexer.token == Symbol.MINUS || lexer.token == Symbol.PLUS) {
            sinal = signal();

        }
        flag = lexer.isIntOrFloat();
        if (flag == 1) {
            valueFloat = lexer.getNumberValueFloat();
            if (Lexer.MaxValueInteger - 1 == valueFloat) {
                error.signal("Number expected");
            }
            lexer.nextToken();
            return new numberExpr(sinal, valueFloat, flag);
        } else {
            valueInt = lexer.getNumberValueInt();
            if (Lexer.MaxValueInteger - 1 == valueInt) {
                error.signal("Number expected");
            }
            lexer.nextToken();

            return new numberExpr(sinal, valueInt);
        }
    }

    // String ::= ”’ . ”’
    public String string() {
        String str = null;
        char ch;
        if (lexer.token == Symbol.STRING) {
            //
            str = lexer.getStringValue();
            lexer.nextToken();
        } else if (lexer.token == Symbol.CHARACTER) {
            str = lexer.getCharValue() + "";
            lexer.nextToken();
        } else {
            error.signal("' expected");
        }

        return str;
    }

    //CompOp ::= ’<’ | ’>’ | ’==’ | ’>=’ | ’<=’ | ’<>’
    public String compOp() {

        if (lexer.token == Symbol.LT) {
            lexer.nextToken();
            return "<";
        } else if (lexer.token == Symbol.GT) {
            lexer.nextToken();
            return ">";
        } else if (lexer.token == Symbol.EQ) {
            lexer.nextToken();
            return "==";
        } else if (lexer.token == Symbol.GE) {
            lexer.nextToken();
            return ">=";
        } else if (lexer.token == Symbol.LE) {
            lexer.nextToken();
            return "<=";
        } else if (lexer.token == Symbol.NEQ) {
            lexer.nextToken();
            return "!=";
        } else {
            error.signal("> or < or == or >= or <= or <> expected");
        }
        return null;
    }

    //PrintStmt ::= ’print’ OrTest {’,’ OrTest}’;’
    public PrintStmt printStmt() {
        ArrayList<orTest> orT = new ArrayList<orTest>();
        if (lexer.token == Symbol.PRINT) {
            lexer.nextToken();

            orT.add(orTest());

            while (lexer.token == Symbol.COMMA) {
                lexer.nextToken();
                orT.add(orTest());
            }
            if (lexer.token == Symbol.SEMICOLON) {
                lexer.nextToken();

            } else {
                error.signal("; expected");
            }
        } else {
            error.signal("PRINT expected");
        }
        return new PrintStmt(orT);
    }

    //BreakStmt ::= ’break’ ’;’
    public BreakStmt breakStmt() {
        if (lexer.token == Symbol.BREAK) {
            lexer.nextToken();
            if (lexer.token == Symbol.SEMICOLON) {
                lexer.nextToken();
            } else {
                error.signal("; expected");
            }
        } else {
            error.signal("BREAK expected");
        }
        return new BreakStmt();
    }

    //ReturnStmt ::= ’return’ [OrTest]’;’
    public ReturnStmt returnStmt() {
        if (lexer.token == Symbol.RETURN) {
            lexer.nextToken();

            if (lexer.token == Symbol.NOT || lexer.token == Symbol.MINUS || lexer.token == Symbol.PLUS || lexer.token == Symbol.IDENT
                    || lexer.token == Symbol.STRING || lexer.token == Symbol.NUMBER || lexer.token == Symbol.TRUE || lexer.token == Symbol.FALSE) {

                orTest();
            }

            if (lexer.token == Symbol.SEMICOLON) {
                lexer.nextToken();

            } else {
                error.signal("; expected.");
            }

        } else {
            error.signal("'Return' expected.");
        }

        return null;
    }

    //FuncStmt ::= Name’(’ [OrList] ’)”;’
    public FuncStmt funcStmt() {
        name();

        if (lexer.token == Symbol.LEFTPAR) {
            lexer.nextToken();

            if (lexer.token == Symbol.NOT || lexer.token == Symbol.MINUS || lexer.token == Symbol.PLUS || lexer.token == Symbol.IDENT
                    || lexer.token == Symbol.STRING || lexer.token == Symbol.NUMBER || lexer.token == Symbol.TRUE || lexer.token == Symbol.FALSE) {

                orTest();
            }

            if (lexer.token == Symbol.RIGHTPAR) {
                lexer.nextToken();

                if (lexer.token == Symbol.SEMICOLON) {
                    lexer.nextToken();

                } else {
                    error.signal("; expected.");

                }
            } else {
                error.signal(") expected.");
            }

        } else {
            error.signal("( expected.");
        }

        return null;
    }

    //CompoundStmt ::= IfStmt | WhileStmt | ForStmt
    public Statement compoundStmt() {
        if (lexer.token == Symbol.IF || lexer.token == Symbol.ELSE) {
            return ifStmt();
        } else if (lexer.token == Symbol.WHILE) {
            return whileStmt();
        } else if (lexer.token == Symbol.FOR) {
            return forStmt();
        } else {
            error.signal("IF or WHILE or FOR expected");
            return null;
        }
    }

    //IfStmt ::= ’if’ OrTest ’{’ {Stmt} ’}’ [’else’ ’{’ {Stmt} ’}’]
    public Statement ifStmt() {
        ArrayList<Statement> stmt1 = new ArrayList<Statement>();
        orTest orT = null;
        ArrayList<Statement> stmt2 = new ArrayList<Statement>();

        if (lexer.token == Symbol.IF) {
            lexer.nextToken();

            orT = orTest();

            if (lexer.token == Symbol.LEFTKEY) {
                lexer.nextToken();
                while (lexer.token == Symbol.IDENT || lexer.token == Symbol.PRINT || lexer.token == Symbol.BREAK
                        || lexer.token == Symbol.IF || lexer.token == Symbol.WHILE || lexer.token == Symbol.FOR) {
                    stmt1.add(stmt());
                }
                if (lexer.token == Symbol.RIGHTKEY) {
                    lexer.nextToken();

                } else {
                    error.signal("} expected");
                }
            } else {
                error.signal("{ expected");
            }
            //IfStatement(ArrayList<Statement> stmt1, orTest orT, ArrayList<Statement> stmt2)

            if (lexer.token == Symbol.ELSE) {
                lexer.nextToken();
                if (lexer.token == Symbol.LEFTKEY) {
                    lexer.nextToken();
                    while (lexer.token == Symbol.IDENT || lexer.token == Symbol.PRINT || lexer.token == Symbol.BREAK
                            || lexer.token == Symbol.IF || lexer.token == Symbol.WHILE || lexer.token == Symbol.FOR) {
                        stmt2.add(stmt());
                    }
                    if (lexer.token == Symbol.RIGHTKEY) {
                        lexer.nextToken();

                    } else {
                        error.signal("} expected");
                    }
                } else {
                    error.signal("{ expected");
                }
                return new IfStatement(stmt1, orT, stmt2);
            }
        } else {
            error.signal("IF expected");
        }
        return new IfStatement(stmt1, orT, null);
    }

    //WhileStmt ::= ’while’ OrTest ’{’ {Stmt} ’}’
    public Statement whileStmt() {
        orTest orT;
        ArrayList<Statement> stmt = new ArrayList<Statement>();
        if (lexer.token == Symbol.WHILE) {
            flagLoop = true;
            lexer.nextToken();

            orT = orTest();
            if (lexer.token == Symbol.LEFTKEY) {
                lexer.nextToken();
                while (lexer.token == Symbol.IDENT || lexer.token == Symbol.PRINT || lexer.token == Symbol.BREAK
                        || lexer.token == Symbol.IF || lexer.token == Symbol.ELSE || lexer.token == Symbol.WHILE || lexer.token == Symbol.FOR) {
                    stmt.add(stmt());
                }
                if (lexer.token == Symbol.RIGHTKEY) {
                    lexer.nextToken();
                    flagLoop = false;
                    return new whileStatement(orT, stmt);
                } else {
                    error.signal("} expected");

                }
            } else {
                error.signal("{ expected");
            }
        } else {
            error.signal("WHILE expected");
        }
        return null;
    }

    //ForStmt ::= ’for’ Name ’inrange’ ’(’ Number ’,’ Number ’)’ ’{’ {Stmt} ’}
    public Statement forStmt() {
        Name name;
        numberExpr number1, number2;
        ArrayList<Statement> stmt = new ArrayList<Statement>();

        if (lexer.token == Symbol.FOR) {
            flagLoop = true;
            lexer.nextToken();
            name = name();
            //variavel name não declarada
            if (symbolTable.get(name.getName()) == null) {
                error.signal("The variable " + name.getName() + " was not declared.");
            }
            if (lexer.token == Symbol.INRANGE) {
                lexer.nextToken();
                if (lexer.token == Symbol.LEFTPAR) {
                    lexer.nextToken();
                    number1 = numberExpr();

                    if (number1.getType() != Type.integerType) {
                        error.signal("Range must be an integer number.");
                    }
                    if (lexer.token == Symbol.COMMA) {
                        lexer.nextToken();
                        number2 = numberExpr();
                        if (number1.getType() != Type.integerType) {
                            error.signal("Range must be an integer number.");
                        }
                        if (lexer.token == Symbol.RIGHTPAR) {
                            lexer.nextToken();
                            if (lexer.token == Symbol.LEFTKEY) {
                                lexer.nextToken();
                                while (lexer.token == Symbol.IDENT || lexer.token == Symbol.PRINT || lexer.token == Symbol.BREAK
                                        || lexer.token == Symbol.IF || lexer.token == Symbol.WHILE || lexer.token == Symbol.FOR) {
                                    stmt.add(stmt());
                                }
                                if (lexer.token == Symbol.RIGHTKEY) {

                                    lexer.nextToken();
                                    //ForStatement(Name name, Number number1, Number number2, ArrayList<Statement> stmt) 
                                    flagLoop = false;
                                    return new ForStatement(name, number1, number2, stmt);
                                } else {
                                    error.signal("} expected");
                                }
                            } else {
                                error.signal("{ expected");
                            }
                        } else {
                            error.signal(") expected");
                        }
                    } else {
                        error.signal(", expected");
                    }
                } else {
                    error.signal("( expected");
                }
            } else {
                error.signal("INRANGE expected");
            }
        } else {
            error.signal("FOR expected");
        }
        flagLoop = false;
        return null;
    }
}
