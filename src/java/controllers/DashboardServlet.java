package controllers;

import constants.ViewURL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import services.implementations.ProjectService;
import services.implementations.TaskService;
import services.implementations.UserService;
import services.interfaces.IProjectService;
import services.interfaces.ITaskService;
import services.interfaces.IUserService;

public class DashboardServlet extends HttpServlet {
    
    private static final Logger LOGGER = Logger.getLogger(DashboardServlet.class.getName());
    private final IProjectService projectService;
    private final ITaskService taskService;
    private final IUserService userService;
    
    public DashboardServlet() {
        try {
            this.projectService = new ProjectService();
            this.taskService = new TaskService();
            this.userService = new UserService();
        } catch (ClassNotFoundException | SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize services", e);
            throw new RuntimeException("Failed to initialize services", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        String userId = (String) session.getAttribute("user_id");
        String role = (String) session.getAttribute("role");

        if (userId == null || role == null) {
            response.sendRedirect(ViewURL.LOGIN_PAGE);
            return;
        }

        if ("NONE".equalsIgnoreCase(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have access to the system");
            return;
        }

        String tab = request.getParameter("tab");
        if (tab == null || tab.isEmpty()) {
            tab = "myProjects";
        }
        
        if ("MANAGER".equalsIgnoreCase(role)) {
            request.setAttribute("myProjects", projectService.getMyProjectsForManager(userId, role));
            request.setAttribute("requestingEnrollments", projectService.getRequestingEnrollments(userId));
        } else {
            request.setAttribute("myProjects", projectService.getEnrolledProjects(userId));
            request.setAttribute("pendingProjects", projectService.getPendingProjects(userId));
        }
        request.setAttribute("enrolledProjects", projectService.getEnrolledProjects(userId));
        request.setAttribute("myTasks", taskService.getMyTasks(userId));
        request.setAttribute("unenrolledProjects", projectService.getUnenrolledProjects(userId));
        request.setAttribute("myInfo", userService.getMyInfo(userId));

        String jspPath;
        if ("MANAGER".equalsIgnoreCase(role)) {
            jspPath = "/view/manager/";
            handleManagerTabs(request, response, tab, jspPath);
        } else if ("MEMBER".equalsIgnoreCase(role)) {
            jspPath = "/view/member/";
            handleMemberTabs(request, response, tab, jspPath);
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid user role: " + role);
        }
    }

    private void handleManagerTabs(HttpServletRequest request, HttpServletResponse response, 
                                 String tab, String jspPath) 
            throws ServletException, IOException {
        switch (tab) {
            case "myInfo":
                request.getRequestDispatcher(jspPath + "myInfo.jsp").forward(request, response);
                break;
            case "requesting":
                request.getRequestDispatcher(jspPath + "requesting.jsp").forward(request, response);
                break;
            case "myProjects":
            default:
                request.getRequestDispatcher(jspPath + "myProjects.jsp").forward(request, response);
                break;
            case "myTasks":
            case "otherProjects":
            case "pendingProjects":
                response.sendError(HttpServletResponse.SC_FORBIDDEN, 
                        "Tab not available for MANAGER role");
                break;
        }
    }
    
    private void handleMemberTabs(HttpServletRequest request, HttpServletResponse response, 
                                String tab, String jspPath) 
            throws ServletException, IOException {
        switch (tab) {
            case "myTasks":
                request.getRequestDispatcher(jspPath + "myTasks.jsp").forward(request, response);
                break;
            case "otherProjects":
                request.getRequestDispatcher(jspPath + "otherProjects.jsp").forward(request, response);
                break;
            case "myInfo":
                request.getRequestDispatcher(jspPath + "myInfo.jsp").forward(request, response);
                break;
            case "pendingProjects":
                request.getRequestDispatcher(jspPath + "pendingProjects.jsp").forward(request, response);
                break;
            case "myProjects":
            default:
                request.getRequestDispatcher(jspPath + "myProjects.jsp").forward(request, response);
                break;
            case "requesting":
                response.sendError(HttpServletResponse.SC_FORBIDDEN, 
                        "Tab not available for MEMBER role");
                break;
        }
    }

}