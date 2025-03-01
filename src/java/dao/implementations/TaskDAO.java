/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao.implementations;

import dao.interfaces.ITaskDAO;
import enums.TaskStatus;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import model.Task;
import utils.DatabaseUtils;

/**
 *
 * @author thien
 */
public class TaskDAO implements ITaskDAO {
    private Connection connection;
    
    public TaskDAO() throws ClassNotFoundException, SQLException {
        this.connection = DatabaseUtils.getConnection();
    }

    @Override
    public boolean add(Task task) {
        String sql = "INSERT INTO tasks (taskId, projectId, createdAt, updateAt, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, task.getTaskId());
            stmt.setString(2, task.getProjectId());
            stmt.setTimestamp(3, task.getCreatedAt() != null ? new Timestamp(task.getCreatedAt().getTime()) : null);
            stmt.setTimestamp(4, task.getUpdateAt() != null ? new Timestamp(task.getUpdateAt().getTime()) : null);
            stmt.setString(5, task.getStatus().name());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Task task) {
        String sql = "DELETE FROM tasks WHERE taskId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, task.getTaskId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Task task) {
        String sql = "UPDATE tasks SET projectId = ?, updateAt = ?, status = ? WHERE taskId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, task.getProjectId());
            stmt.setTimestamp(2, task.getUpdateAt() != null ? new Timestamp(task.getUpdateAt().getTime()) : null);
            stmt.setString(3, task.getStatus().name());
            stmt.setString(4, task.getTaskId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Task> getAll() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Task task = new Task();
                task.setTaskId(rs.getString("taskId"));
                task.setProjectId(rs.getString("projectId"));
                task.setCreatedAt(rs.getTimestamp("createdAt") != null ? new Date(rs.getTimestamp("createdAt").getTime()) : null);
                task.setUpdateAt(rs.getTimestamp("updateAt") != null ? new Date(rs.getTimestamp("updateAt").getTime()) : null);
                task.setStatus(TaskStatus.valueOf(rs.getString("status")));
                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    @Override
    public Task getById(String id) {
        String sql = "SELECT * FROM tasks WHERE taskId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Task task = new Task();
                task.setTaskId(rs.getString("taskId"));
                task.setProjectId(rs.getString("projectId"));
                task.setCreatedAt(rs.getTimestamp("createdAt") != null ? new Date(rs.getTimestamp("createdAt").getTime()) : null);
                task.setUpdateAt(rs.getTimestamp("updateAt") != null ? new Date(rs.getTimestamp("updateAt").getTime()) : null);
                task.setStatus(TaskStatus.valueOf(rs.getString("status")));
                return task;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

