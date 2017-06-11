/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
*/
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

    //Program ::= ’program’ Name ’:’ Body ’end’
    public Program program() {
        Body b = null;
        if (lexer.token != Symbol.PROGRAM) {
            error.signal("PROGRAM expected");
            return null;
        } else {
            lexer.nextToken();
            Name name = name();
            if (name.getName() == null) {
                error.signal("Name expected");
            } else if (Lexer.keywordsTable.get(name.getName()) != null) {
                error.signal("This name is not allowed");
            }

            if (lexer.token != Symbol.COLON) {
                error.signal(": expected");
            } else {
                lexer.nextToken();
                b = body();

                if (lexer.token != Symbol.END) {
                    error.signal("END expected");
                } else {
                    lexer.nextToken();
                }

            }
            return new Program(b);
        }

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
        } else {
            error.signal("Type expected");
            result = null;
        }
        lexer.nextToken();
        return result;
    }

//IdList ::= Name [ ‘[’Number‘]’ ] {’,’ Name [ ‘[’Number‘]’ ]}
    public ArrayList<String> idList() {
        Name name;
        ArrayList<String> id = new ArrayList<String>();
        String var;
        int intNum = 0;
        numberExpr num = null;

        name = name();

        //verifica se existe var na hashTable
        if (symbolTable.get(name.getName()) != null) {
            error.signal("Variable already declared");
        } else {
            if (Lexer.keywordsTable.get(name.getName()) != null) {
                error.signal("Reserved word");
            }
            if (lexer.token == Symbol.LEFTCOL) {
                lexer.nextToken();
                num = numberExpr();

                if (num.getType() == Type.floatType) {
                    error.signal("Float numbers are not allowed to set the size of an array.");
                } else if (num.getType() == Type.integerType && num.getNumInt() < 0) {
                    error.signal("Negative numbers are not allowed to set the size of an array.");
                }

                if (lexer.token == Symbol.RIGHTCOL) {
                    lexer.nextToken();

                } else {
                    error.signal("] expected");
                }
            }
            if (num != null) {
                if (num.getType() == Type.integerType) {
                    intNum = num.getNumInt();
                    var = name.getName() + "[" + intNum + "]";
                    id.add(var);

                } else {
                    error.signal("Int expected");
                }

            } else {
                var = name.getName();
                id.add(var);
            }

            num = null;
            while (lexer.token == Symbol.COMMA) {

                lexer.nextToken();
                name = name();

                if (symbolTable.get(name.getName()) != null) {
                    error.signal("Variable already declared");
                } else {

                    if (lexer.token == Symbol.LEFTCOL) {
                        lexer.nextToken();
                        num = numberExpr();
                        if (lexer.token == Symbol.RIGHTCOL) {
                            lexer.nextToken();
                        } else {
                            error.signal("] expected");
                        }
                    }
                    if (num != null) {
                        if (num.getType() == Type.integerType) {
                            intNum = num.getNumInt();
                            var = name.getName() + "[" + intNum + "]";
                            id.add(var);
                        } else {
                            error.signal("Int expected");
                        }

                    } else {
                        var = name.getName();
                        id.add(var);
                    }
                    num = null;
                }
            }
        }
        return id;
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

        } else {
            error.signal("SIMPLE STATEMENT expected");
            return null;
        }
    }

    //ExprStmt ::= Name[ ‘[’Number‘]’ ] ’=’ (OrTest | ’[’ ExprList ’]’) ’;’
    public ExprStmt exprStmt() {
        Name name;
       boolean flagVetor = false;

        numberExpr number = null;
        orTest orT = null;
        ExprList exprList = null;
        name = name();

        if (name.getName() == null) {
            error.signal("Name expected");
        }
        //procurar na Hash se a variavel foi declarada e seu tipo

        if (symbolTable.get(name.getName()) == null) {
            error.signal("Variable not declared");
        } else {

            if (lexer.token == Symbol.LEFTCOL) {
                lexer.nextToken();
                number = numberExpr();
                if (lexer.token == Symbol.RIGHTCOL) {
                    lexer.nextToken();
                    flagVetor = true;
                } else {
                    error.signal("] expected");
                    return null;
                }

            }
            Variable var = symbolTable.get(name.getName());

            if (lexer.token == Symbol.ASSIGN) {
                lexer.nextToken();
            } else {
                error.signal("= expected");
                return null;
            }
            if (lexer.token == Symbol.SEMICOLON || lexer.token == Symbol.DIV || lexer.token == Symbol.MULT || lexer.token == Symbol.POW) {
                error.signal("Right operand expected");
            }
            if (lexer.token == Symbol.NOT || lexer.token == Symbol.MINUS || lexer.token == Symbol.PLUS || lexer.token == Symbol.IDENT
                    || lexer.token == Symbol.NUMBER || lexer.token == Symbol.STRING || lexer.token == Symbol.TRUE || lexer.token == Symbol.FALSE
                    || lexer.token == Symbol.CHARACTER) {

                // x = numero
                if (lexer.token == Symbol.NUMBER) {
                    //x tipo float = 1;
                    if (symbolTable.get(name.getName()).getType() == Type.floatType && lexer.isIntOrFloat() == 0) {
                        error.signal("The variable " + name.getName() + " only can receive values of float type.");
                    } //x tipo int = 1,0
                    else if (symbolTable.get(name.getName()).getType() == Type.integerType && lexer.isIntOrFloat() == 1) {
                        error.signal("The variable " + name.getName() + " only can receive values of int type.");
                    }
                } //x = variavel                
                else if (lexer.token == Symbol.IDENT) {
                    if (symbolTable.get(lexer.getStringValue()) == null) {
                        error.signal("The variable " + lexer.getStringValue() + " was not declared.");
                    } //VERIFICAR!!!
                    else if (symbolTable.get(lexer.getStringValue()).getType() == symbolTable.get(name.getName()).getType()) {
                       
                        if (flagVetor == false && symbolTable.get(lexer.getStringValue()).getLimit() != 0) {
                            error.signal("Invalid assignment.");
                        }
                    } else if (symbolTable.get(lexer.getStringValue()).getType() != symbolTable.get(name.getName()).getType()) {
                        //var.getType() instanceof BooleanType 
                        error.signal("The variable " + name.getName() + " can only receive values of " + symbolTable.get(name.getName()).getType().getcName() + ".");
                    } //x = vetor
                    else if (symbolTable.get(lexer.getStringValue()).getLimit() != 0 && symbolTable.get(name.getName()).getLimit() != 0) {
                        //var.getType() instanceof BooleanType 
                        error.signal("The variable " + name.getName() + " can not receive an array.");
                    }
                } else if (lexer.token == Symbol.STRING && symbolTable.get(name.getName()).getType() != Type.stringType) {
                    error.signal("The variable " + name.getName() + " can only receive values of string type.");
                } else if (lexer.token == Symbol.CHAR && symbolTable.get(name.getName()).getType() != Type.charType) {
                    error.signal("The variable " + name.getName() + " can only receive values of char type.");
                } else if ((lexer.token == Symbol.TRUE || lexer.token == Symbol.FALSE) && symbolTable.get(name.getName()).getType() != Type.booleanType) {
                    error.signal("The variable " + name.getName() + " can only receive values 'true' or 'false'.");
                }

                orT = orTest();

            } else if (lexer.token == Symbol.LEFTCOL) {
                lexer.nextToken();
                exprList = exprList();

                if (symbolTable.get(name.getName()).getLimit() != 0 && flagVetor == false) {

                    if (symbolTable.get(name.getName()).getLimit() != exprList.getExpr().size()) {
                        error.signal("Assignment error.");
                    }
                }

                if (lexer.token == Symbol.RIGHTCOL) {
                    lexer.nextToken();
                } else {
                    error.signal("] expected");
                    return null;
                }
            }

            if (lexer.token == Symbol.SEMICOLON) {
                lexer.nextToken();
                return new ExprStmt(var, orT, exprList);
            } else {
                error.signal("; expected");
                return null;
            }
        }
        error.signal("Right operand expected");
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
            if(comp.getType() == Type.stringType){
                error.signal("Not is not allowed with strings");
            }
             return new NotTest(not,comp);
        }
        return new NotTest(not,comparison());

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
      
            if((expr1.getType() != expr2.getType()) || ( (flagExpr1 == true|| flagExpr2 == true) && expr1.getType() == expr2.getType())){
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

    //Factor ::= [’+’|’-’] Atom {^ Factor}
    public Factor factor() {
        String sinal = null;
        Atom atom = null;
        ArrayList<Factor> factor = new ArrayList<Factor>();

        if (lexer.token == Symbol.PLUS) {
            sinal = "+";
            lexer.nextToken();
        } else if (lexer.token == Symbol.MINUS) {
            sinal = "-";
            lexer.nextToken();
        }

        atom = atom();
        Type type = atom.getType();

        while (lexer.token == Symbol.POW) {
            lexer.nextToken();
            factor.add(factor());
        }
      
        for (int i = 0; i < factor.size(); i++) {
            if (type.getcName().equals(factor.get(i).atom.type.getcName()) == false) {
                error.signal("Types are different");
            }
        }       
        return new Factor(sinal, atom, factor);
    }

    //Atom ::= Name[ ‘[’(Number | Name)‘]’ ] | Number | String | ’True’ | ’False’
    public Atom atom() {
        Name name1, name2;
        numberExpr number;
        String vetor;
        boolean flagVetorAtom = false;
        int limit = 0;

        if (lexer.token == Symbol.IDENT) {
            name1 = name();

            Variable var = symbolTable.get(name1.getName());
            if (var == null) {
                error.signal("Variable not declared " + name1.getName());
            } else {
                if (var.getLimit() != 0 && (var.getType().getcName().equals("int") || var.getType().getcName().equals("float"))) {
                    flagVetorAtom = true;  
                } else if (var.getLimit() != 0) {
                    limit = var.getLimit();
                }
            }
            if (lexer.token == Symbol.LEFTCOL) {
                
                if (flagVetorAtom == false) {
                    error.signal("Invalid assignment.");
                }
                vetor = name1.getName() + "[";
                lexer.nextToken();
                if (lexer.token == Symbol.PLUS || lexer.token == Symbol.MINUS || lexer.token == Symbol.NUMBER) {
                    number = numberExpr();
                    if (number.getType() == Type.integerType) {
                        int num = number.getNumInt();
                        String numInt = num + "";
                        vetor = vetor + numInt;

                        if (number.getNumInt() >= limit) {
                            error.signal("Array index out of bounds.");
                        }
                    } else {
                        //ERRO FLOAT NO VETOR
                        error.signal("int expected");
                        return null;
                    }
                } else if (lexer.token == Symbol.IDENT) {
                    name2 = name();
                    if (symbolTable.get(name2.getName()) == null) {
                        error.signal("Variable " + name2.getName() + " was not declared.");
                    }
                    vetor = vetor + name2.getName();
                } else {
                    error.signal("NUMBER or NAME expected");
                }
                if (lexer.token == Symbol.RIGHTCOL) {
                    lexer.nextToken();
                    //TEM QUE CONCATENAR TUDO AINDA
                    vetor = vetor + "]";

                    return new Atom(vetor, var.getType());
                } else {
                    error.signal("] expected");
                    return null;
                }
            }
            if(flagVetorAtom == true){
                   flagVetorAtomCol = true; 
                }
            return new Atom(name1.getName(), var.getType());
        } else if (lexer.token == Symbol.PLUS || lexer.token == Symbol.MINUS || lexer.token == Symbol.NUMBER) {
            number = numberExpr();
            if (number.getType() == Type.integerType) {
                int num = number.getNumInt();
                String numInt = num + "";
                return new Atom(numInt, Type.integerType);
            } else {
                //ERRO FLOAT NO VETOR
                float num = number.getNumFloat();
                String numFloat = num + "";
                return new Atom(numFloat, Type.floatType);
            }
        } else if (lexer.token == Symbol.STRING || lexer.token == Symbol.CHARACTER) {
            String str = string();

            return new Atom(str, 1, Type.stringType);

        } else if (lexer.token == Symbol.TRUE) {
            lexer.nextToken();
            return new Atom("True", Type.booleanType);

        } else if (lexer.token == Symbol.FALSE) {
            lexer.nextToken();
            return new Atom("False", Type.booleanType);

        } else {
            error.signal("NUMBER or STRING or BOOLEAN expected");
            return null;
        }
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

    //ExprList ::= Expr {, Expr}
    public ExprList exprList() {
        ArrayList<Expr> expr = new ArrayList<Expr>();

        expr.add(expr());
        while (lexer.token == Symbol.COMMA) {
            lexer.nextToken();
            expr.add(expr());
        }
        return new ExprList(expr);
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
