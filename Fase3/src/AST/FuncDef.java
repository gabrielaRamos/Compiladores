/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

/**
 *
 * ’def’ Name ’(’ [ArgsList] ’)’ : Type ’{’Body’}’
 */
public class FuncDef {
    private Type type;
    private Body body;
    private Name name;
    private ArgList argList;

    public FuncDef(Type type, Body body, Name name, ArgList argList) {
        this.type = type;
        this.body = body;
        this.name = name;
        this.argList = argList;
        
    }
    
   public void genC(PW pw) {
      
       pw.print(type.getcName() + " " + name.getName() + "(");
       if (argList != null){
           argList.genC(pw);
       }
       pw.print("){");
       pw.println("");
       pw.add();
       body.genC(pw);
       pw.sub();
       pw.print("}");
    }
    
   public Type getType(){
       return type;
   }
}
