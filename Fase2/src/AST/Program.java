/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
*/
package AST;

public class Program {
   // Program ::= ’program’ Name ’:’ Body ’end’
    private Body body;
   
    public Program(Body body ) {
        this.body = body;
   
    }
    
    public void genC(PW pw) {
        pw.out.println("#include <stdio.h>");        
        pw.out.println("#include <string.h>");
        pw.out.println("#include <math.h>");
        
        pw.out.println();
        pw.println("int main() {");
        
        pw.add();
        body.genC(pw);
        pw.out.println("");        
        pw.println("return 0;");
        pw.sub();
        pw.out.println("}");
    }
}
