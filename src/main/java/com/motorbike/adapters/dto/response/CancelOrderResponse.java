package com.motorbike.adapters.dto.response;

public class CancelOrderResponse {
    private boolean success;
    private Long orderId;
    private String orderStatus;
    private String formattedRefundAmount;
    private String message;
    private String errorCode;
    private String errorMessage;

    public CancelOrderResponse(boolean success, Long orderId, String orderStatus,
                              String formattedRefundAmount, String message,
                              String errorCode, String errorMessage) {
        this.success = success;
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.formattedRefundAmount = formattedRefundAmount;
        this.message = message;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public Long getOrderId() {return orderId;}

    public String getOrderStatus() {return orderStatus;}

    public String getFormattedRefundAmount() {return formattedRefundAmount;}

    public String getMessage() {return message;}

    public String getErrorCode() {return errorCode;}

    public String getErrorMessage() {return errorMessage;}
}
