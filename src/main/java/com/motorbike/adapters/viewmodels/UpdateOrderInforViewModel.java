package com.motorbike.adapters.viewmodels;

public class UpdateOrderInforViewModel {

    public boolean success;
    public String message;

    public Long orderId;
    public Long customerId;
    public String receiverName;
    public String phoneNumber;
    public String shippingAddress;
    public String note;
    public String orderStatus;
    public String updatedAtDisplay;

    public boolean hasError;
    public String errorCode;
    public String errorMessage;
    public String errorColor;

    public UpdateOrderInforViewModel() {
        this.success = false;
        this.hasError = false;
    }
}
