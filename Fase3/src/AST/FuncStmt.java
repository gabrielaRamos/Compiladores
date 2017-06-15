/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

/**
 *
 * FuncStmt ::= Name’(’ [OrList] ’)”;’
 */
public class FuncStmt extends SimpleStmt{
    private Name name;
    private OrList orList;

    public FuncStmt(Name name, OrList orList) {
        this.name = name;
        this.orList = orList;
    }
    
    @Override
    public void genC(PW pw) {
        name.genC(pw);
        pw.print("(");
        if(orList != null){
            orList.genC(pw);
        }
        pw.print(");");
    }
    
}
