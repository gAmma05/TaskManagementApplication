package controller.auth;

import constants.ServletURL;
import constants.ViewURL;
import dao.implementations.UserDAO;
import enums.UserRole;
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

public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(ViewURL.LOGIN_PAGE).forward(request, response); // Changed from redirect
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            Map<String, String> errors = validateLogin(username, password);

            if (!errors.isEmpty()) {
                for (Map.Entry<String, String> entry : errors.entrySet()) {
                    request.setAttribute(entry.getKey() + "Error", entry.getValue());
                }
                request.getRequestDispatcher(ViewURL.LOGIN_PAGE).forward(request, response);
                return;
            }

            UserDAO userDAO = new UserDAO(DBConnection.getConnection());
            User user = userDAO.validateUser(username.trim(), password);

            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user_id", user.getUserId());
                session.setAttribute("username", user.getUsername());
                session.setAttribute("role", user.getRole().name());
                session.setAttribute("full_name", user.getFullName());

                if (user.getRole().equals(UserRole.NONE)) { 
                    request.setAttribute("generalError", "You have no role to access the system.");
                    request.getRequestDispatcher(ViewURL.LOGIN_PAGE).forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath() + ServletURL.DASHBOARD);
                }
            } else {
                request.setAttribute("generalError", "Invalid username or password.");
                request.getRequestDispatcher(ViewURL.LOGIN_PAGE).forward(request, response);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, "Login failed", ex);
            request.setAttribute("generalError", "A server error occurred: " + ex.getMessage());
            request.getRequestDispatcher(ViewURL.LOGIN_PAGE).forward(request, response);
        }
    }

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