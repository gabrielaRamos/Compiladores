/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
*/
package AST;

public class BreakStmt extends SimpleStmt{

    @Override
    public void genC(PW pw) {
        pw.println("break;");
    }
    
}
