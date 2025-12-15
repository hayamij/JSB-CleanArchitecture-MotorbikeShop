package com.motorbike.adapters.dto.response;

import java.time.LocalDateTime;

public class UpdateUserResponse {
    public static class SuccessData {
        public final Long id;
        public final String email;
        public final String username;
        public final String role;
        public final boolean active;
        public final LocalDateTime updatedAt;

        public SuccessData(Long id, String email, String username, String role, boolean active, LocalDateTime updatedAt) {
            this.id = id;
            this.email = email;
            this.username = username;
            this.role = role;
            this.active = active;
            this.updatedAt = updatedAt;
        }
    }

    public final boolean success;
    public final String errorCode;
    public final String message;
    public final SuccessData data;

    private UpdateUserResponse(boolean success, String errorCode, String message, SuccessData data) {
        this.success = success;
        this.errorCode = errorCode;
        this.message = message;
        this.data = data;
    }

    public static UpdateUserResponse success(SuccessData data) {
        return new UpdateUserResponse(true, null, null, data);
    }

    public static UpdateUserResponse error(String errorCode, String message) {
        return new UpdateUserResponse(false, errorCode, message, null);
    }
}