package com.motorbike.adapters.dto.response;

/**
 * Response DTO for update cart quantity operation
 */
public class UpdateCartResponse {
    private boolean success;
    private String message;

    public UpdateCartResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
}
