/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

/**
 *
 * ReturnStmt ::= ’return’ [OrTest]’;’
 */
public class ReturnStmt extends SimpleStmt{
    private orTest orT;
    
    public ReturnStmt(orTest orT){
        this.orT =  orT;
    }
    
    public void genC(PW pw){
        
        pw.print("return ");
        if (orT != null){
            orT.genC(pw);
        }
        pw.print(";");
    }
}

