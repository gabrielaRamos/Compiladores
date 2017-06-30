/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import java.util.ArrayList;

/**
 *
 * @author Gabriela Ramos
 */
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
