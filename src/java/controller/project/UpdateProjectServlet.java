package controller.project;

import constants.ServletURL;
import constants.ViewURL;
import dao.implementations.ProjectDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Project;
import utils.DBConnection;

public class UpdateProjectServlet extends HttpServlet {

    // DATE_FORMAT is defined here as a private static field
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user_id") == null) {
            response.sendRedirect(ViewURL.LOGIN_PAGE);
            return;
        }

        String projectId = request.getParameter("projectId");
        String projectName = request.getParameter("projectName");
        String description = request.getParameter("description") != null ? request.getParameter("description") : "";
        String budgetStr = request.getParameter("budget");
        double budget = budgetStr != null && !budgetStr.isEmpty() ? Double.parseDouble(budgetStr) : 0.0;
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String tab = request.getParameter("tab");

        try {
            // DATE_FORMAT is used here to parse the date strings
            Date startDate = DATE_FORMAT.parse(startDateStr);
            Date endDate = DATE_FORMAT.parse(endDateStr);
            Date updatedAt = new Date();

            try (Connection conn = DBConnection.getConnection()) {
                ProjectDAO projectDAO = new ProjectDAO(conn);
                Project project = projectDAO.getProjectById(projectId);
                if (project != null) {
                    project.setProjectName(projectName);
                    project.setDescription(description);
                    project.setBudget(budget);
                    project.setStartDate(startDate);
                    project.setEndDate(endDate);
                    project.setUpdatedAt(updatedAt);
                    boolean success = projectDAO.updateProject(project);
                    if (!success) {
                        throw new SQLException("Failed to update project in database.");
                    }
                    response.sendRedirect(request.getContextPath() + "/projectDetails?projectId=" + projectId + "&tab=" + tab);
                } else {
                    request.setAttribute("errorMessage", "Project not found.");
                    request.getRequestDispatcher(request.getContextPath() + "/projectDetails?projectId=" + projectId + "&tab=" + tab).forward(request, response);
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Failed to update project due to a server error.");
                request.getRequestDispatcher(request.getContextPath() + "/projectDetails?projectId=" + projectId + "&tab=" + tab).forward(request, response);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Invalid date format.");
            request.getRequestDispatcher(request.getContextPath() + "/projectDetails?projectId=" + projectId + "&tab=" + tab).forward(request, response);
        }
    }
}
