package com.motorbike.adapters.dto.response;

/**
 * Response DTO for add to cart operation
 */
public class AddToCartResponse {
    private boolean success;
    private String message;
    private String errorCode;
    private String errorMessage;

    public AddToCartResponse(boolean success, String message, String errorCode, String errorMessage) {
        this.success = success;
        this.message = message;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public String getErrorCode() { return errorCode; }
    public String getErrorMessage() { return errorMessage; }
}
