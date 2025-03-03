package controllers.task;

import dao.implementations.TaskDAO;
import enums.TaskPriority;
import enums.TaskStatus;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Task;
import utils.DBConnection;

public class UpdateTaskServlet extends HttpServlet {

    private TaskDAO taskDAO;

    @Override
    public void init() throws ServletException {
        try {
            taskDAO = new TaskDAO(DBConnection.getConnection());
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(UpdateTaskServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        try {
            Task task = new Task();
            task.setTaskId(request.getParameter("taskId"));
            task.setProjectId(request.getParameter("projectId"));
            task.setTaskName(request.getParameter("taskName"));
            task.setDescription(request.getParameter("description"));
            task.setMemberId(request.getParameter("memberId"));
            task.setPriority(TaskPriority.valueOf(request.getParameter("priority")));
            task.setStatus(TaskStatus.valueOf(request.getParameter("status")));

            // Convert java.util.Date to java.sql.Date for deadline
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilDate = sdf.parse(request.getParameter("deadline"));
            task.setDeadline(new java.sql.Date(utilDate.getTime()));

            // Set updatedAt to current date as java.sql.Date
            task.setUpdatedAt(new java.sql.Date(System.currentTimeMillis()));

            taskDAO.updateTask(task);
            String tab = request.getParameter("tab");
            response.sendRedirect(request.getContextPath() + "/projectDetails?projectId=" + task.getProjectId() + "&tab=" + tab);
        } catch (ParseException ex) {
            Logger.getLogger(UpdateTaskServlet.class.getName()).log(Level.SEVERE, "Error parsing date", ex);
            request.setAttribute("errorMessage", "Invalid date format.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (Exception ex) {
            Logger.getLogger(UpdateTaskServlet.class.getName()).log(Level.SEVERE, "Unexpected error", ex);
            request.setAttribute("errorMessage", "An unexpected error occurred.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet to update task details";
    }
}
