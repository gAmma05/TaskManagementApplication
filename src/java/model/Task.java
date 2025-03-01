/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import enums.TaskStatus;
import java.sql.Date;
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
    private String projectId;
    private Date createdAt;
    private Date updatedAt;
    private TaskStatus status;
}
