/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.implementations;

import dao.interfaces.ITaskDAO;
import enums.TaskPriority; // Assuming this enum exists
import enums.TaskStatus;
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
import model.Task;

/**
 *
 * @author thien
 */
public class TaskDAO implements ITaskDAO {

    private final Connection connection;

    public TaskDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean createTask(Task task) {
        String sql = "INSERT INTO Task (task_id, member_id, task_name, description, project_id, priority, status, deadline) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, task.getTaskId());
            stmt.setString(2, task.getMemberId());
            stmt.setString(3, task.getTaskName());
            stmt.setString(4, task.getDescription());
            stmt.setString(5, task.getProjectId());
            stmt.setString(6, task.getPriority() != null ? task.getPriority().name() : null);
            stmt.setString(7, task.getStatus() != null ? task.getStatus().name() : null);
            stmt.setTimestamp(8, task.getDeadline() != null ? new Timestamp(task.getDeadline().getTime()) : null);
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(TaskDAO.class.getName()).log(Level.SEVERE, "Error creating task", ex);
            return false;
        }
    }

    @Override
    public Task getTaskById(String taskId) {
        String sql = "SELECT * FROM Task WHERE task_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, taskId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTask(rs);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(TaskDAO.class.getName()).log(Level.SEVERE, "Error fetching task by ID: " + taskId, ex);
        }
        return null;
    }

    @Override
    public List<Task> getTasksByProject(String projectId) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM Task WHERE project_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, projectId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapResultSetToTask(rs));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(TaskDAO.class.getName()).log(Level.SEVERE, "Error fetching tasks by project ID: " + projectId, ex);
        }
        return tasks;
    }

    @Override
    public List<Task> getTasksByUserAndProject(String userId, String projectId) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT t.* FROM Task t WHERE t.project_id = ?"; // Basic filter by project
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, projectId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapResultSetToTask(rs));
                }
            }
            // Optional: Filter tasks assigned by the user (based on assigner_id)
            tasks.removeIf(task -> !userId.equals(task.getMemberId()));
        } catch (SQLException ex) {
            Logger.getLogger(TaskDAO.class.getName()).log(Level.SEVERE, "Error fetching tasks for user " + userId + " in project " + projectId, ex);
        }
        return tasks;
    }

    @Override
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM Task";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                tasks.add(mapResultSetToTask(rs));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TaskDAO.class.getName()).log(Level.SEVERE, "Error fetching all tasks", ex);
        }
        return tasks;
    }
    
    @Override
    public List<Task> getTasksByUserId(String userId) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM Task WHERE member_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, userId); 
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                tasks.add(mapResultSetToTask(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching tasks: " + e.getMessage());
        }
        return tasks;
    }

    @Override
    public boolean updateTask(Task task) {
        String sql = "UPDATE Task SET member_id = ?, task_name = ?, description = ?, project_id = ?, priority = ?, status = ?, deadline = ? "
                + "WHERE task_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, task.getMemberId());
            stmt.setString(2, task.getTaskName());
            stmt.setString(3, task.getDescription());
            stmt.setString(4, task.getProjectId());
            stmt.setString(5, task.getPriority() != null ? task.getPriority().name() : null);
            stmt.setString(6, task.getStatus() != null ? task.getStatus().name() : null);
            stmt.setTimestamp(7, task.getDeadline() != null ? new Timestamp(task.getDeadline().getTime()) : null);
            stmt.setString(8, task.getTaskId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(TaskDAO.class.getName()).log(Level.SEVERE, "Error updating task: " + task.getTaskId(), ex);
            return false;
        }
    }

    @Override
    public boolean deleteTask(String taskId) {
        String sql = "DELETE FROM Task WHERE task_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, taskId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(TaskDAO.class.getName()).log(Level.SEVERE, "Error deleting task: " + taskId, ex);
            return false;
        }
    }

    private Task mapResultSetToTask(ResultSet rs) throws SQLException {
        Task task = new Task();
        task.setTaskId(rs.getString("task_id"));
        task.setMemberId(rs.getString("member_id"));
        task.setTaskName(rs.getString("task_name"));
        task.setDescription(rs.getString("description"));
        task.setProjectId(rs.getString("project_id"));
        String priorityStr = rs.getString("priority");
        task.setPriority(priorityStr != null ? TaskPriority.valueOf(priorityStr) : null);
        String statusStr = rs.getString("status");
        task.setStatus(statusStr != null ? TaskStatus.valueOf(statusStr) : null);
        Timestamp deadlineTimestamp = rs.getTimestamp("deadline");
        task.setDeadline(deadlineTimestamp != null ? new Date(deadlineTimestamp.getTime()) : null); // Convert Timestamp to Date
        Timestamp createdAtTimestamp = rs.getTimestamp("created_at");
        task.setCreatedAt(createdAtTimestamp != null ? new Date(createdAtTimestamp.getTime()) : null); // Convert Timestamp to Date
        Timestamp updatedAtTimestamp = rs.getTimestamp("updated_at");
        task.setUpdatedAt(updatedAtTimestamp != null ? new Date(updatedAtTimestamp.getTime()) : null); // Convert Timestamp to Date
        return task;
    }
}
