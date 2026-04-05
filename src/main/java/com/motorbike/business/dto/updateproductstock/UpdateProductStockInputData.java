package com.motorbike.business.dto.updateproductstock;

public class UpdateProductStockInputData {
    private final Long productId;
    private final int quantityChange;
    private final String operation; // "INCREASE" or "DECREASE"
    
    public UpdateProductStockInputData(Long productId, int quantityChange, String operation) {
        this.productId = productId;
        this.quantityChange = quantityChange;
        this.operation = operation;
    }

    // Constructor with List<GioHang> (for backward compatibility)
    public UpdateProductStockInputData(java.util.List<com.motorbike.domain.entities.GioHang> carts) {
        this.productId = null;  // Will be determined from carts
        this.quantityChange = 0;  // Will be calculated from carts
        this.operation = "DECREASE";
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public int getQuantityChange() {
        return quantityChange;
    }
    
    public String getOperation() {
        return operation;
    }
}
