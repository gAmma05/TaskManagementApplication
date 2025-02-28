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

    // Method to create a new user
    public boolean create(User u) {
        boolean status = false;
        String sql = "INSERT INTO `User` (first_name, last_name, username, password, email, phone, role, created_at, updated_at)"
                + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, u.getFirstName());
            ps.setString(2, u.getLastName());
            ps.setString(3, u.getUsername());
            ps.setString(4, u.getPassword());
            ps.setString(5, u.getEmail());
            ps.setString(6, u.getPhone());
            ps.setInt(7, UserRole.NONE.getValue()); // Set role as NONE by default
            ps.setTimestamp(8, Timestamp.valueOf(u.getCreatedAt()));
            ps.setTimestamp(9, null);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                status = true;
            }
            con.close();
        } catch (ClassNotFoundException ex) {
            System.out.println("DBUtils not found.");
        } catch (SQLException ex) {
            System.out.println("SQL Exception .Details: ");
            ex.printStackTrace();
        }
        return status;
    }

    // Method to update basic user information
    public boolean updateBasic(User u) {
        boolean status = false;
        String sql = "UPDATE `User` SET firstName = ?, lastName = ?, email = ?, phone = ? WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, u.getFirstName());
            ps.setString(2, u.getLastName());
            ps.setString(3, u.getEmail());
            ps.setString(4, u.getPhone());
            ps.setInt(5, u.getUserId());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                status = true;
            }
            con.close();

        } catch (ClassNotFoundException ex) {
            System.out.println("DBUtils not found.");
        } catch (SQLException ex) {
            System.out.println("SQL Exception .Details: ");
            ex.printStackTrace();
        }
        return status;
    }

    // Method to delete a user by user ID
    public boolean delete(String userID) {
        boolean status = false;
        String sql = "DELETE FROM `User` WHERE user_id = ?";
        try (Connection con = DBConnection.getConnection()) {

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, userID);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                status = true;
            }
            con.close();
        } catch (ClassNotFoundException ex) {
            System.out.println("DBUtils not found.");
        } catch (SQLException ex) {
            System.out.println("SQL Exception .Details: ");
            ex.printStackTrace();
        }
        return status;
    }

    // Method to update user password
    public boolean updatePass(User u) {
        boolean status = false;
        String sql = "UPDATE `User` SET password = ? WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, u.getPassword());
            ps.setInt(2, u.getUserId());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                status = true;
            }
            con.close();

        } catch (ClassNotFoundException ex) {
            System.out.println("DBUtils not found.");
        } catch (SQLException ex) {
            System.out.println("SQL Exception .Details: ");
            ex.printStackTrace();
        }
        return status;
    }

    // Method to set a new password for a user by username
    public boolean setNewPass(String username, String newPassword) {
        boolean status = false;
        String sql = "UPDATE `User` SET password = ? WHERE username = ?";
        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, newPassword);
            ps.setString(2, username);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                status = true;
            }

            con.close();
        } catch (ClassNotFoundException ex) {
            System.out.println("DBUtils not found.");
        } catch (SQLException ex) {
            System.out.println("SQL Exception. Details: ");
            ex.printStackTrace();
        }
        return status;
    }

    // Method to get a user by username and password
    public User getUser(String username, String password) {
        User u = null;
        String sql = "SELECT user_id, username, password, email, role "
                + "FROM `User` u "
                + "WHERE username = ? AND password = ?";
        try (Connection con = DBConnection.getConnection()) {

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
                    switch (rs.getInt("role")) {
                        case -1:
                            u.setRole(UserRole.NONE);
                            break;
                        case 0:
                            u.setRole(UserRole.MEMBER);
                            break;
                        case 1:
                            u.setRole(UserRole.MANAGER);
                            break;
                        default:
                            break;
                    }
                }
            }
            con.close();
        } catch (ClassNotFoundException ex) {
            System.out.println("DBUtils not found.");
        } catch (SQLException ex) {
            System.out.println("SQL Exception. Details: ");
            ex.printStackTrace();
        }
        return u;
    }

    // Method to get a user by username
    public User getUserByUsername(String username) {
        User u = null;
        String sql = "SELECT user_id, username, password, email "
                + "FROM `User` u "
                + "WHERE username = ?";
        try (Connection con = DBConnection.getConnection()) {

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();
            if (rs != null) {
                if (rs.next()) {
                    u = new User();
                    u.setUserId(rs.getInt("user_id"));
                    u.setUsername(rs.getString("username"));
                    u.setPassword(rs.getString("password"));
                    u.setEmail(rs.getString("email"));
                }
            }
            con.close();
        } catch (ClassNotFoundException ex) {
            System.out.println("DBUtils not found.");
        } catch (SQLException ex) {
            System.out.println("SQL Exception. Details: ");
            ex.printStackTrace();
        }
        return u;
    }

    // Method to get a user by email
    public User getUserByEmail(String email) {
        User u = null;
        String sql = "SELECT user_id, username, password "
                + "FROM `User` u "
                + "WHERE email = ?";
        try (Connection con = DBConnection.getConnection()) {

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();
            if (rs != null) {
                if (rs.next()) {
                    u = new User();
                    u.setUserId(rs.getInt("user_id"));
                    u.setUsername(rs.getString("username"));
                    u.setPassword(rs.getString("password"));
                }
            }
            con.close();
        } catch (ClassNotFoundException ex) {
            System.out.println("DBUtils not found.");
        } catch (SQLException ex) {
            System.out.println("SQL Exception. Details: ");
            ex.printStackTrace();
        }
        return u;
    }

    // Method to check if an email is already taken
    public boolean isEmailTaken(String email) {
        boolean status = false;
        String sql = "SELECT 1 FROM `User` u WHERE email = ?";

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                status = true;
            }

            con.close();
        } catch (ClassNotFoundException ex) {
            System.out.println("DBUtils not found.");
        } catch (SQLException ex) {
            System.out.println("SQL Exception. Details: ");
            ex.printStackTrace();
        }
        return status;
    }

    // Method to check if a phone number is already taken
    public boolean isPhoneTaken(String phone) {
        boolean status = false;
        String sql = "SELECT 1 FROM `User` u WHERE phone = ?";

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, phone);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                status = true;
            }

            con.close();
        } catch (ClassNotFoundException ex) {
            System.out.println("DBUtils not found.");
        } catch (SQLException ex) {
            System.out.println("SQL Exception. Details: ");
            ex.printStackTrace();
        }
        return status;
    }
}
