/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
*/
package AST;

import java.util.ArrayList;

public class PrintStmt extends SimpleStmt {

    //PrintStmt ::= ’print’ OrTest {’,’ OrTest}’;’
    private ArrayList<OrTest> orTest;
    public static int print = 0;
    
    public PrintStmt(ArrayList<OrTest> orTest) {
        this.orTest = orTest;
    }
    
    @Override
    public void genC(PW pw) {
        this.print = 0;
        for(OrTest i : orTest){
            
            pw.print("printf(");
            this.print = 1;
            i.genC(pw);
            
            this.print = 0;
            i.genC(pw);
            pw.out.print(");");
            pw.println("");
        }
        
    }
    
}
