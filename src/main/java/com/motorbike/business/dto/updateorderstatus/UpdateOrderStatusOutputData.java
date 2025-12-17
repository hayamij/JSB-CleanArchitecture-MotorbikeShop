package com.motorbike.business.dto.updateorderstatus;

import com.motorbike.domain.entities.DonHang;

public class UpdateOrderStatusOutputData {
    private final Long orderId;
    private final String previousStatus;
    private final String newStatus;
    private final boolean success;
    private final String errorCode;
    private final String errorMessage;
    
    public UpdateOrderStatusOutputData(DonHang order, String previousStatus) {
        this.orderId = order.getMaDonHang();
        this.previousStatus = previousStatus;
        this.newStatus = order.getTrangThai().name();
        this.success = true;
        this.errorCode = null;
        this.errorMessage = null;
    }
    
    private UpdateOrderStatusOutputData(String errorCode, String errorMessage) {
        this.orderId = null;
        this.previousStatus = null;
        this.newStatus = null;
        this.success = false;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
    
    public static UpdateOrderStatusOutputData forError(String errorCode, String errorMessage) {
        return new UpdateOrderStatusOutputData(errorCode, errorMessage);
    }
    
    public boolean isSuccess() { return success; }
    public Long getOrderId() { return orderId; }
    public String getPreviousStatus() { return previousStatus; }
    public String getNewStatus() { return newStatus; }
    public String getErrorCode() { return errorCode; }
    public String getErrorMessage() { return errorMessage; }
}
