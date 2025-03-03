/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao.interfaces;

import java.util.List;
import model.Task;

/**
 *
 * @author thien
 */
public interface ITaskDAO {
    public boolean createTask(Task taks);
    public boolean deleteTask(String id);
    public boolean updateTask(Task task);
    public Task getTaskById(String id);
    public List<Task> getAllTasks();
    public List<Task> getTasksByProject(String projectId);
    public List<Task> getTasksByUserAndProject(String userId, String projectId);
    public List<Task> getTasksByUserId(String userId);
}
