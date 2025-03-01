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

    private static final String INSERT_TASK = "INSERT INTO tasks (task_id, project_id, created_at, updated_at, status) VALUES (?, ?, ?, ?, ?)";
    private static final String DELETE_TASK = "DELETE FROM tasks WHERE task_id = ?";
    private static final String UPDATE_TASK = "UPDATE tasks SET project_id = ?, updated_at = ?, status = ? WHERE task_id = ?";
    private static final String SELECT_BY_ID = "SELECT * FROM tasks WHERE task_id = ?";
    private static final String SELECT_ALL = "SELECT * FROM tasks";

    private Connection connection;

    public TaskDAO() throws ClassNotFoundException, SQLException {
        connection = DBConnection.getConnection();
    }

    @Override
    public boolean add(Task task) {
        try (PreparedStatement pstmt = connection.prepareStatement(INSERT_TASK)) {

            pstmt.setString(1, task.getTaskId());
            pstmt.setString(2, task.getProjectId());
            pstmt.setTimestamp(3, new Timestamp(task.getCreatedAt().getTime()));
            pstmt.setTimestamp(4, new Timestamp(task.getUpdatedAt().getTime()));
            pstmt.setString(5, task.getStatus().name());

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

            pstmt.setString(1, task.getProjectId());
            pstmt.setTimestamp(2, new Timestamp(task.getUpdatedAt().getTime()));
            pstmt.setString(3, task.getStatus().name());
            pstmt.setString(4, task.getTaskId());

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
        task.setProjectId(rs.getString("project_id"));
        // Convert Timestamp to Date
        Timestamp createdTimestamp = rs.getTimestamp("created_at");
        task.setCreatedAt(createdTimestamp != null ? new Date(createdTimestamp.getTime()) : null);

        Timestamp updatedTimestamp = rs.getTimestamp("updated_at");
        task.setUpdatedAt(updatedTimestamp != null ? new Date(updatedTimestamp.getTime()) : null);

        task.setStatus(TaskStatus.valueOf(rs.getString("status")));
        return task;
    }

}
