/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
*/
package AST;

public class NotTest {
    private String not;
    private Comparison comp;

    public NotTest(String not, Comparison comp) {
        this.not = not;
        this.comp = comp;
    }
    
    void genC(PW pw) {
        if(not != null){
            pw.out.print("!");
        }
        comp.genC(pw);
    }
    
}
