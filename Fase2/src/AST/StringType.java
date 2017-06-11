/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
*/
package AST;

public class StringType extends Type {

    public StringType() {
        super("string");
    }
    
    @Override
    public String getcName(){
        return "char";
    } 
}
