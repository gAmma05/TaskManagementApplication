package controllers.task;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dao.implementations.TaskDAO;
import enums.TaskStatus;
import model.Task;
import utils.DBConnection;

public class UpdateTaskStatusServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(UpdateTaskStatusServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        String taskId = request.getParameter("taskId");
        String status = request.getParameter("status");
        String projectId = request.getParameter("projectId");
        String tab = request.getParameter("tab");
        String source = request.getParameter("source");

        try {
            TaskDAO taskDAO = new TaskDAO(DBConnection.getConnection());
            Task task = taskDAO.getTaskById(taskId);
            if (task != null) {
                task.setStatus(TaskStatus.valueOf(status.toUpperCase()));
                taskDAO.updateTask(task);
            }

            // Determine redirect based on source
            String redirectUrl;
            if ("projectDetails".equals(source)) {
                String redirectTab = (tab != null && !tab.isEmpty()) ? tab : "tasks";
                redirectUrl = request.getContextPath() + "/projectDetails?projectId=" + projectId + "&tab=" + redirectTab;
            } else {
                String redirectTab = (tab != null && !tab.isEmpty()) ? tab : "myTasks";
                redirectUrl = request.getContextPath() + "/dashboard?tab=" + redirectTab;
            }
            response.sendRedirect(redirectUrl);
        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Failed to update task status", e);
            request.setAttribute("errorMessage", "Failed to update task status: " + e.getMessage());

            // Error redirect based on source (no /error.jsp)
            if ("projectDetails".equals(source)) {
                String redirectTab = (tab != null && !tab.isEmpty()) ? tab : "tasks";
                request.getRequestDispatcher("/projectDetails?projectId=" + projectId + "&tab=" + redirectTab).forward(request, response);
            } else {
                String redirectTab = (tab != null && !tab.isEmpty()) ? tab : "myTasks";
                request.getRequestDispatcher("/dashboard?tab=" + redirectTab).forward(request, response);
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet to update task status from dashboard or project details";
    }
}
