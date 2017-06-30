/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
*/
package AST;

import java.util.ArrayList;

public class WhileStatement extends CompoundStmt{
    
    private OrTest orT;
    private ArrayList<Statement> stmt;

    public WhileStatement(OrTest orT, ArrayList<Statement> stmt) {
        this.orT = orT;
        this.stmt = stmt;
    }
    
    public void genC(PW pw){
        pw.print("while(");
        pw.add();
        orT.genC(pw);
        pw.out.print(")");
        pw.out.println("{");
        pw.println("");
        if(stmt != null){
            for(Statement s : stmt){
                s.genC(pw);
            }
        }
        pw.sub();
        pw.println("}");
    }
}
