package com.motorbike.adapters.dto.response;

/**
 * Response DTO for view cart operation
 */
public class ViewCartResponse {
    private boolean success;
    private Long userId;

    public ViewCartResponse(boolean success, Long userId) {
        this.success = success;
        this.userId = userId;
    }

    // Getters
    public boolean isSuccess() { return success; }
    public Long getUserId() { return userId; }
}
