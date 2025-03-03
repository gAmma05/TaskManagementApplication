/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers.auth;

import constants.ViewURL;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author thien
 */
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        // Get the current session (if it exists)
        HttpSession session = request.getSession(false); // false means don't create a new session

        if (session != null) {
            // Invalidate the session to clear all attributes
            session.invalidate();
        }

        // Redirect to the login page
        response.sendRedirect(request.getContextPath() + ViewURL.LOGIN_PAGE);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        // Handle POST requests the same way as GET (optional, depending on your needs)
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Handles user logout by invalidating the session and redirecting to login";
    }
}
