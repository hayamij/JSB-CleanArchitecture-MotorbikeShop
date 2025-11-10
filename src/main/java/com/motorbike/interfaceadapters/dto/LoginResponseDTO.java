package com.motorbike.interfaceadapters.dto;

/**
 * Data Transfer Object for Login Response
 * Used for API responses in the presentation layer
 */
public class LoginResponseDTO {
    private Long userId;
    private String email;
    private String username;
    private String role;
    private String token;
    private boolean success;
    private String message;
    
    // Constructors
    public LoginResponseDTO() {
    }
    
    public LoginResponseDTO(Long userId, String email, String username, 
                           String role, String token, boolean success, String message) {
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.role = role;
        this.token = token;
        this.success = success;
        this.message = message;
    }
    
    // Getters and Setters
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}
