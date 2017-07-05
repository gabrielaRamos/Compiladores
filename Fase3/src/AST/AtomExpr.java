/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
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
    
    
    public Atom getAtom() {
        return atom;
    }
    
}
