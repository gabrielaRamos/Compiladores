/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
*/

package AST;

public class Variable {

    private Type type;
    private String name;
    private int limit;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
    
    public Variable(Type type, String name, int limit) {
        this.type = type;
        this.name = name;
        this.limit = limit;
    }

    public Type getType() {
        return type;
        
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
}
