/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
*/
package AST;

import java.util.ArrayList;

public class Declaration {
    //pq static??
    public static ArrayList<ArrayDeclaration> var;
    
    public Declaration (ArrayList<ArrayDeclaration> var){
        this.var = var;
    }
    
    public void genC(PW pw){
        for(ArrayDeclaration v: var){
            
            pw.print(v.getType().getcName() + " ");
            v.genCArray(pw);
           
        }
        pw.println("");
    }
}
