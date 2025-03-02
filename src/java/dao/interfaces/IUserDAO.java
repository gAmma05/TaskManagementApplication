/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao.interfaces;

import java.util.List;
import model.User;

/**
 *
 * @author thien
 */
public interface IUserDAO {

    User validateUser(String username, String password);

    boolean createUser(User user);

    User getUserById(String userId);

    User getUserByUsername(String username);

    boolean updateUser(User user);

    boolean updatePassword(String userId, String newPassword);

    boolean deleteUser(String userId);

    boolean isEmailTaken(String email);

    boolean isPhoneTaken(String phone);

    List<User> getAllUsers();
}
