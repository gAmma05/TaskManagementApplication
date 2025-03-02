/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao.interfaces;

import java.util.List;
import model.Enrollment;

/**
 *
 * @author thien
 */
public interface IEnrollmentDAO {
    boolean createEnrollment(Enrollment enrollment);
    Enrollment getEnrollment(String userId, String projectId);
    List<Enrollment> getEnrollmentsByUser(String userId);
    List<Enrollment> getEnrollmentsByProject(String projectId);
    boolean updateEnrollment(Enrollment enrollment);
    boolean deleteEnrollment(String userId, String projectId);
}
