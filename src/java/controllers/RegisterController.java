/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import dao.implementations.UserDAO;
import enums.UserRole;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.User;
import validator.RegisterValidator;

/**
 *
 * @author gAmma
 */
@WebServlet(name = "RegisterController", urlPatterns = {"/RegisterController"})
public class RegisterController extends HttpServlet {
    
    private final String LOGIN_VIEW = "view/account/login.jsp";
    private final String FORGET_PASSWORD_VIEW = "view/account/forgetpassword.jsp";
    private final String SET_NEW_PASSWORD_VIEW = "view/account/setnewpassword.jsp";
    private final String REGISTER_VIEW = "view/account/register.jsp";
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action != null) {
            preRegister(request, response);
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
    
    private void preRegister(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        List<String> errorList = new ArrayList<>();

        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirm-password");

        String username = req.getParameter("username");
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");

        RegisterValidator rv = new RegisterValidator();
        UserDAO udao = new UserDAO();
        User checkU = udao.getUserByUsername(username);

        if (checkU != null) {
            boolean isDuplicatedUser = rv.duplicatedUser(username, checkU.getUsername());
            if (isDuplicatedUser) {
                errorList.add("This username already exists, try another username");
            }
        } else {
            boolean isDuplicatedEmail = rv.duplicatedEmail(email);
            boolean isDuplicatedPhoneNumber = rv.duplicatedPhoneNumber(phone);
            if (isDuplicatedEmail) {
                errorList.add("This email already exists");
            } else if (isDuplicatedPhoneNumber) {
                errorList.add("This phone number already exists");
            }
        }

        if (!errorList.isEmpty()) {
            req.setAttribute("errorList", errorList);
            req.getRequestDispatcher(REGISTER_VIEW).forward(req, resp);
            return;
        }

        boolean isInvalidFormat = !rv.regexPass(password);
        boolean isShortPass = !rv.passLength(password);
        boolean isInvalidName = !rv.invalidName(firstName, lastName);
        boolean isMismatched = !rv.duplicatedPass(password, confirmPassword);
        boolean isShortName = !rv.usernameLength(username);

        if (isInvalidName) {
            errorList.add("Invalid first name or last name (Must only contain alphabetic character)");
        }
        if (isShortName) {
            errorList.add("Username length is invalid (The length must be more than 5 characters)");
        }
        if (isMismatched) {
            errorList.add("Passwords do not match. Try again!");
        }
        if (isShortPass) {
            errorList.add("The password length must be longer than 6 characters");
        }
        if (isInvalidFormat) {
            errorList.add("Password must contain at least one uppercase letter and one special character");
        }

        req.setAttribute("username", username);
        req.setAttribute("password", password);
        req.setAttribute("firstName", firstName);
        req.setAttribute("lastName", lastName);
        req.setAttribute("email", email);
        req.setAttribute("phone", phone);

        if (!errorList.isEmpty()) {
            req.setAttribute("errorList", errorList);
            req.getRequestDispatcher(REGISTER_VIEW).forward(req, resp);
            return;
        }

        confirmRegister(req, resp);

    }

    private void confirmRegister(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");

        UserDAO udao = new UserDAO();

        User u = new User(firstName, lastName, username, password, email, phone, UserRole.NONE, -1);
        if (udao.create(u)) {
            req.setAttribute("loginGood", "Login successfully");
        } else {
            req.setAttribute("loginBad", "Failed to login");
        }
        req.getRequestDispatcher(LOGIN_VIEW).forward(req, resp);

    }

}
