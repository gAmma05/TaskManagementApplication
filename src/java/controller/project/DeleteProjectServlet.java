package controller.project;

import dao.implementations.ProjectDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import utils.DBConnection;

public class DeleteProjectServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user_id") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String projectId = request.getParameter("projectId");

        try (Connection conn = DBConnection.getConnection()) {
            ProjectDAO projectDAO = new ProjectDAO(conn);
            boolean success = projectDAO.deleteProject(projectId);
            if (!success) {
                throw new SQLException("Failed to delete project from database.");
            }
            response.sendRedirect(request.getContextPath() + "/dashboard");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Failed to delete project due to a server error.");
            request.getRequestDispatcher("/WEB-INF/views/manager/dashboard.jsp").forward(request, response);
        }
    }
}