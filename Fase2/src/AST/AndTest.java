/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
*/
package AST;

import java.util.ArrayList;

public class AndTest {
    private ArrayList<NotTest> notTest;

    public AndTest(ArrayList<NotTest> notTest) {
        this.notTest = notTest;
    }
   
    void genC(PW pw) {
        notTest.get(0).genC(pw);
        for(int i=1; i<notTest.size(); i++){
            pw.out.print("&&");
             notTest.get(i).genC(pw);
        }
     }
    
}
