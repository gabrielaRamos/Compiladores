/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

import java.util.ArrayList;

/**
 *
 * Type NameArray {’,’ Type NameArray}
 */
public class ArgList {

    private ArrayList<Variable> var;

    public ArgList(ArrayList<Variable> var) {
        this.var = var;
    }

    public void genC(PW pw) {
        if (var.size() == 1) {

            pw.print(var.get(0).getType().getcName() + " ");
            if (var.get(0).getObjNameArray().getNumber() != null) {
                pw.print(var.get(0).getObjNameArray().getNameArray());
            } else {
                pw.print(var.get(0).getName());

            }

        } else {
            for (int i = 0; i < var.size(); i++) {
                pw.print(var.get(i).getType().getcName() + " ");
                if (i == var.size() - 1) {

                    if (var.get(i).getObjNameArray().getNumber() != null) {
                        pw.print(var.get(i).getObjNameArray().getNameArray());
                    } else {
                        pw.print(var.get(i).getName());

                    }

                } else {

                    if (var.get(i).getObjNameArray().getNumber() != null) {
                        pw.print(var.get(i).getObjNameArray().getNameArray() + ", ");
                    } else {
                        pw.print(var.get(i).getName() + ", ");

                    }
                }
            }

        }
    }
}
