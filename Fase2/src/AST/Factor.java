/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
*/
package AST;
import java.util.ArrayList;
import AST.CompilerError;

public class Factor extends Expr {

    private String sinal;
    public Atom atom;
    private ArrayList<Factor> factor = null;
    private Type type;
    
    
    public Factor(String sinal, Atom atom, ArrayList<Factor> factor) {
        this.sinal = sinal;
        this.atom = atom;
        this.factor = factor;
    }

    @Override
    public void genC(PW pw) {
      
        int tam;
        int i;
        if (factor.size() >= 1) {
            tam = factor.size();
            for (i = 0; i < tam; i++) {
                pw.out.print("pow(");
            }         
           
            atom.genC(pw);
            
            for (i = 0; i < tam; i++) {
                pw.out.print(",");

                factor.get(i).genC(pw);
                pw.out.print(")");
            }
        } else {
           
            if (sinal != null) {
                pw.out.print(sinal);
            }
            atom.genC(pw);
        }

    }
    
    public Type getType(){
        return atom.getType();
    }
}
