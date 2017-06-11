/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
*/
package AST;

import java.util.ArrayList;


public class ExprList{
    private ArrayList<Expr> expr;
        
    public ExprList(ArrayList<Expr> expr){
        this.expr = expr;
    }
    
    public ArrayList<Expr> getExpr() {
        return expr;
    }
    
    public void genC(PW pw) {
        if(expr != null){
            pw.out.print("[");
            for(int i = 0; i< expr.size(); i++){
                expr.get(i).genC(pw);
                if(i < expr.size()-1){
                  pw.out.print(", ");  
                }
                
            }
            pw.out.print("]");
        }
    }
    
}
