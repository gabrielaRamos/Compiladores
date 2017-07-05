/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
*/
package AST;

import java.util.ArrayList;

public class ArrayDeclaration {
    private Type type;
    private IdList idList;

    public ArrayDeclaration(Type type, IdList idList) {
        this.type = type;
        this.idList = idList;
    }
    
    public Type getType(){
        return type;
    }
    public void genCArray(PW pw){
        idList.genC(pw);
    }
 
        public IdList getIdList(){
        return idList;
    }

}
