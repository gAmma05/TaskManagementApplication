/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao.interfaces;

import java.util.List;
import model.Project;

/**
 *
 * @author thien
 */
public interface IProjectDAO {
    boolean createProject(Project project);
    Project getProjectById(String projectId);
    List<Project> getAllProjects();
    List<Project> getProjectsByManager(String managerId);
    boolean updateProject(Project project);
    boolean deleteProject(String projectId);
}
