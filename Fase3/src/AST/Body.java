/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
*/
package AST;

import java.util.ArrayList;

public class Body {

    private Declaration dec = null;
    private ArrayList<Statement> stmt;

    public Body(Declaration dec, ArrayList<Statement> stmt) {
        this.dec = dec;
        this.stmt = stmt;
    }

    public void genC(PW pw) {
        if (this.dec != null) {
            dec.genC(pw);
        }
        if (stmt != null) {
            for (Statement v : stmt) {
                v.genC(pw);
            }
        }
    }
}
