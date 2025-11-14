package com.motorbike.adapters.dto.response;

/**
 * Response DTO for Registration
 */
public class RegisterResponse {
    private boolean success;
    private Long userId;
    private String email;
    private String username;
    private String role;
    private String message;
    private String errorCode;
    private String errorMessage;

    public RegisterResponse(boolean success, Long userId, String email, String username,
                          String role, String message, String errorCode, String errorMessage) {
        this.success = success;
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.role = role;
        this.message = message;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    // Getters
    public boolean isSuccess() { return success; }
    public Long getUserId() { return userId; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
    public String getMessage() { return message; }
    public String getErrorCode() { return errorCode; }
    public String getErrorMessage() { return errorMessage; }
}
