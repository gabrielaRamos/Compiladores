/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

/**
 *
 * ‘[’(Number | Name)‘]’ | ’(’ [OrList] ’)’
 */
public class Details {

    private NumberExpr number;
    private Name name;
    private OrList orList;
    private String parenteses = null;

    public Details(String parenteses, OrList orList) {
        this.orList = orList;
        this.parenteses = parenteses;
    }

    public Details(NumberExpr number, Name name) {
        this.number = number;
        this.name = name;
    }

    public void genC(PW pw) {
        if (PrintStmt.print != 1) {

            if (this.parenteses != null) {
                if (orList != null) {
                    pw.out.print("(");
                    orList.genC(pw);
                    pw.out.print(")");
                } else {
                    pw.out.print("( )");
                }
            } else {
                pw.out.print("[");
                if (number != null) {
                    number.genC(pw);
                } else if (name != null) {
                    name.genC(pw);
                }
                pw.out.print("]");
            }
        }
    }

}
