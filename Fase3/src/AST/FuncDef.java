/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
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

    public FuncDef(Type type, Name name) {
       this.type = type;
        this.name = name;
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
       pw.println("");
    }
    
   public Type getType(){
       return type;
   }
}
