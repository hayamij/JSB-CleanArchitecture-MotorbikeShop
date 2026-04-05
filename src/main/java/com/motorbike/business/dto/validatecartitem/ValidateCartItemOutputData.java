package com.motorbike.business.dto.validatecartitem;

public class ValidateCartItemOutputData {
    private final boolean valid;
    private final String message;
    private final Long productId;
    
    public ValidateCartItemOutputData(boolean valid, String message, Long productId) {
        this.valid = valid;
        this.message = message;
        this.productId = productId;
    }
    
    // Constructor with just message (no productId)
    public ValidateCartItemOutputData(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
        this.productId = null;
    }
    
    public boolean isValid() {
        return valid;
    }
    
    public String getMessage() {
        return message;
    }
    
    public Long getProductId() {
        return productId;
    }
}
