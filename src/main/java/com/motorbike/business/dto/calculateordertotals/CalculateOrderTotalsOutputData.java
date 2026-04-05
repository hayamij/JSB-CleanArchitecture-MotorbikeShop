package com.motorbike.business.dto.calculateordertotals;

import java.math.BigDecimal;

public class CalculateOrderTotalsOutputData {
    private final int totalItems;
    private final int totalQuantity;
    private final BigDecimal totalAmount;
    private final boolean success;
    private final String errorCode;
    private final String errorMessage;
    
    public CalculateOrderTotalsOutputData(int totalItems, int totalQuantity, BigDecimal totalAmount) {
        this.totalItems = totalItems;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
        this.success = true;
        this.errorCode = null;
        this.errorMessage = null;
    }
    
    private CalculateOrderTotalsOutputData(String errorCode, String errorMessage) {
        this.totalItems = 0;
        this.totalQuantity = 0;
        this.totalAmount = BigDecimal.ZERO;
        this.success = false;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
    
    public static CalculateOrderTotalsOutputData forError(String errorCode, String errorMessage) {
        return new CalculateOrderTotalsOutputData(errorCode, errorMessage);
    }
    
    public boolean isSuccess() { return success; }
    public int getTotalItems() { return totalItems; }
    public int getTotalQuantity() { return totalQuantity; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public String getErrorCode() { return errorCode; }
    public String getErrorMessage() { return errorMessage; }
}
