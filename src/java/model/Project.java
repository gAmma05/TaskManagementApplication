package model;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;

/**
 *
 * @author asus
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    private Long project_id;
    private String project_name;
    private String description;
    private Date start_date;
    private Date end_date;
    private Priority priority;
    private Status status;
    private Double budget;
    private Long manager_id;
    private Date created_at;
    private Date updated_at;

    public enum Priority {
        LOW, MEDIUM, HIGH
    }

    public enum Status {
        NOT_STARTED, IN_PROGRESS, COMPLETED
    }
}