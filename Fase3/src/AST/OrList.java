/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import java.util.ArrayList;

public class OrList {

    private ArrayList<OrTest> orT;

    public OrList(ArrayList<OrTest> orT) {
        this.orT = orT;
    }

    public void genC(PW pw) {
        pw.print("[");
        for (int i = 0; i < orT.size(); i++) {

            if (i == orT.size() - 1) {
                orT.get(i).genC(pw);
            } else {
                orT.get(i).genC(pw);
                pw.print(", ");
            }

        }
         pw.print("]");

    }
}
