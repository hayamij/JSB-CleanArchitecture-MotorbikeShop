package com.motorbike.business.dto.calculatecarttotals;

import java.math.BigDecimal;

public class CalculateCartTotalsOutputData {
    private final boolean success;
    private final int totalItems;
    private final int totalQuantity;
    private final BigDecimal totalAmount;
    private final String errorCode;
    private final String errorMessage;

    // Success constructor
    public CalculateCartTotalsOutputData(int totalItems, int totalQuantity, BigDecimal totalAmount) {
        this.success = true;
        this.totalItems = totalItems;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
        this.errorCode = null;
        this.errorMessage = null;
    }

    // Error constructor
    public CalculateCartTotalsOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.totalItems = 0;
        this.totalQuantity = 0;
        this.totalAmount = BigDecimal.ZERO;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public static CalculateCartTotalsOutputData forError(String errorCode, String errorMessage) {
        return new CalculateCartTotalsOutputData(errorCode, errorMessage);
    }
}
