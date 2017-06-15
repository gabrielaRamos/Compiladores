/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
*/
package AST;

import java.util.ArrayList;

public class ForStatement extends CompoundStmt {

    //ForStmt ::= ’for’ Name ’inrange’ ’(’ Number ’,’ Number ’)’ ’{’ {Stmt} ’}’
    private Name name;
    private numberExpr number1;
    private numberExpr number2;
    private ArrayList<Statement> stmt;

    public ForStatement(Name name, numberExpr number1, numberExpr number2, ArrayList<Statement> stmt) {
        this.name = name;
        this.number1 = number1;
        this.number2 = number2;
        this.stmt = stmt;
    }

    @Override
    public void genC(PW pw) {
        //for x inrange(0, 3) → for(x = 0; x < 3; x + +)
        //for x inrange(3, 0) → for(x = 3; x > 0; x − −)
        pw.out.println("");
        pw.print("for(");
        name.genC(pw);
        pw.out.print(" = ");
        number1.genC(pw);
        pw.out.print("; ");
        name.genC(pw);
        if (this.number1.getNumInt()> this.number2.getNumInt()) {
            pw.out.print(" > ");
            number2.genC(pw);
            pw.out.print(" ; ");
            name.genC(pw);
            pw.out.print("--");
        } else {// if(this.number1.getNum < this.number2.getNum){
            pw.out.print(" < ");
            number2.genC(pw);
            pw.out.print("; ");
            name.genC(pw);
            pw.out.print("++");
        }
        
        if(stmt!= null){
            pw.out.println("){");
            pw.add();
            for(Statement s: stmt){
                s.genC(pw);
            }
            pw.sub();
            pw.println("}");
        }
        else{
            pw.out.print(");");
        }
    }

}
