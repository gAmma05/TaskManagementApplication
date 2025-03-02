/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.implementations;

import dao.implementations.UserDAO;
import dao.interfaces.IUserDAO;
import java.sql.Connection;
import java.sql.SQLException;
import model.User;
import services.interfaces.IUserService;
import utils.DBConnection;

/**
 *
 * @author thien
 */
public class UserService implements IUserService {
    private final IUserDAO userDAO;
    private final Connection connection;

    public UserService() throws ClassNotFoundException, SQLException {
        this.connection = DBConnection.getConnection();
        this.userDAO = new UserDAO(connection);
    }

    @Override
    public User getMyInfo(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return null;
        }
        return userDAO.getUserById(userId);
    }
    
    @Override
    public String getUsername(String userId) {
        User user = userDAO.getUserById(userId);
        return user.getUsername();
    }
}
