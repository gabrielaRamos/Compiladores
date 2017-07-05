/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
*/
package AST;

/**
 *
 * ReturnStmt ::= ’return’ [OrTest]’;’
 */
public class ReturnStmt extends SimpleStmt{
    private OrTest orT;
    
    public ReturnStmt(OrTest orT){
        this.orT =  orT;
    }
    
    public void genC(PW pw){
        
        pw.print("return ");
        
        if (orT != null){
            orT.genC(pw);
        }
        pw.out.print(";");
        pw.println("");
    }
}

