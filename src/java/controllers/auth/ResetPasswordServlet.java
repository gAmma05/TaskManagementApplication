/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers.auth;

import constants.ViewURL;
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
import model.User;
import utils.DBConnection;

/**
 *
 * @author thien
 */
public class ResetPasswordServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        // Redirect to reset password page
        request.getRequestDispatcher(ViewURL.RESET_PASSWORD_PAGE).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        try {
            String username = request.getParameter("username");
            String newPassword = request.getParameter("newPassword");
            String confirmPassword = request.getParameter("confirmPassword");

            Map<String, String> errors = validateResetPassword(username, newPassword, confirmPassword);

            if (!errors.isEmpty()) {
                for (Map.Entry<String, String> entry : errors.entrySet()) {
                    request.setAttribute(entry.getKey() + "Error", entry.getValue());
                }
                request.getRequestDispatcher(request.getContextPath() + ViewURL.RESET_PASSWORD_PAGE).forward(request, response);
                return;
            }

            UserDAO userDAO = new UserDAO(DBConnection.getConnection());
            User user = userDAO.getUserByUsername(username.trim());

            if (user == null) {
                request.setAttribute("generalError", "Username does not exist.");
                request.getRequestDispatcher(request.getContextPath() + ViewURL.RESET_PASSWORD_PAGE).forward(request, response);
                return;
            }

            boolean success = userDAO.updatePassword(user.getUserId(), newPassword.trim());
            if (success) {
                response.sendRedirect(request.getContextPath() + ViewURL.RESET_PASSWORD_PAGE + "?success=true");
            } else {
                request.setAttribute("generalError", "Failed to reset password. Please try again.");
                request.getRequestDispatcher(ViewURL.RESET_PASSWORD_PAGE).forward(request, response);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ResetPasswordServlet.class.getName()).log(Level.SEVERE, "Password reset failed", ex);
            request.setAttribute("generalError", "A server error occurred: " + ex.getMessage());
            request.getRequestDispatcher(request.getContextPath() + ViewURL.RESET_PASSWORD_PAGE).forward(request, response);
        }
    }

    private Map<String, String> validateResetPassword(String username, String newPassword, String confirmPassword) {
        Map<String, String> errors = new HashMap<>();

        if (username == null || username.trim().isEmpty()) {
            errors.put("username", "Username is required.");
        }

        if (newPassword == null || newPassword.trim().isEmpty()) {
            errors.put("newPassword", "New password is required.");
        } else if (newPassword.length() < 6 || newPassword.length() > 50) {
            errors.put("newPassword", "Password must be between 6 and 50 characters.");
        }

        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            errors.put("confirmPassword", "Confirm password is required.");
        } else if (!newPassword.equals(confirmPassword)) {
            errors.put("confirmPassword", "Passwords do not match.");
        }

        return errors;
    }

    @Override
    public String getServletInfo() {
        return "Handles password reset functionality";
    }
}
