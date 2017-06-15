/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
            pw.print(",");
            nameArray.get(i).genC(pw);
        }
    }
    
    public ArrayList getIdList(){
        return this.nameArray;
    }
}
