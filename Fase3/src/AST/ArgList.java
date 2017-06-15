/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import java.util.ArrayList;

/**
 *
 *  Type NameArray {’,’ Type NameArray}
 */
public class ArgList {
    private ArrayList<Variable> var;

    public ArgList(ArrayList<Variable> var) {
        this.var = var;
    }
  
    public void genC(PW pw){
        for(Variable v : var){
            pw.println(v.getType().getcName() + " " + v.getName());
        }
    }    
}
