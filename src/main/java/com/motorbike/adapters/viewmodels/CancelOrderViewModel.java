package com.motorbike.adapters.viewmodels;

/**
 * View Model for Cancel Order Feature
 * Contains already-formatted data ready for UI display
 * NO business logic - pure data container
 */
public class CancelOrderViewModel {
    
    // Success state
    public boolean success;
    public String message;
    
    // Order information (only if success)
    public Long orderId;
    public Long customerId;
    public String orderStatus;
    public String formattedRefundAmount;
    public String cancelReason;
    public String cancelledAtDisplay;
    
    // Error information (only if not success)
    public boolean hasError;
    public String errorCode;
    public String errorMessage;
    public String errorColor;
    
    // Constructor
    public CancelOrderViewModel() {
        this.success = false;
        this.hasError = false;
    }
}