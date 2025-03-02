/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package services.interfaces;

import java.util.List;
import model.Task;

/**
 *
 * @author thien
 */
public interface ITaskService {
    public List<Task> getMyTasks(String userId);
}
