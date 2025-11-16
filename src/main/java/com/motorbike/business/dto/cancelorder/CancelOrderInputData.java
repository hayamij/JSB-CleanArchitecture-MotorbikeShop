package com.motorbike.business.dto.cancelorder;

/**
 * Input DTO for Cancel Order Use Case
 * Carries data INTO the use case from the adapter layer
 * Plain data structure - no business logic
 */
public class CancelOrderInputData {
    private final Long orderId;
    private final Long userId;
    private final String cancelReason;  // Lý do hủy

    public CancelOrderInputData(Long orderId, Long userId, String cancelReason) {
        this.orderId = orderId;
        this.userId = userId;
        this.cancelReason = cancelReason;
    }

    // Getters
    public Long getOrderId() {
        return orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getCancelReason() {
        return cancelReason;
    }
}