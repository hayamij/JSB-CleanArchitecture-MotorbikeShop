package com.motorbike.adapters.dto.response;

public class UpdateOrderInforResponse {
    private final boolean success;
    private final Long orderId;
    private final Long customerId;
    private final String receiverName;
    private final String phoneNumber;
    private final String shippingAddress;
    private final String note;
    private final String orderStatus;
    private final String updatedAt;
    private final String message;
    private final String errorCode;
    private final String errorMessage;

    public UpdateOrderInforResponse(boolean success,
                                    Long orderId,
                                    Long customerId,
                                    String receiverName,
                                    String phoneNumber,
                                    String shippingAddress,
                                    String note,
                                    String orderStatus,
                                    String updatedAt,
                                    String message,
                                    String errorCode,
                                    String errorMessage) {
        this.success = success;
        this.orderId = orderId;
        this.customerId = customerId;
        this.receiverName = receiverName;
        this.phoneNumber = phoneNumber;
        this.shippingAddress = shippingAddress;
        this.note = note;
        this.orderStatus = orderStatus;
        this.updatedAt = updatedAt;
        this.message = message;
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
    public String getUpdatedAt() {return updatedAt;}
    public String getMessage() {return message;}
    public String getErrorCode() {return errorCode;}
    public String getErrorMessage() {return errorMessage;}
}
