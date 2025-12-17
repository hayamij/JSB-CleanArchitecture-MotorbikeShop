package com.motorbike.business.dto.reducestock;

public class ReduceProductStockInputData {
    private final Long productId;
    private final int quantity;
    
    public ReduceProductStockInputData(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public int getQuantity() {
        return quantity;
    }
}
