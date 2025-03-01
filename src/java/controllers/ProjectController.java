package controllers;

import constants.URLConstants;
import dao.implementations.ProjectDAO;
import model.Project;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.SQLException;
import java.util.List;

public class ProjectController extends HttpServlet {

    private ProjectDAO projectDAO;

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
        } else if (pathInfo.startsWith("/tasks/")) {
            handleViewTasks(request, response, pathInfo);
        } else if (pathInfo.startsWith("/close/")) {
            handleCloseProject(request, response, pathInfo);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // Method to list all projects with filtering
    private void handleListProjects(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Boolean isLoggedIn = (Boolean) request.getSession().getAttribute("isLoggedIn");
            if (isLoggedIn == null || !isLoggedIn) {
                response.sendRedirect(request.getContextPath() + URLConstants.AUTH_URL);
                return;
            }

            String nameFilter = request.getParameter("nameFilter");
            String budgetFilter = request.getParameter("budgetFilter");
            String priorityFilter = request.getParameter("priorityFilter");
            String statusFilter = request.getParameter("statusFilter");

            List<Project> projects = projectDAO.getFilteredProjects(nameFilter, budgetFilter, priorityFilter, statusFilter);
            for (Project project : projects) {
                int totalMembers = projectDAO.getTotalMembers(project.getProject_id());
                project.setTotalMembers(totalMembers); // Add total members to the project
            }
            request.setAttribute("projects", projects);
            request.getRequestDispatcher("/view/project/project.jsp").forward(request, response);
        } catch (SQLException | ClassNotFoundException e) {
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

    private void handleViewTasks(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws ServletException, IOException {
        try {
            // Lấy project_id từ URL (/tasks/{project_id})
            String projectIdStr = pathInfo.substring("/tasks/".length());
            int projectId = Integer.parseInt(projectIdStr);

            // Kiểm tra xem người dùng có quyền xem tasks của project này không
            Integer userId = (Integer) request.getSession().getAttribute("userId");
            if (userId == null) {
                response.sendRedirect(request.getContextPath() + URLConstants.AUTH_URL);
                return;
            }

            // Chuyển hướng đến TaskController để xử lý
            request.setAttribute("projectId", projectId);
            request.getRequestDispatcher("/Task?action=list&projectId=" + projectId).forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid project ID");
        }
    }

    private void handleCloseProject(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws ServletException, IOException {
        try {
            // Lấy project_id từ URL (/close/{project_id})
            String projectIdStr = pathInfo.substring("/close/".length());
            int projectId = Integer.parseInt(projectIdStr);

            // Kiểm tra xem người dùng có quyền đóng project này không
            Integer userId = (Integer) request.getSession().getAttribute("userId");
            if (userId == null) {
                response.sendRedirect(request.getContextPath() + URLConstants.AUTH_URL);
                return;
            }

            projectDAO.closeProject(projectId, userId);
            response.sendRedirect(request.getContextPath() + "/projects");
        } catch (NumberFormatException | SQLException | ClassNotFoundException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid project ID or database error");
        }
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
            projectDAO.saveProject(project);
            response.sendRedirect(request.getContextPath() + "/projects");
        } catch (SQLException | ParseException | ClassNotFoundException e) {
            request.setAttribute("error", "Error creating project: " + e.getMessage());
            request.getRequestDispatcher("/view/project/create.jsp").forward(request, response);
        }
    }

    private Project createProjectFromRequest(HttpServletRequest request)
            throws ParseException {
        Project project = new Project();
        project.setProject_name(request.getParameter("projectName"));
        project.setDescription(request.getParameter("description"));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        project.setStart_date(dateFormat.parse(request.getParameter("startDate")));
        project.setEnd_date(dateFormat.parse(request.getParameter("endDate")));

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
