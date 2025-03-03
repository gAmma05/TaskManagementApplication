/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author gAmma
 */
import enums.UserRole;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String userId;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phone;
    private UserRole role;
    
    
    public User(String username, String password, String fullName, String email, String phone, UserRole role) {
        this.userId = UUID.randomUUID().toString();
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }
    
}
