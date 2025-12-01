package com.motorbike.adapters.dto.request;

public class CancelOrderRequest {
    private Long userId;
    private String cancelReason;

    public Long getUserId() {return userId;}

    public void setUserId(Long userId) {this.userId = userId;}

    public String getCancelReason() {return cancelReason;}

    public void setCancelReason(String cancelReason) {this.cancelReason = cancelReason;}
}
