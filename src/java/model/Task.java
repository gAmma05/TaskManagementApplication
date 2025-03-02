/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import enums.TaskPriority;
import enums.TaskStatus;
import java.sql.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author thien
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    private String taskId;
    private String assignerId;
    private String taskName;
    private String description;
    private String projectId;
    private TaskPriority priority;
    private TaskStatus status;
    private Date deadline;
    private Date createdAt;
    private Date updatedAt;

    public Task(String assignerId, String taskName, String description, String projectId, TaskPriority priotity, TaskStatus status, Date deadline, Date createdAt, Date updatedAt) {
        this.taskId = UUID.randomUUID().toString();
        this.assignerId = assignerId;
        this.taskName = taskName;
        this.description = description;
        this.projectId = projectId;
        this.priority = priotity;
        this.status = status;
        this.deadline = deadline;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
