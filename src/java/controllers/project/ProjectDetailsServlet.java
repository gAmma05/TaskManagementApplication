package controllers.project;

import dao.implementations.EnrollmentDAO;
import dao.implementations.ProjectDAO;
import dao.implementations.TaskDAO;
import dao.implementations.UserDAO; // Added UserDAO import
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Project;
import model.Task;
import model.User;
import services.implementations.ProjectService;
import utils.DBConnection;

public class ProjectDetailsServlet extends HttpServlet {

    private ProjectDAO projectDAO;
    private TaskDAO taskDAO;
    private EnrollmentDAO enrollmentDAO;
    private UserDAO userDAO;
    private ProjectService projectService;

    @Override
    public void init() throws ServletException {
        try {
            Connection connection = DBConnection.getConnection();
            projectDAO = new ProjectDAO(connection);
            taskDAO = new TaskDAO(connection);
            enrollmentDAO = new EnrollmentDAO(connection);
            userDAO = new UserDAO(connection);
            projectService = new ProjectService();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProjectDetailsServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user_id") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            String projectId = request.getParameter("projectId");

            if (projectId == null || projectId.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Project ID is required.");
                request.getRequestDispatcher("/view/error.jsp").forward(request, response);
                return;
            }

            // Lấy thông tin dự án và danh sách người dùng
            Project project = projectService.getProjectById(projectId);
            List<User> users = enrollmentDAO.getUsersByProjectId(projectId);
            int totalEnrollNumber = users.isEmpty() ? 1 : users.size(); // Mặc định 1 nếu không có ai

            // Đặt các thuộc tính để gửi sang JSP
            request.setAttribute("project", project);
            request.setAttribute("users", users);
            request.setAttribute("totalEnrollNumber", totalEnrollNumber);

            // Chuyển hướng đến members.jsp
            request.getRequestDispatcher("/view/project/members.jsp").forward(request, response);
        }

        // Get user role from session (assuming it's stored as a String or ENUM)
        String userRole = (String) session.getAttribute("role");
        if (userRole == null) {
            request.setAttribute("errorMessage", "User role not found in session");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        String projectId = request.getParameter("projectId");
        String tab = request.getParameter("tab");

        if (projectId == null || projectId.isEmpty()) {
            request.setAttribute("errorMessage", "Project ID is required");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        Project project = projectDAO.getProjectById(projectId);
        if (project == null) {
            request.setAttribute("errorMessage", "Project not found");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        // Always fetch project data
        request.setAttribute("project", project);

        // Fetch manager username based on managerId
        String managerUsername = "Unassigned"; // Default value

        if (project.getManagerId()
                != null && !project.getManagerId().isEmpty()) {
            User manager = userDAO.getUserById(project.getManagerId());
            if (manager != null) {
                managerUsername = manager.getUsername();
            }
        }

        request.setAttribute("managerUsername", managerUsername);

        // Base path based on user role
        String basePath;
        switch (userRole.toUpperCase()) {
            case "MANAGER":
                basePath = "/view/project/manager/";
                break;
            case "MEMBER":
                basePath = "/view/project/member/";
                break;
            case "NONE":
            default:
                basePath = "/view/project/"; // Fallback for unassigned roles or guests
                break;
        } // Determine which tab to display
        String jspPath;
        if (tab == null) {
            jspPath = basePath + "description.jsp";
        } else {
            switch (tab) {
                case "description":
                    jspPath = basePath + "description.jsp";
                    break;
                case "tasks":
                    List<Task> tasks = taskDAO.getTasksByProject(projectId);
                    request.setAttribute("tasks", tasks);
                    List<User> users = enrollmentDAO.getUsersByProjectId(projectId);
                    request.setAttribute("users", users);
                    jspPath = basePath + "tasks.jsp";
                    break;
                case "members":
                    List<User> members = enrollmentDAO.getUsersByProjectId(projectId);
                    request.setAttribute("users", members);
                    jspPath = basePath + "members.jsp";
                    break;
                default:
                    jspPath = basePath + "description.jsp"; // Default to description
                    break;
            }
        }
        request.setAttribute("tab", tab); // For navbar active state
        request.getRequestDispatcher(jspPath).forward(request, response);
    }
}
