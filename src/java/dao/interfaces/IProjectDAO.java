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
    public void saveProject(Project project);
    public void deleteProject(String projectId, String userId);
    public void updateProject(Project project);
    public List<Project> getUserProjects(String userId);
    public List<Project> getAllProjects();
    public int getTotalMembers(String projectId);
    public List<Project> getFilteredProjects(String nameFilter, String budgetFilter, String priorityFilter, String statusFilter);
    public void closeProject(String projectId, String userId);
}
