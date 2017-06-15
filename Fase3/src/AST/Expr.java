/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
*/
package AST;

public abstract class Expr {
    public abstract void genC(PW pw);
    
    public abstract Type getType();
}
