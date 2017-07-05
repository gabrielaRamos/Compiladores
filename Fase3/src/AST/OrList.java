/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
 */
package AST;

import java.util.ArrayList;

public class OrList {

    private ArrayList<OrTest> orT;

    public OrList(ArrayList<OrTest> orT) {
        this.orT = orT;
    }
    
    public int getSize()
    {
        return this.orT.size();
    }

    public void genC(PW pw) {
       
        for (int i = 0; i < orT.size(); i++) {

            if (i == orT.size() - 1) {
                orT.get(i).genC(pw);
            } else {
                orT.get(i).genC(pw);
                pw.out.print(", ");
            }

        }
        

    }
}
