package com.motorbike.business.dto.findcartitem;

public class FindCartItemInputData {
    private final Long userId;
    private final Long productId;
    
    public FindCartItemInputData(Long userId, Long productId) {
        this.userId = userId;
        this.productId = productId;
    }
    
    // Constructor overload for single product ID
    public FindCartItemInputData(Long productId) {
        this.userId = null;
        this.productId = productId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public Long getCartId() {
        return userId; // cartId is same as userId in this context
    }
}
