package com.motorbike.adapters.viewmodels;

public class CancelOrderViewModel {
    
    public boolean success;
    public String message;
    
    public Long orderId;
    public Long customerId;
    public String orderStatus;
    public String formattedRefundAmount;
    public String cancelReason;
    public String cancelledAtDisplay;
    
    public boolean hasError;
    public String errorCode;
    public String errorMessage;
    public String errorColor;
    
    public CancelOrderViewModel() {
        this.success = false;
        this.hasError = false;
    }
}
