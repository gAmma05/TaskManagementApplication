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
import model.Task;
import utils.DBConnection;

/**
 *
 * @author thien
 */
public class TaskDAO implements ITaskDAO {

    private static final String INSERT_TASK = "INSERT INTO Task (task_id, task_name, description, project_id, priority, status, deadline, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String DELETE_TASK = "DELETE FROM Task WHERE task_id = ?";
    private static final String UPDATE_TASK = "UPDATE Task SET task_name = ?, description = ?, project_id = ?, priority = ?, status = ?, deadline = ?, updated_at = ? WHERE task_id = ?";
    private static final String SELECT_BY_ID = "SELECT * FROM Task WHERE task_id = ?";
    private static final String SELECT_ALL = "SELECT * FROM Task";

    private Connection connection;

    public TaskDAO() throws ClassNotFoundException, SQLException {
        connection = DBConnection.getConnection();
    }

    @Override
    public boolean add(Task task) {
        try (PreparedStatement pstmt = connection.prepareStatement(INSERT_TASK)) {
            pstmt.setString(1, task.getTaskId());
            pstmt.setString(2, task.getTaskName());
            pstmt.setString(3, task.getDescription());
            pstmt.setString(4, task.getProjectId());
            pstmt.setString(5, task.getPriotity().name()); // Assuming TaskPriority enum
            pstmt.setString(6, task.getStatus().name());
            pstmt.setDate(7, task.getDeadline() != null ? new java.sql.Date(task.getDeadline().getTime()) : null);
            pstmt.setTimestamp(8, new Timestamp(task.getCreatedAt().getTime()));
            pstmt.setTimestamp(9, new Timestamp(task.getUpdatedAt().getTime()));

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        try (PreparedStatement pstmt = connection.prepareStatement(DELETE_TASK)) {
            pstmt.setString(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Task task) {
        try (PreparedStatement pstmt = connection.prepareStatement(UPDATE_TASK)) {
            pstmt.setString(1, task.getTaskName());
            pstmt.setString(2, task.getDescription());
            pstmt.setString(3, task.getProjectId());
            pstmt.setString(4, task.getPriotity().name()); // Assuming TaskPriority enum
            pstmt.setString(5, task.getStatus().name());
            pstmt.setDate(6, task.getDeadline() != null ? new java.sql.Date(task.getDeadline().getTime()) : null);
            pstmt.setTimestamp(7, new Timestamp(task.getUpdatedAt().getTime()));
            pstmt.setString(8, task.getTaskId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Task getById(String id) {
        try (PreparedStatement pstmt = connection.prepareStatement(SELECT_BY_ID)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractTaskFromResultSet(rs);
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Task> getAll() {
        List<Task> tasks = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(SELECT_ALL); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                tasks.add(extractTaskFromResultSet(rs));
            }
            return tasks;
        } catch (SQLException e) {
            e.printStackTrace();
            return tasks;
        }
    }

    private Task extractTaskFromResultSet(ResultSet rs) throws SQLException {
        Task task = new Task();
        task.setTaskId(rs.getString("task_id"));
        task.setTaskName(rs.getString("task_name"));
        task.setDescription(rs.getString("description"));
        task.setProjectId(rs.getString("project_id"));
        task.setPriotity(TaskPriority.valueOf(rs.getString("priority").toUpperCase())); // Assuming TaskPriority enum
        task.setStatus(TaskStatus.valueOf(rs.getString("status").toUpperCase()));

        // Convert Date for deadline
        java.sql.Date deadlineSql = rs.getDate("deadline");
        task.setDeadline(deadlineSql != null ? new Date(deadlineSql.getTime()) : null);

        // Convert Timestamp to Date for created_at and updated_at
        Timestamp createdTimestamp = rs.getTimestamp("created_at");
        task.setCreatedAt(createdTimestamp != null ? new Date(createdTimestamp.getTime()) : null);

        Timestamp updatedTimestamp = rs.getTimestamp("updated_at");
        task.setUpdatedAt(updatedTimestamp != null ? new Date(updatedTimestamp.getTime()) : null);

        return task;
    }
}