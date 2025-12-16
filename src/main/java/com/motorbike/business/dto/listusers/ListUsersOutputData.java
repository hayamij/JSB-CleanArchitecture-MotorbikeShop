package com.motorbike.business.dto.listusers;

import java.time.LocalDateTime;
import java.util.List;

public class ListUsersOutputData {
    public static class UserItem {
        public final Long id;
        public final String email;
        public final String username;
        public final String role;
        public final boolean active;
        public final LocalDateTime createdAt;
        public final LocalDateTime updatedAt;
        public final LocalDateTime lastLogin;

        public UserItem(Long id, String email, String username, String role,
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

    private final boolean success;
    private final String errorCode;
    private final String message;
    private final List<UserItem> users;

    private ListUsersOutputData(boolean success, String errorCode, String message, List<UserItem> users) {
        this.success = success;
        this.errorCode = errorCode;
        this.message = message;
        this.users = users;
    }

    public static ListUsersOutputData forSuccess(List<UserItem> users) {
        return new ListUsersOutputData(true, null, null, users);
    }

    public static ListUsersOutputData forError(String errorCode, String message) {
        return new ListUsersOutputData(false, errorCode, message, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public List<UserItem> getUsers() {
        return users;
    }
}