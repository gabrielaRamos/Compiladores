/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
*/
package AST;

import java.util.ArrayList;

public class Declaration {
    public static ArrayList<Variable> var;
    
    public Declaration (ArrayList<Variable> var){
        this.var = var;
    }
    
    public void genC(PW pw){
        for(Variable v: var){
            if(v.getLimit() != 0){
                 pw.println(v.getType().getcName() + " " + v.getName() + "[" + v.getLimit() + "];");
            }
            else if(v.getType() instanceof StringType){
                 pw.println(v.getType().getcName() + " " + v.getName() + "[100];");
            }
            else{
                pw.println(v.getType().getcName() + " " + v.getName() + ";");
            }
           
        }
        pw.println("");
    }
}
