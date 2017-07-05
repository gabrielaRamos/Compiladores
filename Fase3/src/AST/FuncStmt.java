/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
 */
package AST;

/**
 *
 * FuncStmt ::= Name’(’ [OrList] ’)”;’
 */
public class FuncStmt extends SimpleStmt {

    private Name name;
    private OrList orList;

    public FuncStmt(Name name, OrList orList) {
        this.name = name;
        this.orList = orList;
    }

    @Override
    public void genC(PW pw) {

        name.genC(pw);
        pw.out.print("(");
        if (orList != null) {
            orList.genC(pw);
        }
        pw.out.print(");");
        pw.println("");

    }

}
