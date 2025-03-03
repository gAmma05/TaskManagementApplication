package controller.project;

import constants.ServletURL;
import constants.ViewURL;
import dao.implementations.ProjectDAO;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import utils.DBConnection;

public class DeleteProjectServlet extends HttpServlet {

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
        String tab = request.getParameter("tab");
        String projectId = request.getParameter("projectId");
        try {
            ProjectDAO projectDAO = new ProjectDAO(DBConnection.getConnection());
            boolean success = projectDAO.deleteProject(projectId);
            if (!success) {
                throw new SQLException("Failed to delete project from database.");
            }
            response.sendRedirect(request.getContextPath() + ServletURL.DASHBOARD + "?tab=" + tab);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Failed to delete project due to a server error.");
            request.getRequestDispatcher(ServletURL.DASHBOARD + "?tab=" + tab).forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet to delete a project";
    }
}
