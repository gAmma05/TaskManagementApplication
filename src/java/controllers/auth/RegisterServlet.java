/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers.auth;

import constants.ServletURL;
import constants.ViewURL;
import dao.implementations.UserDAO;
import enums.UserRole;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.User;
import utils.DBConnection;
import validator.UserValidator;

/**
 *
 * @author thien
 */
public class RegisterServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet RegisterServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RegisterServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + ViewURL.REGISTER_PAGE);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String fullName = request.getParameter("fullname");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone_number");
            String role = request.getParameter("role");

            UserDAO userDAO = new UserDAO(DBConnection.getConnection());
            UserValidator userValidator = new UserValidator(userDAO);

            Map<String, String> errors = userValidator.validate(username, password, fullName, email, phone);
            if (!errors.isEmpty()) {
                // Set each error as a separate request attribute
                for (Map.Entry<String, String> entry : errors.entrySet()) {
                    request.setAttribute(entry.getKey() + "Error", entry.getValue());
                }
                // Forward back to the form with errors
                request.getRequestDispatcher(ViewURL.REGISTER_PAGE).forward(request, response);
                return;
            }

            User newUser = new User(
                    username.trim(),
                    password, 
                    fullName.trim(),
                    email.trim(),
                    phone != null ? phone.trim() : null,
                    UserRole.valueOf(role.toUpperCase())
            );

            boolean success = userDAO.createUser(newUser);
            if (success) {
                request.getSession().setAttribute("user_id", newUser.getUserId());
                request.getSession().setAttribute("username", newUser.getUsername());
                request.getSession().setAttribute("role", newUser.getRole().name());
                request.getSession().setAttribute("full_name", newUser.getFullName());
                if (role == null || role.equalsIgnoreCase(UserRole.NONE.name())) { 
                    request.setAttribute("generalError", "You have no role to access the system.");
                    request.getRequestDispatcher(ViewURL.REGISTER_PAGE).forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath() + ServletURL.DASHBOARD);
                }
            } else {
                request.setAttribute("generalError", "Registration failed due to a server error. Please try again.");
                request.getRequestDispatcher(ViewURL.REGISTER_PAGE).forward(request, response);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(RegisterServlet.class.getName()).log(Level.SEVERE, "Registration failed", ex);
            request.setAttribute("generalError", "A server error occurred: " + ex.getMessage());
            request.getRequestDispatcher(ViewURL.REGISTER_PAGE).forward(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
