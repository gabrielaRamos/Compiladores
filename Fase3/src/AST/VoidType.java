/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AST;

/**
 *
 * @author Gabriela Ramos
 */
public class VoidType extends Type {

    public VoidType() {
        super("void");
    }

    @Override
    public String getcName() {
        return "void";
    }
    
}
