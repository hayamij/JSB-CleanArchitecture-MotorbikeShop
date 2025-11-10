package com.motorbike.interfaceadapters.dto;

/**
 * Data Transfer Object for Register Response
 * Used for API responses in the presentation layer
 */
public class RegisterResponseDTO {
    private Long userId;
    private String email;
    private String username;
    private String role;
    private boolean success;
    private String message;
    
    // Constructors
    public RegisterResponseDTO() {
    }
    
    public RegisterResponseDTO(Long userId, String email, String username, 
                              String role, boolean success, String message) {
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.role = role;
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
