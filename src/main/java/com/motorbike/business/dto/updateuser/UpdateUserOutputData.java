package com.motorbike.business.dto.updateuser;

import java.time.LocalDateTime;

public class UpdateUserOutputData {
    private final boolean success;
    private final String errorCode;
    private final String message;

    private final Long id;
    private final String email;
    private final String username;
    private final String role;
    private final boolean active;
    private final LocalDateTime updatedAt;

    private UpdateUserOutputData(boolean success, String errorCode, String message,
                                 Long id, String email, String username, String role,
                                 boolean active, LocalDateTime updatedAt) {
        this.success = success;
        this.errorCode = errorCode;
        this.message = message;
        this.id = id;
        this.email = email;
        this.username = username;
        this.role = role;
        this.active = active;
        this.updatedAt = updatedAt;
    }

    public static UpdateUserOutputData forSuccess(Long id, String email, String username, String role, boolean active, LocalDateTime updatedAt) {
        return new UpdateUserOutputData(true, null, null, id, email, username, role, active, updatedAt);
    }

    public static UpdateUserOutputData forError(String errorCode, String message) {
        return new UpdateUserOutputData(false, errorCode, message, null, null, null, null, false, null);
    }

    public boolean isSuccess() { return success; }
    public String getErrorCode() { return errorCode; }
    public String getMessage() { return message; }
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
    public boolean isActive() { return active; }
    public java.time.LocalDateTime getUpdatedAt() { return updatedAt; }
}