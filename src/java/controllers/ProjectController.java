package controllers;

import constants.URLConstants;
import dao.ProjectDAO;
import model.Project;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProjectController extends HttpServlet {

    private ProjectDAO projectDAO;
    private static final Logger LOGGER = Logger.getLogger(ProjectController.class.getName());

    @Override
    public void init() {
        projectDAO = new ProjectDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            handleListProjects(request, response);
        } else if (pathInfo.equals("/new")) {
            handleShowCreateForm(request, response);
        } else if (pathInfo.startsWith("/remove/")) {
            handleCloseProject(request, response, pathInfo);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleCloseProject(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws ServletException, IOException {
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + URLConstants.AUTH_URL);
            return;
        }

        try {
            String idStr = pathInfo.substring("/remove/".length());
            long projectId = Long.parseLong(idStr);

            projectDAO.closeProject(projectId);

            // Redirect back to projects list
            response.sendRedirect(request.getContextPath() + "/projects");
        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error closing project", e);
            request.setAttribute("error", "Error closing project: " + e.getMessage());
            handleListProjects(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid project ID");
        }
    }

    private void handleListProjects(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Integer userId = (Integer) request.getSession().getAttribute("userId");
            if (userId == null) {
                response.sendRedirect(request.getContextPath() + URLConstants.AUTH_URL);
                return;
            }
            // Get search and filter parameters
            String search = request.getParameter("search");
            String status = request.getParameter("status");
            String priority = request.getParameter("priority");
            String budget = request.getParameter("budget");

            List<Project> projects;

            // Check if any search or filter criteria are specified
            if (isAnyFilterSpecified(search, status, priority, budget)) {
                projects = projectDAO.searchUserProjects(userId, search, status, priority, budget);
            } else {
                projects = projectDAO.getUserProjects(userId);
            }

            request.setAttribute("projects", projects);
            request.getRequestDispatcher("/view/project/list.jsp").forward(request, response);
            request.setAttribute("projects", projectDAO.getUserProjects(userId));
            request.getRequestDispatcher("/view/project/list.jsp").forward(request, response);
        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Database error", e);
            throw new ServletException("Database error", e);
        }
    }

    private void handleShowCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer userId = (Integer) request.getSession().getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + URLConstants.AUTH_URL);
            return;
        }
        request.getRequestDispatcher("/view/project/create.jsp").forward(request, response);
    }

    private boolean isAnyFilterSpecified(String search, String status, String priority, String budget) {
        return (search != null && !search.trim().isEmpty())
                || (status != null && !status.trim().isEmpty())
                || (priority != null && !priority.trim().isEmpty())
                || (budget != null && !budget.trim().isEmpty());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            handleCreateProject(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleCreateProject(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Project project = createProjectFromRequest(request);
            projectDAO.saveProjectWithEnrollment(project);
            response.sendRedirect(request.getContextPath() + "/projects");
        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error creating project", e);
            request.setAttribute("error", "Error creating project: " + e.getMessage());
            request.getRequestDispatcher("/view/project/create.jsp").forward(request, response);
        }
    }

    private Project createProjectFromRequest(HttpServletRequest request) throws UnsupportedEncodingException {
        Project project = new Project();
        project.setProject_name(request.getParameter("projectName"));
        project.setDescription(request.getParameter("description"));

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            project.setStart_date(dateFormat.parse(request.getParameter("startDate")));
            project.setEnd_date(dateFormat.parse(request.getParameter("endDate")));
        } catch (ParseException e) {
            LOGGER.log(Level.SEVERE, "Error parsing date", e);
        }

        project.setPriority(Project.Priority.valueOf(request.getParameter("priority").toUpperCase()));
        project.setStatus(Project.Status.valueOf(request.getParameter("status").toUpperCase()));
        project.setBudget(Double.valueOf(request.getParameter("budget")));

        Integer userId = (Integer) request.getSession().getAttribute("userId");
        if (userId != null) {
            project.setManager_id(userId);
        } else {
            throw new IllegalStateException("User ID is not set in session");
        }

        Date now = new Date();
        project.setCreated_at(now);
        project.setUpdated_at(now);

        return project;
    }
}
