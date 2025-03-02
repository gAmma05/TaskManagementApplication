/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import dao.implementations.UserDAO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.User;
import validator.ForgetPasswordValidator;

/**
 *
 * @author gAmma
 */
@WebServlet(name = "ForgetPasswordController", urlPatterns = {"/ForgetPasswordController"})
public class ForgetPasswordController extends HttpServlet {

    private final String LOGIN_VIEW = "view/account/login.jsp";
    private final String FORGET_PASSWORD_VIEW = "view/account/forgetpassword.jsp";
    private final String SET_NEW_PASSWORD_VIEW = "view/account/setnewpassword.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action != null) {
            switch (action) {
                case "fp-confirm-email":
                    confirmEmail(request, response);
                    break;

                case "confirm-forget-password":
                    forgetPassword(request, response);
                    break;
            }
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
        processRequest(request, response);
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
        processRequest(request, response);
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

    private void confirmEmail(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        List<String> errorList = new ArrayList<>();
        
        String email = req.getParameter("email");

        UserDAO udao = new UserDAO();
        User u = udao.getUserByEmail(email);

        if (u != null) {
            req.setAttribute("email", email);
            req.setAttribute("username", u.getUsername());

            req.getRequestDispatcher(SET_NEW_PASSWORD_VIEW).forward(req, resp);
        } else {
            errorList.add("Account not found by email. Try again!");
            req.setAttribute("errorList", errorList);
            req.getRequestDispatcher(FORGET_PASSWORD_VIEW).forward(req, resp);
        }
    }

    private void forgetPassword(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        List<String> errorList = new ArrayList<>();

        String email = req.getParameter("email");
        String username = req.getParameter("username");
        String newPassword = req.getParameter("new-password");
        String confirmNewPassword = req.getParameter("confirm-new-password");

        UserDAO udao = new UserDAO();
        User u = udao.getUserByEmail(email);

        ForgetPasswordValidator fpv = new ForgetPasswordValidator();

        boolean isDuplicateWithOldPass = fpv.duplicatedPass(newPassword, u.getPassword());
        boolean isMismatchedPass = !fpv.duplicatedPass(newPassword, confirmNewPassword);
        boolean isShortPass = fpv.passLength(newPassword);
        boolean isInvalidFormat = !fpv.regexPass(newPassword);

        if (isDuplicateWithOldPass) {
            errorList.add("You are using the old password. Try again");
        }
        if (isMismatchedPass) {
            errorList.add("Passwords do not match. Try again");
        }
        if (isShortPass) {
            errorList.add("The password length must be longer than 6 characters");
        }
        if (isInvalidFormat) {
            errorList.add("Password must contain at least one uppercase letter and one special character");
        }

        if (errorList.isEmpty()) {
            boolean isPasswordUpdated = udao.setNewPass(username, newPassword);
            req.setAttribute(isPasswordUpdated ? "loginGood" : "loginBad",
                    isPasswordUpdated ? "New password has been set successfully" : "Failed to set new password");

            req.getRequestDispatcher(LOGIN_VIEW).forward(req, resp);
        } else {
            req.setAttribute("email", email);
            req.setAttribute("username", username);

            req.setAttribute("errorList", errorList);
            req.getRequestDispatcher(SET_NEW_PASSWORD_VIEW).forward(req, resp);
        }
    }

}
