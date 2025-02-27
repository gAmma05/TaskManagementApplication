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
    public boolean add(Task task);
    public boolean delete(Task task);
    public boolean update(Task task);
    public List<Task> getAll();
    public Task getById(String id);
}
