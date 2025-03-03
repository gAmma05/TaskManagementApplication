/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package services.interfaces;

import java.util.List;
import model.Project;
import services.implementations.ProjectService.EnrollmentWithProject;
import services.implementations.ProjectService.ProjectWithEnrollment;

/**
 *
 * @author thien
 */
public interface IProjectService {
    public List<Project> getEnrolledProjects(String userId);
    public List<Project> getUnenrolledProjects(String userId);
    public List<Project> getMyProjectsForManager(String userId, String role);
    List<ProjectWithEnrollment> getPendingProjects(String userId);
    List<EnrollmentWithProject> getRequestingEnrollments(String managerId);
    public Project getProjectById(String projectId);
}
