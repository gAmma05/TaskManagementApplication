package dao;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author asus
 */


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Project;
import utils.DBConnection;

public class ProjectDAO {
    public void saveProject(Project project) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO project (project_name, description, start_date, end_date, " +
                    "priority, status, budget, manager_id, created_at, updated_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, project.getProject_name());
            pstmt.setString(2, project.getDescription());
            pstmt.setDate(3, new java.sql.Date(project.getStart_date().getTime()));
            pstmt.setDate(4, new java.sql.Date(project.getEnd_date().getTime()));
            pstmt.setString(5, project.getPriority().name());
            pstmt.setString(6, project.getStatus().name());
            pstmt.setDouble(7, project.getBudget());
            pstmt.setLong(8, project.getManager_id());
            pstmt.setTimestamp(9, new Timestamp(project.getCreated_at().getTime()));
            pstmt.setTimestamp(10, new Timestamp(project.getUpdated_at().getTime()));
            
            pstmt.executeUpdate();
        }
    }
    
    public List<Project> getUserProjects(Long userId) throws SQLException, ClassNotFoundException {
        List<Project> projects = new ArrayList<>();
        String sql = "SELECT p.* FROM project p " +
                    "JOIN enroll e ON p.project_id = e.project_id " +
                    "WHERE e.user_id = ?";
                    
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Project project = new Project();
                project.setProject_id(rs.getLong("project_id"));
                project.setProject_name(rs.getString("project_name"));
                project.setDescription(rs.getString("description"));
                project.setStart_date(rs.getDate("start_date"));
                project.setEnd_date(rs.getDate("end_date"));
                project.setPriority(Project.Priority.valueOf(rs.getString("priority")));
                project.setStatus(Project.Status.valueOf(rs.getString("status")));
                project.setBudget(rs.getDouble("budget"));
                project.setManager_id(rs.getLong("manager_id"));
                project.setCreated_at(rs.getTimestamp("created_at"));
                project.setUpdated_at(rs.getTimestamp("updated_at"));
                
                projects.add(project);
            }
        }
        return projects;
    }
}
