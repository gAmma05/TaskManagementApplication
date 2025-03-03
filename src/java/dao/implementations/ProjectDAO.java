package dao.implementations;

import dao.interfaces.IProjectDAO;
import model.Project;
import enums.ProjectStatus;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProjectDAO implements IProjectDAO {
    private final Connection connection;

    public ProjectDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean createProject(Project project) {
        String sql = "INSERT INTO Project (project_id, project_name, description, manager_id, budget, " +
                    "start_date, end_date, status, created_at, updated_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, project.getProjectId());
            stmt.setString(2, project.getProjectName());
            stmt.setString(3, project.getDescription());
            stmt.setString(4, project.getManagerId());
            stmt.setDouble(5, project.getBudget());
            stmt.setTimestamp(6, project.getStartDate() != null ? new Timestamp(project.getStartDate().getTime()) : null);
            stmt.setTimestamp(7, project.getEndDate() != null ? new Timestamp(project.getEndDate().getTime()) : null);
            stmt.setString(8, project.getStatus() != null ? project.getStatus().name() : ProjectStatus.ACTIVE.name());
            stmt.setTimestamp(9, project.getCreatedAt() != null ? new Timestamp(project.getCreatedAt().getTime()) : new Timestamp(System.currentTimeMillis()));
            stmt.setTimestamp(10, project.getUpdatedAt() != null ? new Timestamp(project.getUpdatedAt().getTime()) : new Timestamp(System.currentTimeMillis()));
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(ProjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public Project getProjectById(String projectId) {
        String sql = "SELECT * FROM Project WHERE project_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, projectId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProject(rs);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<Project> getAllProjects() {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM Project";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                projects.add(mapResultSetToProject(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return projects;
    }

    @Override
    public List<Project> getProjectsByManager(String managerId) {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM Project WHERE manager_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, managerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    projects.add(mapResultSetToProject(rs));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return projects;
    }

    @Override
    public boolean updateProject(Project project) {
        String sql = "UPDATE Project SET project_name = ?, description = ?, manager_id = ?, budget = ?, " +
                    "start_date = ?, end_date = ?, status = ?, updated_at = ? WHERE project_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, project.getProjectName());
            stmt.setString(2, project.getDescription());
            stmt.setString(3, project.getManagerId());
            stmt.setDouble(4, project.getBudget());
            stmt.setTimestamp(5, project.getStartDate() != null ? new Timestamp(project.getStartDate().getTime()) : null);
            stmt.setTimestamp(6, project.getEndDate() != null ? new Timestamp(project.getEndDate().getTime()) : null);
            stmt.setString(7, project.getStatus() != null ? project.getStatus().name() : ProjectStatus.ACTIVE.name());
            stmt.setTimestamp(8, new Timestamp(System.currentTimeMillis())); // Always update timestamp
            stmt.setString(9, project.getProjectId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(ProjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean deleteProject(String projectId) {
        String deleteEnrollSQL = "DELETE FROM Enroll WHERE project_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteEnrollSQL)) {
            stmt.setString(1, projectId);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ProjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Then, delete related tasks (if needed)
        String deleteTasksSQL = "DELETE FROM Task WHERE project_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteTasksSQL)) {
            stmt.setString(1, projectId);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ProjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Finally, delete the project
        String deleteProjectSQL = "DELETE FROM Project WHERE project_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteProjectSQL)) {
            stmt.setString(1, projectId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            Logger.getLogger(ProjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    // Helper method to map ResultSet to Project object
    private Project mapResultSetToProject(ResultSet rs) {
        try {
            Project project = new Project();
            project.setProjectId(rs.getString("project_id"));
            project.setProjectName(rs.getString("project_name"));
            project.setDescription(rs.getString("description"));
            project.setManagerId(rs.getString("manager_id"));
            project.setBudget(rs.getDouble("budget"));
            Timestamp startDate = rs.getTimestamp("start_date");
            project.setStartDate(startDate != null ? new Date(startDate.getTime()) : null);
            Timestamp endDate = rs.getTimestamp("end_date");
            project.setEndDate(endDate != null ? new Date(endDate.getTime()) : null);
            project.setStatus(ProjectStatus.valueOf(rs.getString("status")));
            Timestamp createdAt = rs.getTimestamp("created_at");
            project.setCreatedAt(createdAt != null ? new Date(createdAt.getTime()) : null);
            Timestamp updatedAt = rs.getTimestamp("updated_at");
            project.setUpdatedAt(updatedAt != null ? new Date(updatedAt.getTime()) : null);
            return project;
        } catch (SQLException ex) {
            Logger.getLogger(ProjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}