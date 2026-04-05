package com.motorbike.business.dto.removecartitem;

public class RemoveCartItemInputData {
    private final Long userId;
    private final Long productId;
    
    public RemoveCartItemInputData(Long userId, Long productId) {
        this.userId = userId;
        this.productId = productId;
    }

    // Constructor with single Long parameter (productId only, for backward compatibility)
    public RemoveCartItemInputData(Long productId) {
        this.userId = null;
        this.productId = productId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public Long getProductId() {
        return productId;
    }
}
