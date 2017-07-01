/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
 */
import Lexer.CompilerError;
import AST.*;
import java.util.*;
import Lexer.*;
import java.io.*;

public class Compiler {

    private SymbolTable symbolTable;
    private Lexer lexer;
    public CompilerError error;
    private boolean flagLoop;
    private boolean flagVetorAtomCol = false;

    public Program compile(char[] input, PrintWriter outError, String fileName) {
        symbolTable = new SymbolTable();
        error = new CompilerError(lexer, new PrintWriter(outError), fileName);
        lexer = new Lexer(input, error);
        error.setLexer(lexer);
        lexer.nextToken();
        return program();
    }
    //VERIFICAR SE É NECESSÁRIO USAR O TRY CATCH

    //Program ::= ’program’ Name ’:’ FuncDef {FuncDef} ’end’
    public Program program() {
        ArrayList<FuncDef> funcDef = new ArrayList<FuncDef>();
        if (lexer.token == Symbol.PROGRAM) {
            lexer.nextToken();
            name();
            if (lexer.token == Symbol.COLON) {
                lexer.nextToken();
                funcDef.add(funcDef());
                while (lexer.token == Symbol.DEF) {
                    funcDef.add(funcDef());
                }
                if (symbolTable.getInGlobal("main") == null) {
                    error.show("Source code must have a procedure called main");
                }
                if (lexer.token == Symbol.END) {
                    lexer.nextToken();

                } else {
                    error.signal("end expected");
                }
            } else {
                error.signal(": expected");
            }
        } else {
            error.signal("Program Expected");
        }

        return new Program(funcDef);
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

    //FuncDef ::= ’def’ Name ’(’ [ArgsList] ’)’ : Type ’{’Body’}’
    public FuncDef funcDef() {

        Type type = null;
        Body body = null;
        Name name = null;
        ArgList argList = null;

        if (lexer.token == Symbol.DEF) {
            lexer.nextToken();
            name = name();
            if (symbolTable.getInGlobal(name) == null) {

                if (lexer.token == Symbol.LEFTPAR) {
                    lexer.nextToken();
                    if (lexer.token == Symbol.BOOLEAN || lexer.token == Symbol.INT || lexer.token == Symbol.CHAR || lexer.token == Symbol.FLOAT || lexer.token == Symbol.STRING) {
                        argList = argsList();
                    }
                    if (lexer.token == Symbol.RIGHTPAR) {
                        lexer.nextToken();

                        if (lexer.token == Symbol.COLON) {
                            lexer.nextToken();

                            type = type();

                            if (lexer.token == Symbol.CURLYLEFTBRACE) {
                                lexer.nextToken();
                                body = body();
                                if (lexer.token == Symbol.CURLYRIGHTBRACE) {
                                    lexer.nextToken();
                                    FuncDef func = new FuncDef(type, body, name, argList);
                                    symbolTable.putInGlobal(name.getName(), func);

                                    return func;
                                } else {
                                    error.signal("} expected");
                                    return null;
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
            } else {
                //DECLARAÇÃO DE VARIAVEL
                error.show("function alredy statement");

            }
        }
        return null;
    }

    //ArgsList ::= Type NameArray {’,’ Type NameArray}
    //Lista de variables
    public ArgList argsList() {
        ArrayList<Variable> varList = new ArrayList<Variable>();
        Type type = null;
        NameArray name = null;
        Variable var = null;

        type = type();
        name = nameArray();
        if (symbolTable.getInLocal(name.getNameArray()) == null) {
            
            var = new Variable(type, name);
            symbolTable.putInLocal(name.getNameArray(), var);
            varList.add(var);

            while (lexer.token == Symbol.COMMA) {
                lexer.nextToken();
                type = type();
                name = nameArray();
                if(symbolTable.getInLocal(name.getNameArray()) == null){
                    var = new Variable(type, name);
                    symbolTable.putInLocal(name.getNameArray(), var);
                    varList.add(var);
                }
                else{
                    error.signal("variable alredy declared");
                }
                

            }
            return new ArgList(varList);
        } else {
            error.signal("variable alredy declared");
            return null;
        }
    }

    //NameArray ::= Name[‘[’Number‘]’]
    public NameArray nameArray() {
        Name name = null;
        NumberExpr number = null;

        name = name();
        if (lexer.token == Symbol.LEFTSQBRACKET) {
            lexer.nextToken();
            number = numberExpr();
            if (lexer.token == Symbol.RIGHTSQBRACKET) {
                lexer.nextToken();
            } else {
                error.signal("] expected");
            }
        }
        return new NameArray(name, number);
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
        Declaration dec = null;
        ArrayList<Statement> stmt = new ArrayList<Statement>();

        if (lexer.token == Symbol.INT || lexer.token == Symbol.FLOAT || lexer.token == Symbol.STRING || lexer.token == Symbol.BOOLEAN || lexer.token == Symbol.IDENT) {
            dec = declaration();
        }

        while (lexer.token == Symbol.IDENT || lexer.token == Symbol.PRINT || lexer.token == Symbol.BREAK
                || lexer.token == Symbol.IF || lexer.token == Symbol.WHILE || lexer.token == Symbol.FOR || lexer.token == Symbol.FUNCTION || lexer.token == Symbol.RETURN) {
            stmt.add(stmt());
        }
        symbolTable.removeLocalIdent();
        return new Body(dec, stmt);
    }

    //Declaration ::= Type IdList ’;’{ Type IdList’;’}
    //DUVIDA!!!!
    public Declaration declaration() {
        ArrayList<ArrayDeclaration> arrayDec = new ArrayList<ArrayDeclaration>();
        Variable var = null;
        IdList idList = null;
        ArrayDeclaration objDec = null;
        Type type = null;

        type = type();
        idList = idList();
        objDec = new ArrayDeclaration(type, idList);
        arrayDec.add(objDec);

        for (int i = 0; i < arrayDec.size(); i++) {
            for (int j = 0; j < arrayDec.get(i).getIdList().getNameArray().size(); j++) {
                var = new Variable(arrayDec.get(i).getType(), arrayDec.get(i).getIdList().getNameArray().get(j));

                if (symbolTable.getInLocal(var) == null) {

                    symbolTable.putInLocal(var.getName(), var);

                } else {
                    error.signal("Variable was already declared");
                }
            }
        }

        if (lexer.token == Symbol.SEMICOLON) {
            lexer.nextToken();

            while (lexer.token == Symbol.INT || lexer.token == Symbol.BOOLEAN || lexer.token == Symbol.CHAR || lexer.token == Symbol.FLOAT || lexer.token == Symbol.STRING || lexer.token == Symbol.VOID) {

                type = type();
                idList = idList();
                objDec = new ArrayDeclaration(type, idList);
                arrayDec.add(objDec);

                for (int i = 0; i < arrayDec.size(); i++) {
                    for (int j = 0; j < arrayDec.get(i).getIdList().getNameArray().size(); j++) {
                        var = new Variable(arrayDec.get(i).getType(), arrayDec.get(i).getIdList().getNameArray().get(j));

                        if (symbolTable.getInLocal(var) == null) {

                            symbolTable.putInLocal(var.getName(), var);
                        } else {
                            error.signal("Variable was already declared");
                        }
                    }
                }

                if (lexer.token == Symbol.SEMICOLON) {
                    lexer.nextToken();
                } else {
                    error.signal("; expected.");
                }

            }
        } else {
            error.signal("; expected.");
        }

        return new Declaration(arrayDec);
    }

    //Type ::= ’int’ | ’float’ | ’string’ | ’boolean’
    //TYPE VOID
    public Type type() {
        Type result = null;

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
        } else if (lexer.token == Symbol.VECTOR) {
            result = Type.vectorType;
        } else {
            error.signal("Type expected");
            result = null;
        }
        lexer.nextToken();
        return result;
    }

//IdList ::= NameArray {’,’ NameArray}
    public IdList idList() {

        ArrayList<NameArray> nameArray = new ArrayList<NameArray>();

        nameArray.add(nameArray());
        while (lexer.token == Symbol.COMMA) {
            lexer.nextToken();
            nameArray.add(nameArray());
        }

        return new IdList(nameArray);
    }

    //Stmt ::= SimpleStmt | CompoundStmt
    public Statement stmt() {

        Statement stmt = null;

        if (lexer.token == Symbol.IDENT || lexer.token == Symbol.PRINT || lexer.token == Symbol.BREAK || lexer.token == Symbol.FUNCTION || lexer.token == Symbol.RETURN) {
            return simpleStmt();

        } else if (lexer.token == Symbol.IF || lexer.token == Symbol.ELSE || lexer.token == Symbol.WHILE || lexer.token == Symbol.FOR) {

            return compoundStmt();

        } else {
            error.signal("STATEMENT expected");
            return null;
        }

    }

    //SimpleStmt ::= ExprStmt | PrintStmt | BreakStmt | ReturnStmt | FuncStmt
    public Statement simpleStmt() {

        SimpleStmt simpleStmt = null;

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

    //ExprStmt ::= Name [ ‘[’Atom‘]’ ] ’=’ (OrTest | ’[’OrList’]’) ’;’
    public ExprStmt exprStmt() {

        Variable var = null;
        OrTest orT = null;
        OrList orList = null;
        Name name = null;
        Atom atom = null;

        name = name();

        if (name.getName() == null) {
            error.signal("Name expected");
        }
        
        if (symbolTable.getInLocal(name.getName()) == null) {
            error.signal("Variable "+ name.getName()+" not declared");
        } else {
            var = (Variable) symbolTable.getInLocal(name.getName());
            if (lexer.token == Symbol.LEFTSQBRACKET) {
                lexer.nextToken();
                atom = atom();

                if (lexer.token == Symbol.RIGHTSQBRACKET) {
                    lexer.nextToken();
                } else {
                    error.signal("] expected.");
                }

            }
            if (lexer.token == Symbol.ASSIGN) {
                lexer.nextToken();

                if (lexer.token == Symbol.NOT || lexer.token == Symbol.MINUS || lexer.token == Symbol.PLUS || lexer.token == Symbol.IDENT || lexer.token == Symbol.CHARACTER
                        || lexer.token == Symbol.STRING || lexer.token == Symbol.NUMBER || lexer.token == Symbol.TRUE || lexer.token == Symbol.FALSE) {

                    // x = numero
                    if (lexer.token == Symbol.NUMBER) {
                        //x tipo float = 1;
                        if (((Variable) symbolTable.getInLocal(name.getName())).getType() == Type.floatType && lexer.isIntOrFloat() == 0) {
                            error.signal("The variable " + name.getName() + " only can receive values of float type.");
                        } //x tipo int = 1,0
                        else if (((Variable) symbolTable.getInLocal(name.getName())).getType() == Type.integerType && lexer.isIntOrFloat() == 1) {
                            error.signal("The variable " + name.getName() + " only can receive values of int type.");
                        }
                    } //x = variavel                
                    else if (lexer.token == Symbol.IDENT) {
                        if (symbolTable.get(lexer.getStringValue()) == null) {
                            error.signal("The variable " + lexer.getStringValue() + " was not declared.");
                        } //VERIFICAR!!!
                        
                        
                        
                        if ((((Variable) symbolTable.getInLocal(name.getName())).getType() == (((Variable) symbolTable.getInLocal(lexer.getStringValue())).getType())) ||
                                (((Variable) symbolTable.getInLocal(name.getName())).getType() == (((FuncDef) symbolTable.getInGlobal(lexer.getStringValue())).getType()))) {

                            if (atom != null && ((Variable) symbolTable.getInLocal(lexer.getStringValue())).getObjNameArray().getNumber() != null) {
                                error.signal("Invalid assignment.");
                            }
                        } else if (((Variable) symbolTable.getInLocal(lexer.getStringValue())).getType() != ((Variable) symbolTable.getInLocal(lexer.getStringValue())).getType()) {

                            error.signal("The variable " + name.getName() + " can only receive values of " + ((Variable) symbolTable.getInLocal(lexer.getStringValue())).getType().getcName() + ".");
                        } //x = vetor
                        else if (((Variable) symbolTable.getInLocal(lexer.getStringValue())).getObjNameArray().getNumber() == null && ((Variable) symbolTable.getInLocal(name.getName())).getObjNameArray().getNumber() != null) {

                            error.signal("The variable " + name.getName() + " can not receive an array.");
                        }
                    } else if (lexer.token == Symbol.STRING && ((Variable) symbolTable.getInLocal(name.getName())).getType() != Type.stringType) {
                        error.signal("The variable " + name.getName() + " can only receive values of string type.");
                    } else if (lexer.token == Symbol.CHAR && ((Variable) symbolTable.getInLocal(name.getName())).getType() != Type.charType) {
                        error.signal("The variable " + name.getName() + " can only receive values of char type.");
                    } else if ((lexer.token == Symbol.TRUE || lexer.token == Symbol.FALSE) && ((Variable) symbolTable.getInLocal(name.getName())).getType() != Type.booleanType) {
                        error.signal("The variable " + name.getName() + " can only receive values 'true' or 'false'.");
                    }
                    orT = orTest();

                } else if (lexer.token == Symbol.LEFTSQBRACKET) {
                    lexer.nextToken();
                    orList = orList();

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
                    System.out.println("VARRR " + var);
                    return new ExprStmt(var, orT, orList);

                } else {
                    error.signal("; expected");
                }

            } else {
                error.signal("= expected.");
            }
        }
        error.signal("Right operand expected");
        return null;
    }

    //OrTest ::= AndTest {’or’ AndTest}
    public OrTest orTest() {
        ArrayList<AndTest> andT = new ArrayList<AndTest>();

        andT.add(andTest());

        while (lexer.token == Symbol.OR) {

            lexer.nextToken();

            andT.add(andTest());
        }

        return new OrTest(andT);
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
        Comparison comp = null;

        if (lexer.token == Symbol.NOT) {
            not = "not";
            lexer.nextToken();

        }
        comp = comparison();

        return new NotTest(not, comp);
        /*
            if (comp.getType() == Type.stringType) {
                error.signal("Not is not allowed with strings");
            }
            return new NotTest(not, comp);*/

        //  return new NotTest(not, comparison());
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
            /*
            if ((expr1.getType() != expr2.getType()) || ((flagExpr1 == true || flagExpr2 == true) && expr1.getType() == expr2.getType())) {
                error.signal("Comparison not allowed");
            }*/

        }
        return new Comparison(expr1, expr2, op);
    }

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
        /*
        for (int i = 0; i < term.size(); i++) {
            if (term.get(0).getType().getcName().equals(term.get(i).getType().getcName()) == false) {
                error.signal("Types are different");
            }
        }*/
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
        /*
        for (int i = 0; i < factor.size(); i++) {
            if (factor.get(0).atom.type.getcName().equals(factor.get(i).atom.type.getcName()) == false) {
                error.signal("Types are different");
            }
        }*/
        return new Term(factor, sinal);

    }

    //Factor ::= [Signal] AtomExpr {’^’ Factor}
    public Factor factor() {
        String sinal = null;
        AtomExpr atom = null;
        ArrayList<Factor> factor = new ArrayList<Factor>();
        Type type = null;

        if (lexer.token == Symbol.PLUS) {
            sinal = "+";
            lexer.nextToken();
        } else if (lexer.token == Symbol.MINUS) {
            sinal = "-";
            lexer.nextToken();
        }

        atom = atomExpr();

        while (lexer.token == Symbol.POW) {
            lexer.nextToken();
            factor.add(factor());
        }

        /* for (int i = 0; i < factor.size(); i++) {
            if (type.getcName().equals(factor.get(i).atom.type.getcName()) == false) {
                error.signal("Types are different");
            }
        }    */
        return new Factor(sinal, atom, factor);

    }

//AtomExpr ::= Atom [Details]
    public AtomExpr atomExpr() {
        Atom atom = null;
        Details details = null;

        atom = atom();

        if (lexer.token == Symbol.LEFTSQBRACKET || lexer.token == Symbol.LEFTPAR) {

            details = details();

        }

        return new AtomExpr(atom, details);
    }

    //Atom ::= Name | Number | String | ’True’ | ’False’
    //DUVIDA
    public Atom atom() {

        if (lexer.token == Symbol.IDENT) {
            Name name;
            name = name();
            Variable var;

            var = (Variable) symbolTable.getInLocal(name.getName());
            if (var != null) {
                return new Atom(name.getName(), var.getType());
            } else {
                FuncDef func = (FuncDef) symbolTable.getInGlobal(name.getName());;
                if (func != null) {

                    return new Atom(name.getName(), func.getType());
                } else {
                    error.show("Variable not statement");
                    return null;
                }
            }
        } else if (lexer.token == Symbol.NUMBER) {
            NumberExpr number;
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
            return new Atom(string(), 1, Type.stringType);
        } else if (lexer.token == Symbol.TRUE) {
            lexer.nextToken();
            return new Atom("true", Type.booleanType);
        } else if (lexer.token == Symbol.FALSE) {
            lexer.nextToken();
            return new Atom("true", Type.booleanType);
        } else {
            error.signal("NUMBER or STRING or BOOLEAN expected.");
            return null;
        }
    }

    //Details ::= ‘[’(Number | Name)‘]’ | ’(’ [OrList] ’)’
    public Details details() {
        NumberExpr number = null;
        Name name = null;
        OrList orList = null;

        if (lexer.token == Symbol.LEFTSQBRACKET) {

            lexer.nextToken();

            if (lexer.token == Symbol.NUMBER) {
                number = numberExpr();
            } else if (lexer.token == Symbol.IDENT) {
                name = name();
            } else {
                error.signal("Number or name expected.");
            }

            if (lexer.token == Symbol.RIGHTSQBRACKET) {
                lexer.nextToken();
            } else {
                error.signal("] expected.");
            }

            return new Details(number, name);
        }
        if (lexer.token == Symbol.LEFTPAR) {
            lexer.nextToken();

            if (lexer.token == Symbol.NOT || lexer.token == Symbol.MINUS || lexer.token == Symbol.PLUS || lexer.token == Symbol.IDENT
                    || lexer.token == Symbol.STRING || lexer.token == Symbol.NUMBER || lexer.token == Symbol.TRUE || lexer.token == Symbol.FALSE) {

                orList = orList();
                return new Details("(", orList);
            }

            if (lexer.token == Symbol.RIGHTPAR) {
                lexer.nextToken();
            } else {
                error.signal(") expected.");
            }

            return new Details("(");
        }

        return null;
    }

    //Number ::= [Signal] Digit{Digit} [’.’ Digit{Digit}]
    //Implementar no lexer
    public NumberExpr numberExpr() {
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
            return new NumberExpr(sinal, valueFloat, flag);
        } else {
            valueInt = lexer.getNumberValueInt();
            if (Lexer.MaxValueInteger - 1 == valueInt) {
                error.signal("Number expected");
            }
            lexer.nextToken();

            return new NumberExpr(sinal, valueInt);
        }
    }

    // String ::= ”’ . ”’
    public String string() {
        String str = null;
        char ch;

        //captura string no Lexer
        if (lexer.token == Symbol.STRING) {
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

    //OrList ::= OrTest {’,’ OrTest}
    public OrList orList() {

        ArrayList<OrTest> orT = new ArrayList<OrTest>();

        orT.add(orTest());

        while (lexer.token == Symbol.COMMA) {
            lexer.nextToken();
            orT.add(orTest());
        }

        return new OrList(orT);
    }

    //PrintStmt ::= ’print’ OrTest {’,’ OrTest}’;’
    public PrintStmt printStmt() {
        ArrayList<OrTest> orT = new ArrayList<OrTest>();

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

        OrTest orT = null;

        if (lexer.token == Symbol.RETURN) {
            lexer.nextToken();

            if (lexer.token == Symbol.NOT || lexer.token == Symbol.MINUS || lexer.token == Symbol.PLUS || lexer.token == Symbol.IDENT
                    || lexer.token == Symbol.STRING || lexer.token == Symbol.NUMBER || lexer.token == Symbol.TRUE || lexer.token == Symbol.FALSE) {

                orT = orTest();
            }

            if (lexer.token == Symbol.SEMICOLON) {
                lexer.nextToken();

            } else {
                error.signal("; expected.");
            }

        } else {
            error.signal("'Return' expected.");
        }

        return new ReturnStmt(orT);
    }

    //FuncStmt ::= Name’(’ [OrList] ’)”;’
    public FuncStmt funcStmt() {
        Name name = null;
        OrList orList = null;

        name = name();

        if (lexer.token == Symbol.LEFTPAR) {
            lexer.nextToken();

            if (lexer.token == Symbol.NOT || lexer.token == Symbol.MINUS || lexer.token == Symbol.PLUS || lexer.token == Symbol.IDENT
                    || lexer.token == Symbol.STRING || lexer.token == Symbol.NUMBER || lexer.token == Symbol.TRUE || lexer.token == Symbol.FALSE) {

                orList = orList();
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

        return new FuncStmt(name, orList);
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
        OrTest orT = null;
        ArrayList<Statement> stmt2 = new ArrayList<Statement>();

        if (lexer.token == Symbol.IF) {
            lexer.nextToken();

            orT = orTest();

            if (lexer.token == Symbol.CURLYLEFTBRACE) {
                lexer.nextToken();
                while (lexer.token == Symbol.IDENT || lexer.token == Symbol.PRINT || lexer.token == Symbol.BREAK
                        || lexer.token == Symbol.IF || lexer.token == Symbol.WHILE || lexer.token == Symbol.FOR) {
                    stmt1.add(stmt());

                }
                if (lexer.token == Symbol.CURLYRIGHTBRACE) {
                    lexer.nextToken();

                } else {
                    error.signal("} expected");
                }
            } else {
                error.signal("{ expected");
            }

            if (lexer.token == Symbol.ELSE) {
                lexer.nextToken();
                if (lexer.token == Symbol.CURLYLEFTBRACE) {
                    lexer.nextToken();
                    while (lexer.token == Symbol.IDENT || lexer.token == Symbol.PRINT || lexer.token == Symbol.BREAK
                            || lexer.token == Symbol.IF || lexer.token == Symbol.WHILE || lexer.token == Symbol.FOR) {
                        stmt2.add(stmt());

                    }
                    if (lexer.token == Symbol.CURLYRIGHTBRACE) {
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
        OrTest orT = null;
        ArrayList<Statement> stmt = new ArrayList<Statement>();

        if (lexer.token == Symbol.WHILE) {
            flagLoop = true;
            lexer.nextToken();

            orT = orTest();
            if (lexer.token == Symbol.CURLYLEFTBRACE) {
                lexer.nextToken();
                while (lexer.token == Symbol.IDENT || lexer.token == Symbol.PRINT || lexer.token == Symbol.BREAK
                        || lexer.token == Symbol.IF || lexer.token == Symbol.ELSE || lexer.token == Symbol.WHILE || lexer.token == Symbol.FOR) {
                    stmt.add(stmt());

                }
                if (lexer.token == Symbol.CURLYRIGHTBRACE) {
                    lexer.nextToken();
                    flagLoop = false;
                    return new WhileStatement(orT, stmt);
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
        NumberExpr number1, number2;
        ArrayList<Statement> stmt = new ArrayList<Statement>();

        if (lexer.token == Symbol.FOR) {
            flagLoop = true;
            lexer.nextToken();
            name = name();
            //variavel name não declarada
            /* if (symbolTable.get(name.getName()) == null) {
                error.signal("The variable " + name.getName() + " was not declared.");
            }*/
            if (lexer.token == Symbol.INRANGE) {
                lexer.nextToken();
                if (lexer.token == Symbol.LEFTPAR) {
                    lexer.nextToken();
                    number1 = numberExpr();

                    /*  if (number1.getType() != Type.integerType) {
                        error.signal("Range must be an integer number.");
                    }*/
                    if (lexer.token == Symbol.COMMA) {
                        lexer.nextToken();
                        number2 = numberExpr();
                        /* if (number1.getType() != Type.integerType) {
                            error.signal("Range must be an integer number.");
                        }*/
                        if (lexer.token == Symbol.RIGHTPAR) {
                            lexer.nextToken();
                            if (lexer.token == Symbol.CURLYLEFTBRACE) {
                                lexer.nextToken();
                                while (lexer.token == Symbol.IDENT || lexer.token == Symbol.PRINT || lexer.token == Symbol.BREAK
                                        || lexer.token == Symbol.IF || lexer.token == Symbol.WHILE || lexer.token == Symbol.FOR) {
                                    stmt.add(stmt());

                                }
                                if (lexer.token == Symbol.CURLYRIGHTBRACE) {

                                    lexer.nextToken();
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
