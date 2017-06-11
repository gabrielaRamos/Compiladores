/*Nome: Alessia Melo    RA:620289
        Gabriela Ramos  RA:620360
*/
package AST;

import java.util.ArrayList;

public class numberExpr {

    //Number ::= [Signal] Digit{Digit} [’.’ Digit{Digit}]
    private String signal = null;
    private float valueFloat;
    private int valueInt;
    private String number;
    private int flag;

    public numberExpr(String sinal, int valueInt) {
        this.signal = sinal;
        this.valueInt = valueInt;
        this.flag = 0;
        if (signal == "-") {
            this.number = signal + valueInt;
        } else {
            this.number = valueInt + "";
        }
    }

    public numberExpr(String signal, float valueFloat, int flag) {
        this.signal = signal;
        this.valueFloat = valueFloat;
        this.flag = flag;
        if (signal == "-") {
            this.number = signal + valueFloat;
        } else {
            this.number = valueFloat + "";
        }

    }
    

    public int getNumInt() {
        
        this.valueInt = Integer.valueOf(this.number);
        return this.valueInt;
    }

    public float getNumFloat() {
       this.valueFloat = Float.valueOf(this.number);
        return this.valueFloat;
    }

    public Type getType() {
        if (this.flag == 1) {
            return Type.floatType;
        } else {
            return Type.integerType;
        }
    }

    public void genC(PW pw) {
        pw.out.print(this.number);
    }

}
