package com.motorbike.adapters.dto.response;

/**
 * Response DTO for Login
 */
public class LoginResponse {
    private boolean success;
    private Long userId;
    private String email;
    private String username;
    private String role;
    private Long cartId;
    private boolean cartMerged;
    private int mergedItemsCount;
    private String message;
    private String errorCode;
    private String errorMessage;

    public LoginResponse(boolean success, Long userId, String email, String username,
                        String role, Long cartId, boolean cartMerged, int mergedItemsCount,
                        String message, String errorCode, String errorMessage) {
        this.success = success;
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.role = role;
        this.cartId = cartId;
        this.cartMerged = cartMerged;
        this.mergedItemsCount = mergedItemsCount;
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
    public Long getCartId() { return cartId; }
    public boolean isCartMerged() { return cartMerged; }
    public int getMergedItemsCount() { return mergedItemsCount; }
    public String getMessage() { return message; }
    public String getErrorCode() { return errorCode; }
    public String getErrorMessage() { return errorMessage; }
}
