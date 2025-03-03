package controllers.task;

import dao.implementations.TaskDAO;
import enums.TaskPriority;
import enums.TaskStatus;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Task;
import utils.DBConnection;

public class CreateTaskServlet extends HttpServlet {
    private TaskDAO taskDAO;

    @Override
    public void init() throws ServletException {
        try {
            taskDAO = new TaskDAO(DBConnection.getConnection());
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(CreateTaskServlet.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServletException("Failed to initialize TaskDAO", ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        Task task = new Task();
        task.setTaskId(UUID.randomUUID().toString());
        task.setProjectId(request.getParameter("projectId"));
        task.setTaskName(request.getParameter("taskName"));
        task.setDescription(request.getParameter("description"));
        task.setMemberId(request.getParameter("memberId")); // Changed from assignerId
        task.setPriority(TaskPriority.valueOf(request.getParameter("priority")));
        
        try {
            Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("deadline"));
            task.setDeadline(new java.sql.Date(utilDate.getTime()));
        } catch (ParseException ex) {
            Logger.getLogger(CreateTaskServlet.class.getName()).log(Level.SEVERE, "Invalid deadline format", ex);
            request.setAttribute("errorMessage", "Invalid deadline format");
            request.getRequestDispatcher("/view/project/tasks.jsp").forward(request, response);
            return;
        }

        Date now = new Date();
        task.setCreatedAt(new java.sql.Date(now.getTime()));
        task.setUpdatedAt(new java.sql.Date(now.getTime()));
        task.setStatus(TaskStatus.PENDING); // Adjust per your enum

        taskDAO.createTask(task);
        String tab = request.getParameter("tab");
        response.sendRedirect(request.getContextPath() + "/projectDetails?projectId=" + task.getProjectId() + "&tab=" + tab);
    }

    @Override
    public String getServletInfo() {
        return "Creates a new task for a project";
    }
}