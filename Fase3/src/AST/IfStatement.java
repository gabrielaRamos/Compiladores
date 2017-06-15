/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
*/
package AST;

import java.util.*;

public class IfStatement extends CompoundStmt {

    //IfStmt ::= ’if’ OrTest ’{’ {Stmt} ’}’ [’else’ ’{’ {Stmt} ’}’]
    private ArrayList<Statement> stmt1;
    private ArrayList<Statement> stmt2;
    private orTest orT;

    public IfStatement(ArrayList<Statement> stmt1, orTest orT, ArrayList<Statement> stmt2) {
        this.stmt1 = stmt1;
        this.orT = orT;
        this.stmt2 = stmt2;
    }

    @Override
    public void genC(PW pw) {
        pw.out.println("");
        pw.print("if(");
        orT.genC(pw);
        pw.out.print(")");
        pw.out.println("{");

        if (stmt1 != null) {
            pw.add();
            for (Statement s : stmt1) {
                s.genC(pw);
            }
            pw.sub();
        }

        pw.println("}");

        if (stmt2 != null) {
            pw.print("else {");
            pw.println("");
            pw.add();
            
            for (Statement s : stmt2) {
                s.genC(pw);
            }
                
            pw.sub();
            pw.println("}");

        }
    }

}
