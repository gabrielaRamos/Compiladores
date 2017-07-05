/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
*/
package AST;

/**
 *
 * Name[‘[’Number‘]’]
 */
public class NameArray {
   private Name name;
   private NumberExpr number;

    public NameArray(Name name, NumberExpr number) {
        this.name = name;
        this.number = number;
    }
    
    public void genC(PW pw){
        name.genC(pw);
        if (number != null){
            pw.out.print("[");
            number.genC(pw);
            pw.out.print("]");
        }
    }
    
    public String getNameArray(){
        if(number != null){
            return (name.getName() + "[" + number.getNumInt() + "]");
        }
        else{
            return name.getName();
        }
    }
    
    public String getName() {
        return name.getName();
    }

    public NumberExpr getNumber() {
        return number;
    }
}
