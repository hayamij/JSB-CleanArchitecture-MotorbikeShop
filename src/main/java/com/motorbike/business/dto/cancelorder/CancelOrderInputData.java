package com.motorbike.business.dto.cancelorder;

public class CancelOrderInputData {
    private final Long orderId;
    private final Long userId;
    private final String cancelReason;

    public CancelOrderInputData(Long orderId, Long userId, String cancelReason) {
        this.orderId = orderId;
        this.userId = userId;
        this.cancelReason = cancelReason;
    }

    public Long getOrderId() {return orderId;}

    public Long getUserId() {return userId;}

    public String getCancelReason() {return cancelReason;}
}
