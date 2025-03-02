/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.auth;

import constants.URLConstants;
import dao.implementations.UserDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.User;
import utils.DBConnection;

/**
 *
 * @author thien
 */
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            // Validate inputs and store field-specific errors
            Map<String, String> errors = validateLogin(username, password);

            if (!errors.isEmpty()) {
                // Set each error as a request attribute
                for (Map.Entry<String, String> entry : errors.entrySet()) {
                    request.setAttribute(entry.getKey() + "Error", entry.getValue());
                }
                request.getRequestDispatcher(request.getContextPath() + URLConstants.LOGIN_PAGE).forward(request, response);
                return;
            }

            UserDAO userDAO = new UserDAO(DBConnection.getConnection());
            User user = userDAO.validateUser(username.trim(), password);

            if (user != null) {
                // Create session and store user data
                HttpSession session = request.getSession();
                session.setAttribute("user_id", user.getUserId());
                session.setAttribute("username", user.getUsername());
                session.setAttribute("role", user.getRole().name());
                session.setAttribute("full_name", user.getFullName());
                response.sendRedirect(request.getContextPath() + URLConstants.DASHBOARD_PAGE);
            } else {
                request.setAttribute("generalError", "Invalid username or password.");
                request.getRequestDispatcher(request.getContextPath() + URLConstants.LOGIN_PAGE).forward(request, response);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, "Login failed", ex);
            request.setAttribute("generalError", "A server error occurred: " + ex.getMessage());
            request.getRequestDispatcher(request.getContextPath() + URLConstants.LOGIN_PAGE).forward(request, response);
        }
    }

    // Validation method for login fields
    private Map<String, String> validateLogin(String username, String password) {
        Map<String, String> errors = new HashMap<>();

        if (username == null || username.trim().isEmpty()) {
            errors.put("username", "Username is required.");
        }

        if (password == null || password.trim().isEmpty()) {
            errors.put("password", "Password is required.");
        }

        return errors;
    }

    @Override
    public String getServletInfo() {
        return "Handles user login";
    }
}