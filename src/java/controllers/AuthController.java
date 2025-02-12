package controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;

import dao.UserDAO;
import enums.UserRole;
import model.User;

public class AuthController extends HttpServlet {

    private final String AUTH = "Auth";
    private final String DASHBOARD = "view/dashboard/dashboard.jsp";
    private final String LOGIN_VIEW = "view/account/login.jsp";
    private final String REGISTER_VIEW = "view/account/register.jsp";
    private final String ROLE_SELECT_VIEW = "view/account/roleselect.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);
        if (action != null) {
            switch (action) {
                case "login":
                    login(request, response);
                    break;

                case "register":
                    request.getRequestDispatcher(REGISTER_VIEW).forward(request, response);
                    break;

                case "role-selection":
                    roleSelection(request, response);
                    break;

                case "confirm-register":
                    register(request, response);
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
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        UserDAO ud = new UserDAO();

        User ulog = ud.getUser(username, password);

        if (ulog != null) {
            HttpSession session = req.getSession();
            session.setAttribute("username", username);
            session.setAttribute("isLoggedIn", true);
            session.setAttribute("role", ulog.getRole());
            
            req.getRequestDispatcher(DASHBOARD).forward(req, resp);
            
        } else {
            req.setAttribute("error", "Wrong username or password");
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

    private void roleSelection(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirm-password");

        if (!password.equals(confirmPassword)) {
            req.setAttribute("passDup", "Passwords do not match. Try again!");
            req.getRequestDispatcher(REGISTER_VIEW).forward(req, resp);
            return;
        }
        String username = req.getParameter("username");
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");

        req.setAttribute("username", username);
        req.setAttribute("password", password);
        req.setAttribute("firstName", firstName);
        req.setAttribute("lastName", lastName);
        req.setAttribute("email", email);
        req.setAttribute("phone", phone);

        req.getRequestDispatcher(ROLE_SELECT_VIEW).forward(req, resp);

    }

    private void register(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
//        String departmentName = req.getParameter("department");

        String roleString = req.getParameter("selected");
        int role = 0;
        if (roleString != null) {
            if (roleString.equalsIgnoreCase("manager")) {
                role = 1;
            } else if (roleString.equalsIgnoreCase("member")) {
                role = 0;
            }
        } else {
            return;
        }

        UserDAO udao = new UserDAO();
        User checkU = udao.getUser(username, password);
        if (checkU != null) {
            req.setAttribute("error", "This username already exists, try another username");
            req.getRequestDispatcher(REGISTER_VIEW).forward(req, resp);
        } else {
            User u = new User(firstName, lastName, username, password, email, phone, role == 1 ? UserRole.MANAGER : UserRole.MEMBER, 0);
            if (udao.create(u)) {
                req.setAttribute("loginGood", "Login successfully");
            } else {
                req.setAttribute("loginBad", "Failed to login");
            }
            req.getRequestDispatcher(LOGIN_VIEW).forward(req, resp);
        }

//        User u = new User(username, password, firstName, lastName, email, role)
    }
}
