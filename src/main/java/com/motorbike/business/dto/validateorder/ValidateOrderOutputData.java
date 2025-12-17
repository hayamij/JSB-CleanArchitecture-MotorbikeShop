package com.motorbike.business.dto.validateorder;

public class ValidateOrderOutputData {
    private final boolean valid;
    private final String message;
    private final Long orderId;
    
    public ValidateOrderOutputData(boolean valid, String message, Long orderId) {
        this.valid = valid;
        this.message = message;
        this.orderId = orderId;
    }
    
    public boolean isValid() {
        return valid;
    }
    
    public String getMessage() {
        return message;
    }
    
    public Long getOrderId() {
        return orderId;
    }
}
