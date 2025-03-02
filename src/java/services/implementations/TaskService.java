/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package services.implementations;

import dao.implementations.TaskDAO;
import dao.interfaces.ITaskDAO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Task;
import services.interfaces.ITaskService;
import utils.DBConnection;

/**
 *
 * @author thien
 */
public class TaskService implements ITaskService {
    private final ITaskDAO taskDAO;
    private final Connection connection;

    public TaskService() throws ClassNotFoundException, SQLException {
        this.connection = DBConnection.getConnection();
        this.taskDAO = new TaskDAO(connection);
    }

    @Override
    public List<Task> getMyTasks(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return taskDAO.getTasksByUserId(userId);
    }
}
