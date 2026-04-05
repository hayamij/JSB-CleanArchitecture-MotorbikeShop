package com.motorbike.business.dto.updateproductstock;

public class UpdateProductStockOutputData {
    private final boolean success;
    private final String message;
    private final Long productId;
    private final int newStock;
    
    public UpdateProductStockOutputData(boolean success, String message, Long productId, int newStock) {
        this.success = success;
        this.message = message;
        this.productId = productId;
        this.newStock = newStock;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public int getNewStock() {
        return newStock;
    }
}
