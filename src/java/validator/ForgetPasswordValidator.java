/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validator;

/**
 *
 * @author gAmma
 */
public class ForgetPasswordValidator {
   
    public boolean duplicatedPass(String pass, String conPass){
        return pass.equals(conPass);
    }

    public boolean regexPass(String pass){
        return pass.matches(".*[A-Z].*") && pass.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
    }
    
    public boolean passLength(String pass){
        return pass.length() <= 6;
    }
}
