/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

/**
 *
 * ‘[’(Number | Name)‘]’ | ’(’ [OrList] ’)’
 */
class Details {
    private numberExpr number;
    private Name name;
    private OrList orList;

    public Details(OrList orList) {
        this.orList = orList;
    }
    
    public Details(numberExpr number, Name name) {
        this.number = number;
        this.name = name;
    }

    public void genC(PW pw) {
        if (orList != null){
            pw.print("(");
            orList.genC(pw);
            pw.print(")");
        }
        else{
            pw.print("[");
            if(number != null){
                number.genC(pw);
            }
            else if(name != null){
                name.genC(pw);
            }
            pw.print("]");
        }
    }
    
}
