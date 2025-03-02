package controller.project;

import dao.implementations.EnrollmentDAO;
import dao.implementations.ProjectDAO;
import dao.implementations.TaskDAO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Project;
import model.Task;
import model.User;
import utils.DBConnection;

public class ProjectDetailsServlet extends HttpServlet {
    private ProjectDAO projectDAO;
    private TaskDAO taskDAO;
    private EnrollmentDAO enrollmentDAO;

    @Override
    public void init() throws ServletException {
        try {
            Connection connection = DBConnection.getConnection();
            projectDAO = new ProjectDAO(connection);
            taskDAO = new TaskDAO(connection);
            enrollmentDAO = new EnrollmentDAO(connection);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProjectDetailsServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
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

        // Determine which tab to display
        String jspPath;
        if (null == tab) {
            jspPath = "/view/project/description.jsp";
        } else switch (tab) {
            case "description":
                jspPath = "/view/project/description.jsp";
                break;
            case "tasks":
                {
                    List<Task> tasks = taskDAO.getTasksByProject(projectId);
                    request.setAttribute("tasks", tasks);
                    List<User> users = enrollmentDAO.getUsersByProjectId(projectId);
                    request.setAttribute("users", users); // For assignee dropdown
                    jspPath = "/view/project/tasks.jsp";
                    break;
                }
            case "members":
                {
                    List<User> users = enrollmentDAO.getUsersByProjectId(projectId);
                    request.setAttribute("users", users);
                    jspPath = "/view/project/members.jsp";
                    break;
                }
            default:
                jspPath = "/view/project/description.jsp"; // Default to description
                break;
        }

        request.setAttribute("tab", tab); // For navbar active state
        request.getRequestDispatcher(jspPath).forward(request, response);
    }
}