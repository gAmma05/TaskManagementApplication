/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services.implementations;

import dao.implementations.EnrollmentDAO;
import dao.implementations.ProjectDAO;
import dao.interfaces.IEnrollmentDAO;
import dao.interfaces.IProjectDAO;
import enums.EnrollmentStatus;
import enums.ProjectStatus;
import enums.UserRole;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import model.Enrollment;
import model.Project;
import services.interfaces.IProjectService;
import utils.DBConnection;

/**
 *
 * @author thien
 */
public class ProjectService implements IProjectService {

    private final IProjectDAO projectDAO;
    private final IEnrollmentDAO enrollmentDAO;
    private final Connection connection;

    public ProjectService() throws ClassNotFoundException, SQLException {
        connection = DBConnection.getConnection();
        this.projectDAO = new ProjectDAO(connection);
        this.enrollmentDAO = new EnrollmentDAO(connection);
    }

    @Override
    public List<Project> getEnrolledProjects(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return new ArrayList<>(); // Return empty list for invalid userId
        }

        List<Enrollment> enrollments = getAllActiveEnrollmentsForUser(userId);
        return enrollments.stream()
                .map(enrollment -> projectDAO.getProjectById(enrollment.getProjectId()))
                .filter(project -> project != null && project.getStatus() == ProjectStatus.ACTIVE)
                .collect(Collectors.toList());
    }

    @Override
    public List<Project> getUnenrolledProjects(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return projectDAO.getAllProjects();
        }

        List<Enrollment> allEnrollments = enrollmentDAO.getEnrollmentsByUser(userId);
        List<String> enrolledOrRequestedProjectIds = allEnrollments.stream()
                .map(Enrollment::getProjectId)
                .collect(Collectors.toList());

        return projectDAO.getAllProjects().stream()
                .filter(project -> !enrolledOrRequestedProjectIds.contains(project.getProjectId()))
                .filter(project -> project.getStatus() == ProjectStatus.ACTIVE)
                .collect(Collectors.toList());
    }

    @Override
    public List<Project> getMyProjectsForManager(String userId, String role) {
        if (userId == null || userId.trim().isEmpty() || !role.toUpperCase().equals(UserRole.MANAGER.name())) {
            return new ArrayList<>();
        }

        List<Project> allManagedProjects = projectDAO.getProjectsByManager(userId);

        // Filter for active projects (optional, remove if you want all projects regardless of status)
        return allManagedProjects.stream()
                .filter(project -> project.getStatus() == ProjectStatus.ACTIVE)
                .collect(Collectors.toList());
    }

    private List<Enrollment> getAllActiveEnrollmentsForUser(String userId) {
        List<Enrollment> allEnrollments = new ArrayList<>();
        List<Project> allProjects = projectDAO.getAllProjects();

        for (Project project : allProjects) {
            Enrollment enrollment = enrollmentDAO.getEnrollmentByUserAndProject(userId, project.getProjectId());
            if (enrollment != null && enrollment.getStatus() == EnrollmentStatus.ACTIVE) {
                allEnrollments.add(enrollment);
            }
        }

        return allEnrollments;
    }

    @Override
    public List<ProjectWithEnrollment> getPendingProjects(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return new ArrayList<>();
        }
        List<ProjectWithEnrollment> pendingProjects = new ArrayList<>();
        List<Project> allProjects = projectDAO.getAllProjects();

        for (Project project : allProjects) {
            Enrollment enrollment = enrollmentDAO.getEnrollmentByUserAndProject(userId, project.getProjectId());
            if (enrollment != null && enrollment.getStatus() == EnrollmentStatus.PENDING) {
                pendingProjects.add(new ProjectWithEnrollment(project, enrollment.getStatus()));
            }
        }
        return pendingProjects;
    }

    @Override
    public List<EnrollmentWithProject> getRequestingEnrollments(String managerId) {
        if (managerId == null || managerId.trim().isEmpty()) {
            return new ArrayList<>();
        }
        List<EnrollmentWithProject> requesting = new ArrayList<>();
        List<Project> managedProjects = projectDAO.getProjectsByManager(managerId);

        for (Project project : managedProjects) {
            List<Enrollment> enrollments = enrollmentDAO.getEnrollmentsByProject(project.getProjectId());
            for (Enrollment enrollment : enrollments) {
                if (enrollment.getStatus() == EnrollmentStatus.PENDING) {
                    requesting.add(new EnrollmentWithProject(enrollment, project));
                }
            }
        }
        return requesting;
    }

    public static class ProjectWithEnrollment {

        private final Project project;
        private final EnrollmentStatus enrollmentStatus;

        public ProjectWithEnrollment(Project project, EnrollmentStatus enrollmentStatus) {
            this.project = project;
            this.enrollmentStatus = enrollmentStatus;
        }

        public Project getProject() {
            return project;
        }

        public EnrollmentStatus getEnrollmentStatus() {
            return enrollmentStatus;
        }
    }

    // DTO for requesting enrollments
    public static class EnrollmentWithProject {

        private final Enrollment enrollment;
        private final Project project;

        public EnrollmentWithProject(Enrollment enrollment, Project project) {
            this.enrollment = enrollment;
            this.project = project;
        }

        public Enrollment getEnrollment() {
            return enrollment;
        }

        public Project getProject() {
            return project;
        }
    }
}
