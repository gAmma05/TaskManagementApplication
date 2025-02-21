package dao;

import model.Project;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProjectDAO {
    private static final Logger LOGGER = Logger.getLogger(ProjectDAO.class.getName());

    public void saveProject(Project project) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO project (project_name, description, start_date, end_date, " +
                    "priority, status, budget, manager_id, created_at, updated_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, project.getProject_name());
            pstmt.setString(2, project.getDescription());
            pstmt.setDate(3, new java.sql.Date(project.getStart_date().getTime()));
            pstmt.setDate(4, new java.sql.Date(project.getEnd_date().getTime()));
            pstmt.setString(5, project.getPriority().getDisplayName());
            pstmt.setString(6, project.getStatus().getDisplayName());
            pstmt.setDouble(7, project.getBudget());
            pstmt.setInt(8, project.getManager_id());
            pstmt.setTimestamp(9, new Timestamp(project.getCreated_at().getTime()));
            pstmt.setTimestamp(10, new Timestamp(project.getUpdated_at().getTime()));
            
            pstmt.executeUpdate();

            // Get the generated project_id
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                long projectId = generatedKeys.getLong(1);
                project.setProject_id(projectId);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error saving project", e);
            throw e;
        }
    }

    public void saveProjectWithEnrollment(Project project) throws SQLException, ClassNotFoundException {
        saveProject(project);

        String enrollSql = "INSERT INTO enroll (user_id, project_id, role, joined_at) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(enrollSql)) {
            
            pstmt.setInt(1, project.getManager_id());
            pstmt.setLong(2, project.getProject_id());
            pstmt.setString(3, "Manager");
            pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error saving enrollment", e);
            throw e;
        }
    }

    public List<Project> getUserProjects(Integer userId) throws SQLException, ClassNotFoundException {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT p.* FROM project p " +
                    "JOIN enroll e ON p.project_id = e.project_id " +
                    "WHERE e.user_id = ?";
                    
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Project project = new Project();
                    project.setProject_id(rs.getLong("project_id"));
                    project.setProject_name(rs.getString("project_name"));
                    project.setDescription(rs.getString("description"));
                    project.setStart_date(rs.getDate("start_date"));
                    project.setEnd_date(rs.getDate("end_date"));
                    project.setPriority(Project.Priority.valueOf(rs.getString("priority").toUpperCase()));
                    project.setStatus(Project.Status.fromDisplayName(rs.getString("status")));
                    project.setBudget(rs.getDouble("budget"));
                    project.setManager_id(rs.getInt("manager_id"));
                    project.setCreated_at(rs.getTimestamp("created_at"));
                    project.setUpdated_at(rs.getTimestamp("updated_at"));
                    
                    projects.add(project);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving user projects", e);
            throw e;
        }
        return projects;
    }
}
