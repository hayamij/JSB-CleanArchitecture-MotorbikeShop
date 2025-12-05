package com.motorbike.business.dto.updateorderinfor;

import java.time.LocalDateTime;

public class UpdateOrderInforOutputData {
    private final boolean success;
    private final Long orderId;
    private final Long customerId;
    private final String receiverName;
    private final String phoneNumber;
    private final String shippingAddress;
    private final String note;
    private final String orderStatus;
    private final LocalDateTime updatedAt;
    private final String errorCode;
    private final String errorMessage;

    public UpdateOrderInforOutputData(Long orderId, Long customerId, String receiverName,
                                      String phoneNumber, String shippingAddress, String note,
                                      String orderStatus, LocalDateTime updatedAt) {
        this.success = true;
        this.orderId = orderId;
        this.customerId = customerId;
        this.receiverName = receiverName;
        this.phoneNumber = phoneNumber;
        this.shippingAddress = shippingAddress;
        this.note = note;
        this.orderStatus = orderStatus;
        this.updatedAt = updatedAt;
        this.errorCode = null;
        this.errorMessage = null;
    }

    public UpdateOrderInforOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.orderId = null;
        this.customerId = null;
        this.receiverName = null;
        this.phoneNumber = null;
        this.shippingAddress = null;
        this.note = null;
        this.orderStatus = null;
        this.updatedAt = null;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {return success;}
    public Long getOrderId() {return orderId;}
    public Long getCustomerId() {return customerId;}
    public String getReceiverName() {return receiverName;}
    public String getPhoneNumber() {return phoneNumber;}
    public String getShippingAddress() {return shippingAddress;}
    public String getNote() {return note;}
    public String getOrderStatus() {return orderStatus;}
    public LocalDateTime getUpdatedAt() {return updatedAt;}
    public String getErrorCode() {return errorCode;}
    public String getErrorMessage() {return errorMessage;}

    public static UpdateOrderInforOutputData forSuccess(Long orderId, Long customerId, String receiverName,
                                                        String phoneNumber, String shippingAddress, String note,
                                                        String orderStatus, LocalDateTime updatedAt) {
        return new UpdateOrderInforOutputData(orderId, customerId, receiverName, phoneNumber,
                shippingAddress, note, orderStatus, updatedAt);
    }

    public static UpdateOrderInforOutputData forError(String errorCode, String errorMessage) {
        return new UpdateOrderInforOutputData(errorCode, errorMessage);
    }
}
