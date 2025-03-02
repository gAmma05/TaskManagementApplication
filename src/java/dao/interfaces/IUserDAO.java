/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao.interfaces;

import model.User;

/**
 *
 * @author thien
 */
public interface IUserDAO {
    public boolean create(User u);
    public boolean updateBasic(User u);
    public boolean delete(String userID);
    public boolean updatePass(User u);
    public boolean setNewPass(String username, String newPassword);
    public User getUser(String username, String password);
    public User getUserByUsername(String username);
    public User getUserByEmail(String email);
    public boolean isEmailTaken(String email);
    public boolean isPhoneTaken(String phone);
}
