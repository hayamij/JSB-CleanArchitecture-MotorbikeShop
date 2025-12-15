package com.motorbike.adapters.dto.response;

import java.time.LocalDateTime;

public class AddUserResponse {
    public static class SuccessData {
        public final Long id;
        public final String email;
        public final String username;
        public final String role;
        public final boolean active;
        public final LocalDateTime createdAt;

        public SuccessData(Long id, String email, String username, String role, boolean active, LocalDateTime createdAt) {
            this.id = id;
            this.email = email;
            this.username = username;
            this.role = role;
            this.active = active;
            this.createdAt = createdAt;
        }
    }

    public final boolean success;
    public final String errorCode;
    public final String message;
    public final SuccessData data;

    private AddUserResponse(boolean success, String errorCode, String message, SuccessData data) {
        this.success = success;
        this.errorCode = errorCode;
        this.message = message;
        this.data = data;
    }

    public static AddUserResponse success(SuccessData data) {
        return new AddUserResponse(true, null, null, data);
    }

    public static AddUserResponse error(String errorCode, String message) {
        return new AddUserResponse(false, errorCode, message, null);
    }
}