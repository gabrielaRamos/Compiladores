/*
*/

import AST.*;
import java.util.*;


public class Compiler {

    public Program compile( char []p_input  ) {
        input = p_input;
        tokenPos = 0;
        nextToken();
        Program result = program();
        if ( token != '\0' )
          error();
        return result;
        }

    //Program ::= ’P’ Name ’:’ Body ’E’
    private Program program() {
      if (token == 'P'){
        nextToken();
        
      } 
      else
        error();
      return new Program ();
    }
    
    private Body body(){
        
    }
    
    private Declaration declaration (){
        
    }
    
    private Stmt stmt (){
        
    }
    
    private SimpleStmt simplestmt(){
        
    }
    private ExprStmt exprstmt(){
        
    }
    private PrintStmt printstmt(){
        
    }
    private BreakStmt breakstmt (){
        
    }
    private CompoundStmt compoundstmt (){
        
    }
    private IfStmt ifstmt(){
        
    }
    private WhileStmt whilestmt(){
        
    }
    private Comparison comparison(){
        
    }
    private CompOp compop(){
        
    }
    private Expr expr(){
        
    }
    private Term term (){
        
    }
    private Factor factor(){
        
    }
    private Atom atom(){
        
    }
    private Type type(){
        
    }
    private Name name(){
        
    }
    private Number number(){
        
    }
    private String string(){
        
    }
    private char letter(){
        
    }
    private char Digit(){
        
    }
    private void nextToken() {
        System.out.println(token);
        while (  tokenPos < input.length && input[tokenPos] == ' ' ) 
          tokenPos++;
        if ( tokenPos < input.length ) {
          token = input[tokenPos];        
          tokenPos++;
        }
        else
          token = '\0';
        
    }
    
    private void error() {
        if ( tokenPos == 0 ) 
          tokenPos = 1; 
        else 
          if ( tokenPos >= input.length )
            tokenPos = input.length - 1;
        
        String strInput = new String( input, tokenPos - 1, input.length - tokenPos + 1 );
        String strError = "Error at \"" + strInput + "\"";
        System.out.print( strError );
        throw new RuntimeException(strError);
    }
    
    private char token;
    private int  tokenPos;
    private char []input;
    
}
