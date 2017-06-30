/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import java.util.ArrayList;

public class OrList {
    private ArrayList<OrTest> orT;

    public OrList(ArrayList<OrTest> orT) {
        this.orT = orT;
    }
    
    public void genC(PW pw){
        for(OrTest orT : orT){
            orT.genC(pw);
            pw.print(",");
        }
    }
}
