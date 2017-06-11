/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
*/
package AST;

public class IntegerType extends Type {

    public IntegerType() {
        super("integer");
    }

    @Override
    public String getcName() {
        return "int";
    }
    
}
