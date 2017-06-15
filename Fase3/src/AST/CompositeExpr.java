/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
*/
package AST;

import java.util.ArrayList;

public class CompositeExpr extends Expr {

    private ArrayList<Term> term;
    private ArrayList<Character> sinal = null;

    public CompositeExpr(ArrayList<Term> term, ArrayList<Character> sinal) {
        this.term = term;
        this.sinal = sinal;
    }

    @Override
    public void genC(PW pw) {
        if (term != null) {
             term.get(0).genC(pw);
             if (this.sinal != null) {
                for (int i = 0; i < sinal.size(); i++) {
                    //percorre array para concatenar Term com o sinal 
                    pw.out.print(" "+sinal.get(i)+" ");
                    term.get(i + 1).genC(pw);
                }
            }
            
        }

    }

    public Type getType(){
        return term.get(0).getType();
    }

  

}
