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
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {
    private String userId;
    private String fullName;
    private String username;
    private String password;
    private String email;
    private String phone;
    private UserRole role;
    
    
    public User(String fullName, String username, String password, String email, String phone, UserRole role) {
        this.userId = UUID.randomUUID().toString();
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.fullName = fullName;
    }
    
}
