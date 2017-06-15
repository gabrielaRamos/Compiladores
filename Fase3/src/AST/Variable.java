/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
 */
package AST;
//Type NameArray {’,’ Type NameArray}
public class Variable {

    private Type type;
    private NameArray name;

    public Variable(Type type, NameArray name) {
        this.type = type;
        this.name = name;

    }

    public Type getType() {
        return type;

    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return this.name.getNameArray();
    }

    public void setName(NameArray name) {
        this.name = name;
    }

}
