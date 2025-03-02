package dao.implementations;

import dao.interfaces.IProjectDAO;
import enums.ProjectStatus;
import enums.UserRole;
import model.Project;
import utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProjectDAO implements IProjectDAO {

    @Override
    public void saveProject(Project project) {
        String sql = "INSERT INTO Project (project_id, project_name, description, manager_id, start_date, "
                + "end_date, status, budget, created_at, updated_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, project.getProjectId());
            pstmt.setString(2, project.getProjectName());
            pstmt.setString(3, project.getDescription());
            pstmt.setString(4, project.getManagerId());
            pstmt.setDate(5, new java.sql.Date(project.getStartDate().getTime()));
            pstmt.setDate(6, new java.sql.Date(project.getEndDate().getTime()));
            pstmt.setString(7, project.getStatus().name());
            pstmt.setDouble(8, project.getBudget()); // Now using the budget property
            pstmt.setTimestamp(9, new Timestamp(project.getCreatedAt().getTime()));
            pstmt.setTimestamp(10, new Timestamp(project.getUpdatedAt().getTime()));

            pstmt.executeUpdate();

            // Set the role of the manager
            String setRoleSql = "UPDATE `User` SET role = ? WHERE user_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(setRoleSql)) {
                ps.setString(1, UserRole.MANAGER.name());
                ps.setString(2, project.getManagerId());
                ps.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(ProjectDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ProjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ProjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void deleteProject(String projectId, String userId) {
        String sql = "UPDATE Project SET status = ? WHERE project_id = ? AND manager_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ProjectStatus.CANCELLED.name()); // Assuming CANCELLED exists in ProjectStatus
            pstmt.setString(2, projectId);
            pstmt.setString(3, userId); // Only the manager can delete their project
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to delete project: No matching project found or unauthorized.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ProjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void updateProject(Project project) {
        String sql = "UPDATE Project SET project_name = ?, description = ?, start_date = ?, end_date = ?, "
                + "status = ?, budget = ?, updated_at = ? WHERE project_id = ? AND manager_id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, project.getProjectName());
            pstmt.setString(2, project.getDescription());
            pstmt.setDate(3, new java.sql.Date(project.getStartDate().getTime()));
            pstmt.setDate(4, new java.sql.Date(project.getEndDate().getTime()));
            pstmt.setString(5, project.getStatus().name());
            pstmt.setDouble(6, project.getBudget()); // Now using the budget property
            pstmt.setTimestamp(7, new Timestamp(project.getUpdatedAt().getTime()));
            pstmt.setString(8, project.getProjectId());
            pstmt.setString(9, project.getManagerId()); // Only the manager can update their project

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to update project: No matching project found or unauthorized.");
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ProjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ProjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<Project> getUserProjects(String userId) {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT p.* FROM Project p "
                + "JOIN Enroll e ON p.project_id = e.project_id "
                + "WHERE e.user_id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Project project = new Project();
                    project.setProjectId(rs.getString("project_id"));
                    project.setProjectName(rs.getString("project_name"));
                    project.setDescription(rs.getString("description"));
                    project.setManagerId(rs.getString("manager_id"));
                    project.setStartDate(rs.getDate("start_date"));
                    project.setEndDate(rs.getDate("end_date"));
                    project.setStatus(ProjectStatus.valueOf(rs.getString("status").toUpperCase()));
                    project.setBudget(rs.getDouble("budget")); // Now using the budget property
                    project.setCreatedAt(rs.getTimestamp("created_at"));
                    project.setUpdatedAt(rs.getTimestamp("updated_at"));

                    projects.add(project);
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ProjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ProjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return projects;
    }

    @Override
    public List<Project> getAllProjects() {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM Project";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Project project = new Project();
                    project.setProjectId(rs.getString("project_id"));
                    project.setProjectName(rs.getString("project_name"));
                    project.setDescription(rs.getString("description"));
                    project.setManagerId(rs.getString("manager_id"));
                    project.setStartDate(rs.getDate("start_date"));
                    project.setEndDate(rs.getDate("end_date"));
                    project.setStatus(ProjectStatus.valueOf(rs.getString("status").toUpperCase()));
                    project.setBudget(rs.getDouble("budget")); // Now using the budget property
                    project.setCreatedAt(rs.getTimestamp("created_at"));
                    project.setUpdatedAt(rs.getTimestamp("updated_at"));

                    projects.add(project);
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ProjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ProjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return projects;
    }

    public int getTotalMembers(String projectId) {
        String sql = "SELECT COUNT(*) AS total_members FROM Enroll WHERE project_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, projectId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total_members");
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ProjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ProjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    @Override
    public List<Project> getFilteredProjects(String nameFilter, String budgetFilter, String priorityFilter, String statusFilter) {
        List<Project> projects = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Project WHERE 1=1");

        if (nameFilter != null && !nameFilter.isEmpty()) {
            sql.append(" AND project_name LIKE ?");
        }
        if (budgetFilter != null && !budgetFilter.isEmpty()) {
            sql.append(" AND budget <= ?"); // Now fully functional with budget
        }
        if (statusFilter != null && !statusFilter.isEmpty()) {
            sql.append(" AND status = ?");
        }
        // Note: Priority filter remains unused since it's not in the Project model

        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (nameFilter != null && !nameFilter.isEmpty()) {
                pstmt.setString(paramIndex++, "%" + nameFilter + "%");
            }
            if (budgetFilter != null && !budgetFilter.isEmpty()) {
                pstmt.setDouble(paramIndex++, Double.parseDouble(budgetFilter));
            }
            if (statusFilter != null && !statusFilter.isEmpty()) {
                pstmt.setString(paramIndex++, statusFilter.toUpperCase());
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Project project = new Project();
                    project.setProjectId(rs.getString("project_id"));
                    project.setProjectName(rs.getString("project_name"));
                    project.setDescription(rs.getString("description"));
                    project.setManagerId(rs.getString("manager_id"));
                    project.setStartDate(rs.getDate("start_date"));
                    project.setEndDate(rs.getDate("end_date"));
                    project.setStatus(ProjectStatus.valueOf(rs.getString("status").toUpperCase()));
                    project.setBudget(rs.getDouble("budget")); // Now using the budget property
                    project.setCreatedAt(rs.getTimestamp("created_at"));
                    project.setUpdatedAt(rs.getTimestamp("updated_at"));

                    projects.add(project);
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ProjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ProjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return projects;
    }

    @Override
    public void closeProject(String projectId, String userId) {
        String sql = "UPDATE Project SET status = ? WHERE project_id = ? AND manager_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ProjectStatus.COMPLETED.name()); // Assuming COMPLETED exists in ProjectStatus
            pstmt.setString(2, projectId);
            pstmt.setString(3, userId);
            pstmt.executeUpdate();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ProjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ProjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}