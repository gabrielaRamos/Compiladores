/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
 */
package AST;

import java.util.ArrayList;

/**
 *
 * NameArray {’,’ NameArray}
 */
public class IdList {
    private ArrayList<NameArray> nameArray;

    public IdList(ArrayList<NameArray> nameArray) {
        this.nameArray = nameArray;
    }
    
    public void genC(PW pw){
        nameArray.get(0).genC(pw);
        for(int i=1; i< nameArray.size(); i++){
            pw.out.print(", ");
            nameArray.get(i).genC(pw);
        }
    }
    
    public ArrayList<NameArray> getNameArray(){
        return this.nameArray;
    }
}
