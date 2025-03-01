package dao.implementations;

import model.Project;
import utils.DatabaseUtils;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO {

    public void saveProject(Project project) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO project (project_name, description, start_date, end_date, "
                + "priority, status, budget, manager_id, created_at, updated_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtils.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

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
        }
    }

    public List<Project> getUserProjects(Integer userId) throws SQLException, ClassNotFoundException {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT p.* FROM project p "
                + "JOIN enroll e ON p.project_id = e.project_id "
                + "WHERE e.user_id = ?";

        try (Connection conn = DatabaseUtils.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

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

    public Project getById(String id) {
        
    }
}
