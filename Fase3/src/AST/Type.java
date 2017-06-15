/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
*/
package AST;

public abstract class Type {
    private String name;
    
    public Type(String name){
        this.name = name;
    }
    
    public static Type booleanType = new BooleanType();
    public static Type integerType = new IntegerType();
    public static Type charType =  new CharType();
    public static Type floatType = new FloatType();
    public static Type stringType = new StringType();
    public static Type voidType = new VoidType();
    //verificar void type (????)
    public static Type vectorType = new VectorType();
    
    public String getName(){
        return this.name;
    }
    abstract public String getcName();
}
