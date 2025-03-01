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
import model.User;
import utils.DBConnection;

/**
 *
 * @author gAmma
 */
public class UserDAO implements IUserDAO {

    @Override
    public boolean create(User u) {
        boolean status = false;
        String sql = "INSERT INTO User (user_id, full_name, username, password, email, phone_number, role) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, u.getUserId());
            ps.setString(2, u.getFullName());
            ps.setString(3, u.getUsername());

            ps.setString(4, u.getPassword());
            ps.setString(5, u.getEmail());
            ps.setString(6, u.getPhone());
            ps.setString(7, u.getRole().getValue());

            int rows = ps.executeUpdate();
            status = rows > 0;

        } catch (ClassNotFoundException ex) {
            System.out.println("DBUtils not found.");
        } catch (SQLException ex) {
            System.out.println("SQL Exception. Details: ");
            ex.printStackTrace();
        }
        return status;
    }

    @Override
    public boolean updateBasic(User u) {
        boolean status = false;
        String sql = "UPDATE User SET full_name = ?, email = ?, phone = ? WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, u.getFullName());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPhone());
            ps.setString(4, u.getUserId());

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

    @Override
    public boolean delete(String userID) {
        boolean status = false;
        String sql = "DELETE FROM User WHERE user_id = ?";
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

    @Override
    public boolean updatePass(User u) {
        boolean status = false;
        String sql = "UPDATE User SET password = ? WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, u.getPassword());
            ps.setString(2, u.getUserId());

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

    @Override
    public boolean setNewPass(String username, String newPassword) {
        boolean status = false;
        String sql = "UPDATE User SET password = ? WHERE username = ?";
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

    @Override
    public User getUser(String username, String password) {
        User u = null;
        String sql = "SELECT user_id, full_name, username, password, email, role "
                + "FROM User u "
                + "WHERE username = ? AND password = ?";
        try (Connection con = DBConnection.getConnection()) {

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs != null) {
                if (rs.next()) {
                    u = new User();
                    u.setUserId(rs.getString("user_id"));
                    u.setFullName(rs.getString("full_name"));
                    u.setUsername(rs.getString("username"));
                    u.setPassword(rs.getString("password"));
                    u.setEmail(rs.getString("email"));
                    switch (rs.getString("role")) {
                        case "NONE":
                            u.setRole(UserRole.NONE);
                            break;
                        case "MEMBER":
                            u.setRole(UserRole.MEMBER);
                            break;
                        case "MANAGER":
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

    @Override
    public User getUserByUsername(String username) {
        User u = null;
        String sql = "SELECT user_id, full_name, username, password, email FROM User WHERE username = ?";

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                u = new User();
                u.setUserId(rs.getString("user_id"));
                u.setFullName(rs.getString("full_name"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setEmail(rs.getString("email"));
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("DBUtils not found.");
        } catch (SQLException ex) {
            System.out.println("SQL Exception. Details: ");
            ex.printStackTrace();
        }
        return u;
    }

    @Override
    public User getUserByEmail(String email) {
        User u = null;
        String sql = "SELECT user_id, username, password "
                + "FROM User u "
                + "WHERE email = ?";
        try (Connection con = DBConnection.getConnection()) {

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);

            ResultSet rs = ps.executeQuery();
            if (rs != null) {
                if (rs.next()) {
                    u = new User();
                    u.setUserId(rs.getString("user_id"));
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

    @Override
    public boolean isEmailTaken(String email) {
        boolean status = false;
        String sql = "SELECT 1 FROM User u WHERE email = ?";

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

    @Override
    public boolean isPhoneTaken(String phone) {
        boolean status = false;
        String sql = "SELECT 1 FROM User u WHERE phone_number = ?";

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
