package com.motorbike.business.dto.validatecartitem;

public class ValidateCartItemInputData {
    private final Long userId;
    private final Long productId;
    private final int quantity;
    
    public ValidateCartItemInputData(Long userId, Long productId, int quantity) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }

    // Constructor with 2 parameters (for backward compatibility)
    public ValidateCartItemInputData(Long productId, int quantity) {
        this.userId = null;
        this.productId = productId;
        this.quantity = quantity;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public int getQuantity() {
        return quantity;
    }
}
