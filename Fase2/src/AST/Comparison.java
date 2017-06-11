/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
*/
package AST;

import java.util.ArrayList;

public class Comparison {

    private Expr expr1;
    private Expr expr2;
    private String op;

    public Comparison(Expr expr1, Expr expr2, String op) {
        this.expr1 = expr1;
        this.expr2 = expr2;
        this.op = op;
    }

    void genC(PW pw) {
        expr1.genC(pw);
        if (op != null) {
            pw.out.print(" "+op+" ");
            expr2.genC(pw);
        }

    }
    
    public Type getType(){
        return expr1.getType();
    }
}
