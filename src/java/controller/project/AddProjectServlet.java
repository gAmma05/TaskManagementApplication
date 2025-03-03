package controller.project;

import constants.ServletURL;
import constants.ViewURL;
import dao.implementations.ProjectDAO;
import enums.ProjectStatus;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Project;
import utils.DBConnection;

public class AddProjectServlet extends HttpServlet {

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

        String userId = (String) session.getAttribute("user_id");
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
            Date currentDate = new Date();

            Project project = new Project();
            project.setProjectId(UUID.randomUUID().toString());
            project.setProjectName(projectName);
            project.setDescription(description);
            project.setManagerId(userId);
            project.setBudget(budget);
            project.setStartDate(startDate);
            project.setEndDate(endDate);
            project.setStatus(ProjectStatus.ACTIVE);
            project.setCreatedAt(currentDate);
            project.setUpdatedAt(currentDate);

            try (Connection conn = DBConnection.getConnection()) {
                ProjectDAO projectDAO = new ProjectDAO(conn);
                boolean success = projectDAO.createProject(project);
                if (!success) {
                    throw new SQLException("Failed to create project in database.");
                }
                response.sendRedirect(request.getContextPath() + ServletURL.DASHBOARD + "?tab=" + tab);
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Failed to add project due to a server error.");
                request.getRequestDispatcher(request.getContextPath() + ServletURL.DASHBOARD + "?tab=" + tab).forward(request, response);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Invalid date format.");
            request.getRequestDispatcher(request.getContextPath() + ServletURL.DASHBOARD + "?tab=" + tab).forward(request, response);
        }
    }
}
