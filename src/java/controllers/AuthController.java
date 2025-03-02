package controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;

import dao.implementations.UserDAO;
import java.util.ArrayList;
import java.util.List;
import validator.LoginValidator;

public class AuthController extends HttpServlet {

    private final String AUTH = "Auth";
    private final String DASHBOARD = "view/dashboard/dashboard.jsp";
    private final String LOGIN_VIEW = "view/account/login.jsp";
    private final String FORGET_PASSWORD_VIEW = "view/account/forgetpassword.jsp";
    private final String REGISTER_VIEW = "view/account/register.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);
        if (action != null) {
            switch (action) {
                case "login":
                    login(request, response);
                    break;

                case "forget-password":
                    response.sendRedirect(FORGET_PASSWORD_VIEW);
                    break;

                case "register":
                    response.sendRedirect(REGISTER_VIEW);
                    break;

                case "logout":
                    logout(request, response);
                    break;

                default:
                    if (session != null && session.getAttribute("isLoggedIn") != null) {
                        request.getRequestDispatcher(DASHBOARD).forward(request, response);
                    } else {
                        request.getRequestDispatcher(LOGIN_VIEW).forward(request, response);
                    }
                    break;
            }
        } else {
            if (session != null && session.getAttribute("isLoggedIn") != null) {
                request.getRequestDispatcher(DASHBOARD).forward(request, response);
            } else {
                request.getRequestDispatcher(LOGIN_VIEW).forward(request, response);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void login(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        List<String> errorList = new ArrayList<>();

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        UserDAO ud = new UserDAO();

        LoginValidator lv = new LoginValidator();

        if (lv.validAccount(ud.getUser(username, password))) {
            HttpSession session = req.getSession();
            session.setAttribute("username", username);
            session.setAttribute("isLoggedIn", true);
            session.setAttribute("role", ud.getUser(username, password).getRole());
            session.setAttribute("fullName", ud.getUser(username, password).getFullName());

            req.getRequestDispatcher(DASHBOARD).forward(req, resp);
        } else {

            errorList.add("Wrong username or password");
            req.setAttribute("errorList", errorList);
            req.setAttribute("username", username);
            req.getRequestDispatcher(LOGIN_VIEW).forward(req, resp);
        }
    }

    private void logout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        resp.sendRedirect(AUTH);
    }
}
