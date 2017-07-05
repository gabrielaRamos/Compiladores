/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
 */

package Aux_;


import Lexer.*;
import Lexer.Lexer;
import java.io.*;

public class CompilerError {

    public CompilerError(Lexer lexer, PrintWriter out, String fileName) {
// output of an error is done in out
        this.lexer = lexer;
        this.out = out;
        thereWasAnError = false;
    }

    public void setLexer(Lexer lexer) {
        this.lexer = lexer;
    }

    public boolean wasAnErrorSignalled() {
        return thereWasAnError;
    }

    public void show(String strMessage) {
        show(strMessage, false);
    }

    public void show(String strMessage, boolean goPreviousToken) {
// is goPreviousToken is true, the error is signalled at the line of the
// previous token, not the last one.
        if (goPreviousToken) {
            System.out.println("Error at line " + lexer.getLineNumberBeforeLastToken() + ": ");
            System.out.println(lexer.getLineBeforeLastToken());
        } else {
            System.out.println("Error at line " + lexer.getLineNumber() + ": ");
            System.out.println(lexer.getCurrentLine());
        }
        System.out.println(strMessage);
        System.out.flush();
        if (System.out.checkError()) {
            System.out.println("Error in signaling an error");
        }
        thereWasAnError = true;
    }

    public void signal(String strMessage) {
        show(strMessage);
        System.out.flush();
        thereWasAnError = true;
        
        throw new RuntimeException();
    }
    private Lexer lexer;
    private PrintWriter out;
    private boolean thereWasAnError;
}
