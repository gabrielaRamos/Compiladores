/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
*/

package AST;


import Lexer.*;
import java.io.*;

public class CompilerError {

    public CompilerError(PrintWriter out, String fileName) {
// output of an error is done in out
        this.out = out;
        this.fileName = fileName;
    }

    public void setLexer(Lexer lexer) {
        this.lexer = lexer;
    }

    public void signal(String strMessage) {
        out.println(this.fileName + ", " + lexer.getLineNumber() + ", " + strMessage );
        
        if (out.checkError()) {
            System.out.println("Error in signaling an error");
        }
        throw new RuntimeException(strMessage);
    }
    private Lexer lexer;
    PrintWriter out;
    private String fileName;
}
