package com.motorbike.adapters.dto.response;

/**
 * Response DTO for checkout operation
 */
public class CheckoutResponse {
    private boolean success;
    private String message;

    public CheckoutResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
}
