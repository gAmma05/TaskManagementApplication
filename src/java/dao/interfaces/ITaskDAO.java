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
    public boolean add(Task taks);
    public boolean delete(String id);
    public boolean update(Task task);
    public Task getById(String id);
    public List<Task> getAll();
}
