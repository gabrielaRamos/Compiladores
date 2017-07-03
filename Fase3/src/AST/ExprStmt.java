/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
*/
package AST;

public class ExprStmt extends SimpleStmt {

    private Variable variable;
    private OrTest orT = null;
    private OrList orList = null;

    public ExprStmt(Variable variable, OrTest orT, OrList orList) {
        this.variable = variable;
        this.orT = orT;
        this.orList = orList;
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
                pw.out.print("[");
                orList.genC(pw);
                pw.out.print("]");
            }
             pw.out.print(";");
             pw.println("");
        }
    }
}
