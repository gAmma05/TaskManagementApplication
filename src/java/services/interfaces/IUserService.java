/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package services.interfaces;

import java.util.List;
import model.User;

/**
 *
 * @author thien
 */
public interface IUserService {
    public User getMyInfo(String userId);
    public String getUsername(String userId);
}
