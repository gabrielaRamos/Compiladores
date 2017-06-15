/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
*/
package AST;

import java.util.ArrayList;

public class Term extends Expr {

    private ArrayList<Factor> factor;
    private ArrayList<Character> sinal = null;
    private Type type;
    
    public Term(ArrayList<Factor> factor, ArrayList<Character> sinal) {
        this.factor = factor;
        this.sinal = sinal;
    }

    @Override
    public void genC(PW pw) {
        if (factor != null) {
            factor.get(0).genC(pw);
            if (this.sinal != null) {
                for (int i = 0; i < sinal.size(); i++) {
                    //percorre array para concatenar Term com o sinal *|/
                    pw.out.print(" " + sinal.get(i) + " ");
                    factor.get(i + 1).genC(pw);
                }
            }

        }
    }

    public Type getType(){
        return factor.get(0).getType();
    }

}
