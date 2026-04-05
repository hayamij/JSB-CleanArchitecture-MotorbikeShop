package com.motorbike.business.dto.sortorders;

import com.motorbike.domain.entities.DonHang;
import java.util.List;

/**
 * UC-79: Sort Orders By Date - Output Data
 * Contains sorted orders
 */
public class SortOrdersByDateOutputData {
    
    private final boolean success;
    private final String errorCode;
    private final String errorMessage;
    private final List<DonHang> sortedOrders;
    
    private SortOrdersByDateOutputData(
            boolean success,
            String errorCode,
            String errorMessage,
            List<DonHang> sortedOrders) {
        this.success = success;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.sortedOrders = sortedOrders;
    }
    
    public static SortOrdersByDateOutputData forSuccess(List<DonHang> sortedOrders) {
        return new SortOrdersByDateOutputData(true, null, null, sortedOrders);
    }
    
    public static SortOrdersByDateOutputData forError(String errorCode, String errorMessage) {
        return new SortOrdersByDateOutputData(false, errorCode, errorMessage, null);
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
    
    public List<DonHang> getSortedOrders() {
        return sortedOrders;
    }
}
