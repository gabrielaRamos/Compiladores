/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

/**
 *
 * Atom [Details]
 */
public class AtomExpr {
    private Atom atom;
    private Details details;

    public AtomExpr(Atom atom, Details details) {
        this.atom = atom;
        this.details = details;
    }
    
    public void genC(PW pw) {
       atom.genC(pw);
       if(details != null){
           details.genC(pw);
       }
    }

    public Type getType() {
        return atom.getType();
    }
    
}
