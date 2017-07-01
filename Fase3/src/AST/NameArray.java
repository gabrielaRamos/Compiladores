/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

/**
 *
 * Name[‘[’Number‘]’]
 */
public class NameArray {
   private Name name;
   private NumberExpr number;

    public NameArray(Name name, NumberExpr number) {
        this.name = name;
        this.number = number;
    }
    
    public void genC(PW pw){
        name.genC(pw);
        if (number != null){
            pw.print("[");
            number.genC(pw);
            pw.print("]");
        }
    }
    
    public String getNameArray(){
        if(number != null){
            return (name.getName() + "[" + number.getNumInt() + "]");
        }
        else{
            return name.getName();
        }
    }
    
    public String getName() {
        return name.getName();
    }

    public NumberExpr getNumber() {
        return number;
    }
}
