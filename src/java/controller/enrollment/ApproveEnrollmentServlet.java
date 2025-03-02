package controller.enrollment;

import dao.implementations.EnrollmentDAO;
import dao.interfaces.IEnrollmentDAO;
import enums.EnrollmentStatus;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import model.Enrollment;
import utils.DBConnection;

public class ApproveEnrollmentServlet extends HttpServlet {
    
    private final IEnrollmentDAO enrollmentDAO;
    
    public ApproveEnrollmentServlet() throws ClassNotFoundException, SQLException {
        this.enrollmentDAO = new EnrollmentDAO(DBConnection.getConnection());
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String userId = request.getParameter("userId");
        String projectId = request.getParameter("projectId");
        String managerId = request.getSession().getAttribute("user_id") != null 
                ? request.getSession().getAttribute("user_id").toString() 
                : null;
        
        if (managerId == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Manager not logged in");
            return;
        }
        
        if (userId == null || projectId == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID and Project ID are required");
            return;
        }
        
        Enrollment enrollment = enrollmentDAO.getEnrollmentByUserAndProject(userId, projectId);
        if (enrollment == null || enrollment.getStatus() != EnrollmentStatus.PENDING) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No pending enrollment found");
            return;
        }
        
        // Verify manager owns the project (optional)
        // Add logic here if needed using projectDAO
        
        enrollment.setStatus(EnrollmentStatus.ACTIVE);
        boolean success = enrollmentDAO.updateEnrollment(enrollment);
        
        if (success) {
            response.sendRedirect(request.getContextPath() + "/dashboard?tab=requesting");
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to approve enrollment");
        }
    }
}