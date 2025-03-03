package controllers.user;

import constants.ViewURL;
import dao.implementations.EnrollmentDAO;
import dao.implementations.ProjectDAO;
import dao.implementations.TaskDAO;
import dao.implementations.UserDAO;
import enums.EnrollmentStatus;
import enums.UserRole;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Enrollment;
import model.Project;
import model.Task;
import model.User;
import utils.DBConnection;

public class UserServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(UserServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user_id") == null) {
            response.sendRedirect(request.getContextPath() + ViewURL.LOGIN_PAGE);
            return;
        }

        String userId = (String) session.getAttribute("user_id");
        System.out.println(userId);
        String role = (String) session.getAttribute("role");
        Connection conn = null;

        try {
            conn = DBConnection.getConnection();

            // Initialize DAOs
            EnrollmentDAO enrollmentDAO = new EnrollmentDAO(conn);
            ProjectDAO projectDAO = new ProjectDAO(conn);
            TaskDAO taskDAO = new TaskDAO(conn);
            UserDAO userDAO = new UserDAO(conn);

            // 1. Fetch Enrolled Projects
            List<Enrollment> enrollments = enrollmentDAO.getEnrollmentsByUser(userId);
            List<Project> enrolledProjects = new ArrayList<>();
            List<String> enrolledProjectIds = new ArrayList<>();
            for (Enrollment enrollment : enrollments) {
                Project project = projectDAO.getProjectById(enrollment.getProjectId());
                if (project != null && enrollment.getStatus().equals(EnrollmentStatus.ACTIVE)) {
                    enrolledProjects.add(project);
                    enrolledProjectIds.add(project.getProjectId());
                }
            }
            request.setAttribute("enrolledProjects", enrolledProjects);
            LOGGER.info("Enrolled Projects fetched: " + enrolledProjects.size());

            // 2. Fetch Tasks
            List<Task> myTasks = taskDAO.getAllTasks().stream()
                    .filter(task -> enrolledProjectIds.contains(task.getProjectId()))
                    .collect(Collectors.toList());
            request.setAttribute("myTasks", myTasks);
            LOGGER.info("Tasks fetched: " + myTasks.size());

            // 3. Fetch Unenrolled Projects
            List<Project> allProjects = projectDAO.getAllProjects();
            List<String> allEnrolledProjectIds = enrollments.stream()
                    .map(Enrollment::getProjectId)
                    .collect(Collectors.toList());
            List<Project> unenrolledProjects = allProjects.stream()
                    .filter(project -> !allEnrolledProjectIds.contains(project.getProjectId()))
                    .collect(Collectors.toList());
            request.setAttribute("unenrolledProjects", unenrolledProjects);
            LOGGER.info("Unenrolled Projects fetched: " + unenrolledProjects.size());

            // 4. Fetch User Info
            User user = userDAO.getUserById(userId);
            if (user == null) {
                request.setAttribute("errorMessage", "User information not found.");
                request.getRequestDispatcher(ViewURL.LOGIN_PAGE).forward(request, response);
                return;
            }
            request.setAttribute("myInfo", user);
            LOGGER.info("User Info fetched: " + user);

            // Forward to the dashboard
            if (role.equalsIgnoreCase(UserRole.MANAGER.name())) {
                request.getRequestDispatcher(ViewURL.MANAGER_DASHBOARD_PAGE).forward(request, response);
            } else if (role.equalsIgnoreCase(UserRole.MEMBER.name())) {
                request.getRequestDispatcher(ViewURL.MEMBER_DASHBOARD_PAGE).forward(request, response);
            } else {
                request.setAttribute("generalError", "You have no role to access the system.");
                request.getRequestDispatcher(ViewURL.REGISTER_PAGE).forward(request, response);
            }

        } catch (SQLException | ClassNotFoundException ex) {
            LOGGER.log(Level.SEVERE, "Failed to fetch dashboard data for user: " + userId, ex);
            request.setAttribute("errorMessage", "Unable to load dashboard data due to a server error.");
            if (role != null && role.equalsIgnoreCase(UserRole.MANAGER.name())) {
                response.sendRedirect(ViewURL.MANAGER_DASHBOARD_PAGE);
            } else if (role != null && role.equalsIgnoreCase(UserRole.MEMBER.name())) {
                response.sendRedirect(ViewURL.MEMBER_DASHBOARD_PAGE);
            } else {
                request.getRequestDispatcher(ViewURL.REGISTER_PAGE).forward(request, response);
            }
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Failed to close database connection", e);
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Fetches all dashboard data (enrolled projects, tasks, unenrolled projects, and user info) for the logged-in user";
    }
}