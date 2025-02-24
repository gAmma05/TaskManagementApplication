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
import validator.ForgetPasswordValidator;
import validator.RegisterValidator;

public class AuthController extends HttpServlet {

    private final String AUTH = "Auth";
    private final String DASHBOARD = "view/dashboard/dashboard.jsp";
    private final String LOGIN_VIEW = "view/account/login.jsp";
    private final String FORGET_PASSWORD_VIEW = "view/account/forgetpassword.jsp";
    private final String SET_NEW_PASSWORD_VIEW = "view/account/setnewpassword.jsp";
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

                case "forget-password":
                    response.sendRedirect(FORGET_PASSWORD_VIEW);
                    break;

                case "fp-confirm-email":
                    confirmEmail(request, response);
                    break;

                case "confirm-forget-password":
                    forgetPassword(request, response);
                    break;

                case "register":
                    response.sendRedirect(REGISTER_VIEW);
                    break;

                case "role-selection":
                    preRoleSelection(request, response);
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

    private void preRoleSelection(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirm-password");

        String username = req.getParameter("username");
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");

        RegisterValidator rv = new RegisterValidator();
        UserDAO udao = new UserDAO();
        User checkU = udao.getUserByEmail(email);

        if (checkU != null) {
            boolean isDuplicatedUser = rv.duplicatedUser(username, checkU.getUsername());

            if (isDuplicatedUser) {
                req.setAttribute("userDup", "This username already exists, try another username");
                req.getRequestDispatcher(REGISTER_VIEW).forward(req, resp);
                return;
            }
        }

        boolean isInvalidFormat = !rv.regexPass(password);
        boolean isInvalidPassLength = !rv.passLength(password);
        boolean isInvalidName = !rv.invalidName(firstName, lastName);
        boolean isMismatched = !rv.duplicatedPass(password, confirmPassword);
        boolean isInvalidNameLength = !rv.usernameLength(username);

        if (isInvalidName) {
            req.setAttribute("invalidName", "Invalid first name or last name (Must only contain alphabetic character).");
        } else if (isInvalidNameLength) {
            req.setAttribute("nameLength", "Username length is invalid (The length must be more than 5 characters)");
        } else if (isMismatched) {
            req.setAttribute("passDup", "Passwords do not match. Try again!");
        } else if (isInvalidPassLength) {
            req.setAttribute("passLength", "The password length must be longer than 6 characters");
        } else if (isInvalidFormat) {
            req.setAttribute("regexPass", "Password must contain at least one uppercase letter and one special character");
        }

        if (req.getAttribute("invalidName") != null || req.getAttribute("nameLength") != null
                || req.getAttribute("passDup") != null || req.getAttribute("passLength") != null || req.getAttribute("regexPass") != null) {
            req.getRequestDispatcher(REGISTER_VIEW).forward(req, resp);
            return;
        }
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

        User u = new User(firstName, lastName, username, password, email, phone, role == 1 ? UserRole.MANAGER : UserRole.MEMBER, 0);
        if (udao.create(u)) {
            req.setAttribute("loginGood", "Login successfully");
        } else {
            req.setAttribute("loginBad", "Failed to login");
        }
        req.getRequestDispatcher(LOGIN_VIEW).forward(req, resp);

//        User u = new User(username, password, firstName, lastName, email, role)
    }

    private void confirmEmail(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String email = req.getParameter("email");

        UserDAO udao = new UserDAO();
        User u = udao.getUserByEmail(email);

        if (u != null) {
            req.setAttribute("email", email);
            req.setAttribute("username", u.getUsername());

            req.getRequestDispatcher(SET_NEW_PASSWORD_VIEW).forward(req, resp);
        } else {
            req.setAttribute("emailDup", "Account not found by email. Try again!");
            req.getRequestDispatcher(FORGET_PASSWORD_VIEW).forward(req, resp);
        }
    }

    private void forgetPassword(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String email = req.getParameter("email");
        String username = req.getParameter("username");
        String newPassword = req.getParameter("new-password");
        String confirmNewPassword = req.getParameter("confirm-new-password");

        UserDAO udao = new UserDAO();
        User u = udao.getUserByEmail(email);

        ForgetPasswordValidator fpv = new ForgetPasswordValidator();

        boolean isDuplicateWithOldPass = fpv.duplicatedPass(newPassword, u.getPassword());
        boolean isMismatchedPass = !fpv.duplicatedPass(newPassword, confirmNewPassword);
        boolean isInvalidLength = !fpv.passLength(newPassword);
        boolean isInvalidFormat = !fpv.regexPass(newPassword);

        req.setAttribute("email", email);
        req.setAttribute("username", username);

        if (isDuplicateWithOldPass) {
            req.setAttribute("dupPass", "You are using the old password. Try again");
        } else if (isMismatchedPass) {
            req.setAttribute("dupPass", "Passwords do not match. Try again");
        } else if (isInvalidLength) {
            req.setAttribute("passLength", "The password length must be longer than 6 characters");
        } else if (isInvalidFormat) {
            req.setAttribute("regexPass", "Password must contain at least one uppercase letter and one special character");
        } else {
            boolean isPasswordUpdated = udao.setNewPass(username, newPassword);
            req.setAttribute(isPasswordUpdated ? "loginGood" : "loginBad",
                    isPasswordUpdated ? "New password has been set successfully" : "Failed to set new password");

            req.getRequestDispatcher(LOGIN_VIEW).forward(req, resp);
            return;
        }

        req.getRequestDispatcher(SET_NEW_PASSWORD_VIEW).forward(req, resp);

    }
}
