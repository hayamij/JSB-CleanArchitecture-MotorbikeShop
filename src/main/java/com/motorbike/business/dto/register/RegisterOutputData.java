package com.motorbike.business.dto.register;

import com.motorbike.domain.entities.UserRole;
import java.time.LocalDateTime;

/**
 * Output DTO for Register Use Case
 * Carries data OUT OF the use case to the presenter
 * Contains raw data before formatting
 */
public class RegisterOutputData {
    private final boolean success;
    private final String errorCode;
    private final String errorMessage;
    
    // User data (only if success)
    private final Long userId;
    private final String email;
    private final String username;
    private final UserRole role;
    private final LocalDateTime createdAt;
    
    // Auto-login info (optional)
    private final boolean autoLoginEnabled;
    private final String sessionToken;

    // Constructor for success case
    public RegisterOutputData(Long userId, String email, String username, 
                             UserRole role, LocalDateTime createdAt,
                             boolean autoLoginEnabled, String sessionToken) {
        this.success = true;
        this.errorCode = null;
        this.errorMessage = null;
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.role = role;
        this.createdAt = createdAt;
        this.autoLoginEnabled = autoLoginEnabled;
        this.sessionToken = sessionToken;
    }

    // Constructor for success case without auto-login
    public RegisterOutputData(Long userId, String email, String username, 
                             UserRole role, LocalDateTime createdAt) {
        this.success = true;
        this.errorCode = null;
        this.errorMessage = null;
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.role = role;
        this.createdAt = createdAt;
        this.autoLoginEnabled = false;
        this.sessionToken = null;
    }

    // Constructor for error case
    public RegisterOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.userId = null;
        this.email = null;
        this.username = null;
        this.role = null;
        this.createdAt = null;
        this.autoLoginEnabled = false;
        this.sessionToken = null;
    }

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public UserRole getRole() {
        return role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isAutoLoginEnabled() {
        return autoLoginEnabled;
    }

    public String getSessionToken() {
        return sessionToken;
    }
}
