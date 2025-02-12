/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

/**
 *
 * @author vothimaihoa
 */

import constants.URLConstants;
import model.Project;
import dao.ProjectDAO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

        if (pathInfo == null || pathInfo.equals(URLConstants.PROJECT_LIST_URL)) {
            handleListProjects(request, response);
        } else if (pathInfo.equals(URLConstants.PROJECT_CREATE_URL)) {
            handleShowCreateForm(request, response);
        }
    }

    private void handleListProjects(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Long userId = (Long) request.getSession().getAttribute("userId");
            if (userId == null) {
                response.sendRedirect(request.getContextPath() + URLConstants.AUTH_URL);
                return;
            }
            request.setAttribute("projects", projectDAO.getUserProjects(userId));
            request.getRequestDispatcher(URLConstants.PROJECT_LIST_VIEW).forward(request, response);
        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException("Database error", e);
        }
    }

    private void handleShowCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher(URLConstants.PROJECT_CREATE_VIEW).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo.equals(URLConstants.PROJECT_CREATE_URL)) {
            handleCreateProject(request, response);
        }
    }

    private void handleCreateProject(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Project project = createProjectFromRequest(request);
            projectDAO.saveProject(project);
            response.sendRedirect(request.getContextPath() + URLConstants.PROJECTS_URL);
        } catch (SQLException | ParseException | ClassNotFoundException e) {
            request.setAttribute("error", "Error creating project: " + e.getMessage());
            request.getRequestDispatcher(URLConstants.PROJECT_CREATE_VIEW).forward(request, response);
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

        project.setPriority(Project.Priority.valueOf(request.getParameter("priority")));
        project.setStatus(Project.Status.NOT_STARTED);
        project.setBudget(Double.valueOf(request.getParameter("budget")));

        Long userId = (Long) request.getSession().getAttribute("userId");
        project.setManager_id(userId);

        Date now = new Date();
        project.setCreated_at(now);
        project.setUpdated_at(now);

        return project;
    }
}
