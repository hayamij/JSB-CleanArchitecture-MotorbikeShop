package com.motorbike.business.dto.reducestock;

public class ReduceProductStockOutputData {
    private final Long productId;
    private final String productName;
    private final int newStock;
    private final boolean success;
    private final String errorCode;
    private final String errorMessage;
    
    // Success constructor
    public ReduceProductStockOutputData(Long productId, String productName, int newStock) {
        this.productId = productId;
        this.productName = productName;
        this.newStock = newStock;
        this.success = true;
        this.errorCode = null;
        this.errorMessage = null;
    }
    
    // Error constructor
    private ReduceProductStockOutputData(String errorCode, String errorMessage) {
        this.productId = null;
        this.productName = null;
        this.newStock = 0;
        this.success = false;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
    
    public static ReduceProductStockOutputData forError(String errorCode, String errorMessage) {
        return new ReduceProductStockOutputData(errorCode, errorMessage);
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public int getNewStock() {
        return newStock;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
}
