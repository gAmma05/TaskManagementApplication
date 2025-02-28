package dao;

import enums.UserRole;
import model.Project;
import utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO {

    public void saveProject(Project project) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO project (project_name, description, start_date, end_date, "
                + "priority, status, budget, manager_id, created_at, updated_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

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

            // Set the role of the creator as Leader
            String setRoleSql = "UPDATE `User` SET role = ? WHERE user_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(setRoleSql)) {
                ps.setInt(1, UserRole.MANAGER.getValue());
                ps.setInt(2, project.getManager_id());
                ps.executeUpdate();
            }
        }
    }

    public List<Project> getUserProjects(Integer userId) throws SQLException, ClassNotFoundException {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT p.* FROM project p "
                + "JOIN enroll e ON p.project_id = e.project_id "
                + "WHERE e.user_id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

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
                    project.setStatus(Project.Status.valueOf(rs.getString("status").toUpperCase()));
                    project.setBudget(rs.getDouble("budget"));
                    project.setManager_id(rs.getInt("manager_id"));
                    project.setCreated_at(rs.getTimestamp("created_at"));
                    project.setUpdated_at(rs.getTimestamp("updated_at"));

                    projects.add(project);
                }
            }
        }
        return projects;
    }

    // Method to get all projects
    public List<Project> getAllProjects() throws SQLException, ClassNotFoundException {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT * FROM project";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Project project = new Project();
                    project.setProject_id(rs.getLong("project_id"));
                    project.setProject_name(rs.getString("project_name"));
                    project.setDescription(rs.getString("description"));
                    project.setStart_date(rs.getDate("start_date"));
                    project.setEnd_date(rs.getDate("end_date"));
                    project.setPriority(Project.Priority.valueOf(rs.getString("priority").toUpperCase().replace(" ", "_")));
                    project.setStatus(Project.Status.valueOf(rs.getString("status").toUpperCase().replace(" ", "_")));
                    project.setBudget(rs.getDouble("budget"));
                    project.setManager_id(rs.getInt("manager_id"));
                    project.setCreated_at(rs.getTimestamp("created_at"));
                    project.setUpdated_at(rs.getTimestamp("updated_at"));

                    projects.add(project);
                }
            }
        }
        return projects;
    }

    // Method to get the total number of members in a project
    public int getTotalMembers(long projectId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) AS total_members FROM enroll WHERE project_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, projectId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total_members");
                }
            }
        }
        return 0;
    }

    // Method to get all projects with filtering
    public List<Project> getFilteredProjects(String nameFilter, String budgetFilter, String priorityFilter, String statusFilter) throws SQLException, ClassNotFoundException {
        List<Project> projects = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM project WHERE deleted_at IS NULL");

        if (nameFilter != null && !nameFilter.isEmpty()) {
            sql.append(" AND project_name LIKE ?");
        }
        if (budgetFilter != null && !budgetFilter.isEmpty()) {
            sql.append(" AND budget <= ?");
        }
        if (priorityFilter != null && !priorityFilter.isEmpty()) {
            sql.append(" AND priority = ?");
        }
        if (statusFilter != null && !statusFilter.isEmpty()) {
            sql.append(" AND status = ?");
        }

        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (nameFilter != null && !nameFilter.isEmpty()) {
                pstmt.setString(paramIndex++, "%" + nameFilter + "%");
            }
            if (budgetFilter != null && !budgetFilter.isEmpty()) {
                pstmt.setDouble(paramIndex++, Double.parseDouble(budgetFilter));
            }
            if (priorityFilter != null && !priorityFilter.isEmpty()) {
                pstmt.setString(paramIndex++, priorityFilter.toUpperCase());
            }
            if (statusFilter != null && !statusFilter.isEmpty()) {
                pstmt.setString(paramIndex++, statusFilter.toUpperCase());
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Project project = new Project();
                    project.setProject_id(rs.getLong("project_id"));
                    project.setProject_name(rs.getString("project_name"));
                    project.setDescription(rs.getString("description"));
                    project.setStart_date(rs.getDate("start_date"));
                    project.setEnd_date(rs.getDate("end_date"));
                    project.setPriority(Project.Priority.valueOf(rs.getString("priority").toUpperCase().replace(" ", "_")));
                    project.setStatus(Project.Status.valueOf(rs.getString("status").toUpperCase().replace(" ", "_")));
                    project.setBudget(rs.getDouble("budget"));
                    project.setManager_id(rs.getInt("manager_id"));
                    project.setCreated_at(rs.getTimestamp("created_at"));
                    project.setUpdated_at(rs.getTimestamp("updated_at"));

                    projects.add(project);
                }
            }
        }
        return projects;
    }

    // Method to close a project
    public void closeProject(int projectId, int userId) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE project SET deleted_at = ? WHERE project_id = ? AND manager_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            pstmt.setInt(2, projectId);
            pstmt.setInt(3, userId);
            pstmt.executeUpdate();
        }
    }
}
