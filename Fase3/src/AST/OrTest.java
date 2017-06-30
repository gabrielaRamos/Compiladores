/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
*/
package AST;

import java.util.ArrayList;


public class OrTest {

    //OrTest ::= AndTest {’or’ AndTest}
    private ArrayList<AndTest> andT;

    public OrTest(ArrayList<AndTest> andT) {
        this.andT = andT;
    }

    public void genC(PW pw) {
        andT.get(0).genC(pw);

        for (int i = 1; i < andT.size(); i++) {
            pw.out.print("||");
            andT.get(i).genC(pw);
        }
    }

}
