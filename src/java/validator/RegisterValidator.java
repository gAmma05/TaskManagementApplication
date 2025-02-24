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
public class RegisterValidator {

    public boolean usernameLength(String username) {
        return username.length() > 5;
    }

    public boolean invalidName(String firstName, String lastName) {
        return firstName.matches("[a-zA-Z ]+") && lastName.matches("[a-zA-Z ]+");
    }

    public boolean duplicatedUser(String username, String dupUsername) {
        return username.equals(dupUsername);
    }

    public boolean duplicatedPass(String pass, String conPass) {
        return pass.equals(conPass);
    }

    public boolean regexPass(String pass) {
        return pass.matches(".*[A-Z].*") && pass.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
    }

    public boolean passLength(String pass) {
        return pass.length() > 6;
    }
}
