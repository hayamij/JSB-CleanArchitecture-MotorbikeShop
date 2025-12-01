package com.motorbike.business.dto.register;

import com.motorbike.domain.entities.VaiTro;
import java.time.LocalDateTime;

public class RegisterOutputData {
    private final boolean success;
    private final String errorCode;
    private final String errorMessage;
    
    private final Long userId;
    private final String email;
    private final String username;
    private final VaiTro role;
    private final LocalDateTime createdAt;
    
    private final boolean autoLoginEnabled;
    private final String sessionToken;

    public RegisterOutputData(Long userId, String email, String username,
                             VaiTro role, LocalDateTime createdAt,
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

    public RegisterOutputData(Long userId, String email, String username,
                             VaiTro role, LocalDateTime createdAt) {
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

    public boolean isSuccess() {
        return success;
    }

    public String getErrorCode() {return errorCode;}

    public String getErrorMessage() {return errorMessage;}

    public Long getUserId() {return userId;}

    public String getEmail() {return email;}

    public String getUsername() {return username;}

    public VaiTro getRole() {return role;}

    public LocalDateTime getCreatedAt() {return createdAt;}

    public boolean isAutoLoginEnabled() {
        return autoLoginEnabled;
    }

    public String getSessionToken() {return sessionToken;}
    
    public static RegisterOutputData forSuccess(
            Long userId, String email, String username,
            VaiTro role, LocalDateTime createdAt) {
        return new RegisterOutputData(userId, email, username, role, createdAt);
    }
    
    public static RegisterOutputData forError(String errorCode, String errorMessage) {
        return new RegisterOutputData(errorCode, errorMessage);
    }
}
