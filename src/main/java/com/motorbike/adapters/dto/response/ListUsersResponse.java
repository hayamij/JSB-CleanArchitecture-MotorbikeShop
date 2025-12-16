package com.motorbike.adapters.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class ListUsersResponse {
    public static class UserItemResponse {
        public final Long id;
        public final String email;
        public final String username;
        public final String role;
        public final boolean active;
        public final LocalDateTime createdAt;
        public final LocalDateTime updatedAt;
        public final LocalDateTime lastLogin;

        public UserItemResponse(Long id, String email, String username, String role,
                                boolean active, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime lastLogin) {
            this.id = id;
            this.email = email;
            this.username = username;
            this.role = role;
            this.active = active;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.lastLogin = lastLogin;
        }
    }

    public final boolean success;
    public final String errorCode;
    public final String message;
    public final List<UserItemResponse> users;

    private ListUsersResponse(boolean success, String errorCode, String message, List<UserItemResponse> users) {
        this.success = success;
        this.errorCode = errorCode;
        this.message = message;
        this.users = users;
    }

    public static ListUsersResponse success(List<UserItemResponse> users) {
        return new ListUsersResponse(true, null, null, users);
    }

    public static ListUsersResponse error(String errorCode, String message) {
        return new ListUsersResponse(false, errorCode, message, null);
    }
}