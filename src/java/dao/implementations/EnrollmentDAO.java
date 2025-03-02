/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.implementations;

import dao.interfaces.IEnrollmentDAO;
import enums.EnrollmentStatus;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Enrollment;

/**
 *
 * @author thien
 */
public class EnrollmentDAO implements IEnrollmentDAO {
    private final Connection connection;

    public EnrollmentDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean createEnrollment(Enrollment enrollment) {
        String sql = "INSERT INTO Enroll (user_id, project_id, joined_at, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, enrollment.getUserId());
            stmt.setString(2, enrollment.getProjectId());
            stmt.setTimestamp(3, enrollment.getJoinedAt() != null ? new Timestamp(enrollment.getJoinedAt().getTime()) : null);
            stmt.setString(4, enrollment.getStatus().name());
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(EnrollmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public Enrollment getEnrollment(String userId, String projectId) {
        String sql = "SELECT * FROM Enroll WHERE user_id = ? AND project_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userId);
            stmt.setString(2, projectId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEnrollment(rs);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(EnrollmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<Enrollment> getEnrollmentsByUser(String userId) {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT * FROM Enroll WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    enrollments.add(mapResultSetToEnrollment(rs));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(EnrollmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return enrollments;
    }

    @Override
    public List<Enrollment> getEnrollmentsByProject(String projectId) {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT * FROM Enroll WHERE project_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, projectId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    enrollments.add(mapResultSetToEnrollment(rs));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(EnrollmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return enrollments;
    }

    @Override
    public boolean updateEnrollment(Enrollment enrollment) {
        String sql = "UPDATE Enroll SET joined_at = ?, status = ? WHERE user_id = ? AND project_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, enrollment.getJoinedAt() != null ? new Timestamp(enrollment.getJoinedAt().getTime()) : null);
            stmt.setString(2, enrollment.getStatus().name());
            stmt.setString(3, enrollment.getUserId());
            stmt.setString(4, enrollment.getProjectId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(EnrollmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean deleteEnrollment(String userId, String projectId) {
        String sql = "DELETE FROM Enroll WHERE user_id = ? AND project_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userId);
            stmt.setString(2, projectId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(EnrollmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    private Enrollment mapResultSetToEnrollment(ResultSet rs) throws SQLException {
        Enrollment enrollment = new Enrollment();
        enrollment.setUserId(rs.getString("user_id"));
        enrollment.setProjectId(rs.getString("project_id"));
        Timestamp joinedAt = rs.getTimestamp("joined_at");
        enrollment.setJoinedAt(joinedAt != null ? new Date(joinedAt.getTime()) : null);
        enrollment.setStatus(EnrollmentStatus.valueOf(rs.getString("status")));
        return enrollment;
    }
}
