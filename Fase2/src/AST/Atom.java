/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
*/

package AST;

public class Atom extends Expr {

    private String atom;
    private Variable var;
    public static int flagVariable = 0;
    public int text;
    public Type type;

    public Atom(String atom, Type type) {
        this.atom = atom;
        this.type = type;
    }

    public Atom(String string, int i, Type type) {
        this.atom = string;
        this.text = i;
        this.type = type;
    }

    @Override
    public void genC(PW pw) {
        this.flagVariable = 0;
        if (PrintStmt.print == 1) {
            if (Declaration.var != null) {
                for (int i = 0; i < Declaration.var.size(); i++) {

                    if (Declaration.var.get(i).getName().equals(this.atom)) {
                        var = Declaration.var.get(i);
                        //existe variable
                        flagVariable = 1;
                    }
                }
            }
            if (flagVariable == 1) {
                if (var.getType() instanceof BooleanType || var.getType() instanceof IntegerType) {
                    pw.out.print("\"%d \"");
                } else if (var.getType() instanceof FloatType) {
                    pw.out.print("\"%f \"");
                } else if (var.getType() instanceof StringType) {
                    pw.out.print("\"%s \"");
                } else if (var.getType() instanceof CharType) {
                    pw.out.print("\"%c \"");
                }
                pw.out.print(", ");
            }
        } else {

            if (this.atom.equals("True")) {
                pw.out.print("1");
            } else if (this.atom.equals("False")) {
                pw.out.print("0");

            } else {
                if (this.text == 1) {

                    pw.out.print("\"" + this.atom + "\"");
                } else {

                    pw.out.print(this.atom);
                }

            }
        }

    }

    public Type getType() {
        return this.type;
    }

}
