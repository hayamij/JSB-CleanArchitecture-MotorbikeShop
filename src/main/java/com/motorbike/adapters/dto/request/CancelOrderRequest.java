package com.motorbike.adapters.dto.request;

/**
 * Request DTO for cancel order
 */
public class CancelOrderRequest {
    private Long userId;
    private String cancelReason;

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }
}