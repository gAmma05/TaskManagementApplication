/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import enums.UserRole;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import model.User;
import utils.DBConnection;

/**
 *
 * @author gAmma
 */
public class UserDAO {

    public boolean create(User u) {
        boolean status = false;
        String sql = "INSERT INTO [User] (first_name, last_name, username, password, email, phone, role, created_at, updated_at)"
                + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, u.getFirstName());
            ps.setString(2, u.getLastName());
            ps.setString(3, u.getUsername());
            ps.setString(4, u.getPassword());
            ps.setString(5, u.getEmail());
            ps.setString(6, u.getPhone());
            ps.setInt(7, u.getRole().getValue());
            ps.setTimestamp(8, Timestamp.valueOf(u.getCreatedAt()));
            ps.setTimestamp(9, null);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                status = true;
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("DBUtils not found.");
        } catch (SQLException ex) {
            System.out.println("SQL Exception .Details: ");
            ex.printStackTrace();
        }
        return status;
    }

    public User getUser(String username, String password) {
        User u = null;
        String sql = "SELECT user_id, username, password, email, role "
                + "FROM [User] u "
                + "WHERE username = ? AND password = ?";
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs != null) {
                if (rs.next()) {
                    u = new User();
                    u.setUserId(rs.getInt("user_id"));
                    u.setUsername(rs.getString("username"));
                    u.setPassword(rs.getString("password"));
                    u.setEmail(rs.getString("email"));
                    u.setRole(rs.getInt("role") == 1 ? UserRole.MANAGER : UserRole.MEMBER);
                }
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("DBUtils not found.");
        } catch (SQLException ex) {
            System.out.println("SQL Exception. Details: ");
            ex.printStackTrace();
        }
        return u;
    }
}
