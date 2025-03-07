package controllers;

import constants.ViewURL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Project;
import model.Task;
import model.User;
import services.implementations.ProjectService;
import services.implementations.ProjectService.EnrollmentWithProject;
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

        // Fetch project lists
        List<Project> myProjects;
        Map<String, String> managerUsernames = new HashMap<>(); // Moved outside to share scope
        if ("MANAGER".equalsIgnoreCase(role)) {
            myProjects = projectService.getMyProjectsForManager(userId, role);
            request.setAttribute("myProjects", myProjects);
            List<EnrollmentWithProject> requestingEnrollments = projectService.getRequestingEnrollments(userId);
            request.setAttribute("requestingEnrollments", requestingEnrollments);

            Map<String, User> enrollmentUsers = new HashMap<>();
            for (EnrollmentWithProject req : requestingEnrollments) {
                String requestingUserId = req.getEnrollment().getUserId();
                User user = null;
                if (requestingUserId != null && !requestingUserId.isEmpty()) {
                    try {
                        user = userService.getMyInfo(requestingUserId);
                        if (user == null) {
                            user = new User();
                            user.setUsername("Unknown");
                        }
                    } catch (Exception e) {
                        LOGGER.log(Level.WARNING, "Failed to fetch user for userId: " + requestingUserId, e);
                        user = new User();
                        user.setUsername("Unknown");
                    }
                }
                enrollmentUsers.put(requestingUserId, user);
            }
            request.setAttribute("enrollmentUsers", enrollmentUsers);

            // Add manager usernames for manager's projects
            for (Project project : myProjects) {
                String managerId = project.getManagerId();
                String managerUsername = "Unassigned";
                if (managerId != null && !managerId.isEmpty()) {
                    try {
                        managerUsername = userService.getUsername(managerId);
                        if (managerUsername == null) {
                            managerUsername = "Unassigned";
                        }
                    } catch (Exception e) {
                        LOGGER.log(Level.WARNING, "Failed to fetch username for managerId: " + managerId, e);
                    }
                }
                managerUsernames.put(project.getProjectId(), managerUsername);
            }
        } else {
            myProjects = projectService.getEnrolledProjects(userId);
            request.setAttribute("myProjects", myProjects);
            List<ProjectService.ProjectWithEnrollment> pendingProjects = projectService.getPendingProjects(userId);
            request.setAttribute("pendingProjects", pendingProjects);

            // Add manager usernames for pending projects
            for (ProjectService.ProjectWithEnrollment pending : pendingProjects) {
                String managerId = pending.getProject().getManagerId();
                String managerUsername = "Unassigned";
                if (managerId != null && !managerId.isEmpty()) {
                    try {
                        managerUsername = userService.getUsername(managerId);
                        if (managerUsername == null) {
                            managerUsername = "Unassigned";
                        }
                    } catch (Exception e) {
                        LOGGER.log(Level.WARNING, "Failed to fetch username for managerId: " + managerId, e);
                    }
                }
                managerUsernames.put(pending.getProject().getProjectId(), managerUsername);
            }
        }
        List<Project> enrolledProjects = projectService.getEnrolledProjects(userId);
        request.setAttribute("enrolledProjects", enrolledProjects);

        // Add manager usernames for enrolled projects
        for (Project project : enrolledProjects) {
            String managerId = project.getManagerId();
            String managerUsername = "Unassigned";
            if (managerId != null && !managerId.isEmpty()) {
                try {
                    managerUsername = userService.getUsername(managerId);
                    if (managerUsername == null) {
                        managerUsername = "Unassigned";
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Failed to fetch username for managerId: " + managerId, e);
                }
            }
            managerUsernames.put(project.getProjectId(), managerUsername);
        }

        // Fetch tasks and map project names
        List<Task> myTasks = taskService.getMyTasks(userId);
        Map<String, String> taskProjectNames = new HashMap<>();
        for (Task task : myTasks) {
            String projectId = task.getProjectId();
            String projectName = "Unknown";
            if (projectId != null && !projectId.isEmpty()) {
                try {
                    Project project = projectService.getProjectById(projectId);
                    if (project != null) {
                        projectName = project.getProjectName();
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Failed to fetch project for projectId: " + projectId, e);
                }
            }
            taskProjectNames.put(task.getTaskId(), projectName);
        }
        request.setAttribute("myTasks", myTasks);
        request.setAttribute("taskProjectNames", taskProjectNames);
        request.setAttribute("otherProjects", projectService.getNotManagerProject(userId));
        
        List<Project> unenrolledProjects = projectService.getUnenrolledProjects(userId);
        request.setAttribute("unenrolledProjects", unenrolledProjects);
        request.setAttribute("myInfo", userService.getMyInfo(userId));

        // Add manager usernames for unenrolled projects
        for (Project project : unenrolledProjects) {
            String managerId = project.getManagerId();
            String managerUsername = "Unassigned";
            if (managerId != null && !managerId.isEmpty()) {
                try {
                    managerUsername = userService.getUsername(managerId);
                    if (managerUsername == null) {
                        managerUsername = "Unassigned";
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Failed to fetch username for managerId: " + managerId, e);
                }
            }
            managerUsernames.put(project.getProjectId(), managerUsername);
        }
        request.setAttribute("managerUsernames", managerUsernames);

        String jspPath;
        if ("MANAGER".equalsIgnoreCase(role)) {
            jspPath = "/view/dashboard/manager/";
            handleManagerTabs(request, response, tab, jspPath);
        } else if ("MEMBER".equalsIgnoreCase(role)) {
            jspPath = "/view/dashboard/member/";
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
            case "otherProjects":
                request.getRequestDispatcher(jspPath + "otherProjects.jsp").forward(request, response);
                break;
            case "myProjects":
            default:
                request.getRequestDispatcher(jspPath + "myProjects.jsp").forward(request, response);
                break;
            case "myTasks":
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
