/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
*/
package AST;

import java.util.*;

public class Name {
    //Name ::= Letter{Letter | Digit}

    private String name;
    
    public Name(String name) {
        this.name = name;
      
    }
    
    public void genC(PW pw){
        pw.out.print(this.name);
    }
    
    public String getName(){
        return this.name;
    }
}
