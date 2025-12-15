package com.motorbike.business.dto.adduser;

import java.time.LocalDateTime;

public class AddUserOutputData {
    private final boolean success;
    private final String errorCode;
    private final String message;
    private final Long id;
    private final String email;
    private final String username;
    private final String role;
    private final boolean active;
    private final LocalDateTime createdAt;

    private AddUserOutputData(boolean success, String errorCode, String message,
                              Long id, String email, String username, String role,
                              boolean active, LocalDateTime createdAt) {
        this.success = success;
        this.errorCode = errorCode;
        this.message = message;
        this.id = id;
        this.email = email;
        this.username = username;
        this.role = role;
        this.active = active;
        this.createdAt = createdAt;
    }

    public static AddUserOutputData forSuccess(Long id, String email, String username, String role, boolean active, LocalDateTime createdAt) {
        return new AddUserOutputData(true, null, null, id, email, username, role, active, createdAt);
    }

    public static AddUserOutputData forError(String errorCode, String message) {
        return new AddUserOutputData(false, errorCode, message, null, null, null, null, false, null);
    }

    public boolean isSuccess() { return success; }
    public String getErrorCode() { return errorCode; }
    public String getMessage() { return message; }
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
    public boolean isActive() { return active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}