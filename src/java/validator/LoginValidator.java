/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validator;

import dao.UserDAO;
import model.User;

/**
 *
 * @author gAmma
 */
public class LoginValidator {
    UserDAO udao = new UserDAO();
    
    public boolean validAccount(User u){
        return u != null;
    }
}
