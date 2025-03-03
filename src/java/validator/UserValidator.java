/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package validator;

import dao.interfaces.IUserDAO;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 *
 * @author thien
 */
public class UserValidator {
    private final IUserDAO userDAO;
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$"; // Basic email format
    private static final String PHONE_REGEX = "^\\d{8,15}$"; // 8-15 digits

    public UserValidator(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public Map<String, String> validate(String username, String password, String fullName, String email, String phone) {
        Map<String, String> errors = new HashMap<>();

        // Username validation
        if (username == null || username.trim().isEmpty()) {
            errors.put("username", "Username is required.");
        } else if (username.length() < 3 || username.length() > 50) {
            errors.put("username", "Username must be between 3 and 50 characters.");
        } else if (userDAO.getUserByUsername(username) != null) {
            errors.put("username", "Username already exists.");
        }

        // Password validation
        if (password == null || password.trim().isEmpty()) {
            errors.put("password", "Password is required.");
        } else if (password.length() < 6 || password.length() > 50) {
            errors.put("password", "Password must be between 6 and 50 characters.");
        }

        // Full Name validation
        if (fullName == null || fullName.trim().isEmpty()) {
            errors.put("fullname", "Full name is required.");
        } else if (fullName.length() < 2 || fullName.length() > 100) {
            errors.put("fullname", "Full name must be between 2 and 100 characters.");
        }

        // Email validation
        if (email == null || email.trim().isEmpty()) {
            errors.put("email", "Email is required.");
        } else if (!Pattern.matches(EMAIL_REGEX, email)) {
            errors.put("email", "Invalid email format.");
        }

        // Phone validation (optional)
        if (phone != null && !phone.trim().isEmpty() && !Pattern.matches(PHONE_REGEX, phone)) {
            errors.put("phone_number", "Phone number must be 8-15 digits if provided.");
        }

        return errors;
    }
}