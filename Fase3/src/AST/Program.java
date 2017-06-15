/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
 */
package AST;

import java.util.ArrayList;

public class Program {
    // Program ::= ’program’ Name ’:’ Body ’end’

    private ArrayList<FuncDef> funcDef;

    public Program(ArrayList<FuncDef> funcDef) {
        this.funcDef = funcDef;

    }

    public void genC(PW pw) {
        pw.out.println("#include <stdio.h>");
        pw.out.println("#include <string.h>");
        pw.out.println("#include <math.h>");

        pw.out.println();
        pw.println("int main() {");

        pw.add();
        if (funcDef != null) {
            for(FuncDef f : funcDef){
                f.genC(pw);
            }
        }
        pw.out.println("");
        pw.println("return 0;");
        pw.sub();
        pw.out.println("}");
    }
}
