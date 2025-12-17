package com.motorbike.business.dto.calculateorderstats;

import java.math.BigDecimal;

/**
 * UC-80: Calculate Order Statistics - Output Data
 * Contains calculated statistics for orders
 */
public class CalculateOrderStatisticsOutputData {
    
    private final boolean success;
    private final String errorCode;
    private final String errorMessage;
    private final BigDecimal totalRevenue;
    private final int orderCount;
    private final int totalItems;
    
    private CalculateOrderStatisticsOutputData(
            boolean success,
            String errorCode,
            String errorMessage,
            BigDecimal totalRevenue,
            int orderCount,
            int totalItems) {
        this.success = success;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.totalRevenue = totalRevenue;
        this.orderCount = orderCount;
        this.totalItems = totalItems;
    }
    
    public static CalculateOrderStatisticsOutputData forSuccess(
            BigDecimal totalRevenue,
            int orderCount,
            int totalItems) {
        return new CalculateOrderStatisticsOutputData(true, null, null, totalRevenue, orderCount, totalItems);
    }
    
    public static CalculateOrderStatisticsOutputData forError(String errorCode, String errorMessage) {
        return new CalculateOrderStatisticsOutputData(false, errorCode, errorMessage, BigDecimal.ZERO, 0, 0);
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }
    
    public int getOrderCount() {
        return orderCount;
    }
    
    public int getTotalItems() {
        return totalItems;
    }
}
