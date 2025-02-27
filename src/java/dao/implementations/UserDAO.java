/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.implementations;

import enums.UserRole;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import model.User;
import utils.DatabaseUtils;

/**
 *
 * @author gAmma
 */
public class UserDAO {

    public boolean create(User u) {
        boolean status = false;
        String sql = "INSERT INTO [User] (first_name, last_name, username, password, email, phone, role, created_at, updated_at)"
                + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseUtils.getConnection()) {
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
            con.close();
        } catch (ClassNotFoundException ex) {
            System.out.println("DBUtils not found.");
        } catch (SQLException ex) {
            System.out.println("SQL Exception .Details: ");
            ex.printStackTrace();
        }
        return status;
    }

    public boolean updateBasic(User u) {
        boolean status = false;
        String sql = "UPDATE [User] SET firstName = ?, lastName = ?, email = ?, phone = ? WHERE user_id = ?";

        try (Connection con = DatabaseUtils.getConnection()) {
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

    public boolean updatePass(User u) {
        boolean status = false;
        String sql = "UPDATE [User] SET password = ? WHERE user_id = ?";

        try (Connection con = DatabaseUtils.getConnection()) {
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

    public boolean delete(String userID) {
        boolean status = false;
        String sql = "DELETE FROM [User] WHERE user_id = ?";
        try (Connection con = DatabaseUtils.getConnection()) {

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

    public User getUser(String username, String password) {
        User u = null;
        String sql = "SELECT user_id, username, password, email, role "
                + "FROM [user] u "
                + "WHERE username = ? AND password = ?";
        try (Connection con = DatabaseUtils.getConnection()) {

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
            con.close();
        } catch (ClassNotFoundException ex) {
            System.out.println("DBUtils not found.");
        } catch (SQLException ex) {
            System.out.println("SQL Exception. Details: ");
            ex.printStackTrace();
        }
        return u;
    }

    public boolean setNewPass(String username, String newPassword) {
        boolean status = false;
        String sql = "UPDATE [User] SET password = ? WHERE username = ?";
        try (Connection con = DatabaseUtils.getConnection()) {
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

    public User getUserByEmail(String email) {
        User u = null;
        String sql = "SELECT user_id, username, password "
                + "FROM [User] u "
                + "WHERE email = ?";
        try (Connection con = DatabaseUtils.getConnection()) {

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
}
