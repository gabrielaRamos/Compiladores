/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
*/
package AST;

public class ExprStmt extends SimpleStmt {

    private Variable variable;
    private orTest orT = null;
    private ExprList expr = null;

    public ExprStmt(Variable variable, orTest orT, ExprList expr) {
        this.variable = variable;
        this.orT = orT;
        this.expr = expr;
    }

    @Override
    public void genC(PW pw) {
        
        if(variable.getType() == Type.stringType && orT != null){
            pw.print("strcpy("+ variable.getName() + ",");
            //para usar strcpy necessariamente deve ser um orTest
            orT.genC(pw);
            pw.out.println(");");
           
        }
        else {
            pw.print(variable.getName() + " = ");
            if(orT != null){
                orT.genC(pw);
            }
            else {
                expr.genC(pw);
            }
             pw.out.print(";");
             pw.println("");
        }
    }
}
