package model;

import enums.ProjectStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;

/**
 *
 * Author: asus
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    private String projectId;
    private String projectName;
    private String description;
    private String managerId;
    private double budget;
    private Date startDate;
    private Date endDate;
    private ProjectStatus status;
    private Date createdAt;
    private Date updatedAt;

    public Project(String projectName, String description, String managerId, Date startDate, Date endDate, ProjectStatus status, Date createdAt, Date updatedAt) {
        this.projectId = UUID.randomUUID().toString();
        this.projectName = projectName;
        this.description = description;
        this.managerId = managerId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
}
