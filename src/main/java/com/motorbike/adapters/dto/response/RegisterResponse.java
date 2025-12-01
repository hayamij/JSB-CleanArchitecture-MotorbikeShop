package com.motorbike.adapters.dto.response;

public class RegisterResponse {
    private boolean success;
    private Long userId;
    private String email;
    private String username;
    private String role;
    private String createdAtDisplay;
    private boolean autoLoginEnabled;
    private String sessionToken;
    private String message;
    private String errorCode;
    private String errorMessage;

    public RegisterResponse(boolean success, Long userId, String email, String username,
                          String role, String createdAtDisplay, boolean autoLoginEnabled,
                          String sessionToken, String message, String errorCode, String errorMessage) {
        this.success = success;
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.role = role;
        this.createdAtDisplay = createdAtDisplay;
        this.autoLoginEnabled = autoLoginEnabled;
        this.sessionToken = sessionToken;
        this.message = message;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() { return success; }
    public Long getUserId() { return userId; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
    public String getCreatedAtDisplay() { return createdAtDisplay; }
    public boolean isAutoLoginEnabled() { return autoLoginEnabled; }
    public String getSessionToken() { return sessionToken; }
    public String getMessage() { return message; }
    public String getErrorCode() { return errorCode; }
    public String getErrorMessage() { return errorMessage; }
}
