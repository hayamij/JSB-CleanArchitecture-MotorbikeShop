package com.motorbike.business.dto.checkinventory;

public class CheckInventoryAvailabilityOutputData {
    private final boolean success;
    private final boolean available;
    private final int availableStock;
    private final String productName;
    private final Long productId;
    private final String errorCode;
    private final String errorMessage;

    // Success constructor
    public CheckInventoryAvailabilityOutputData(Long productId, String productName, 
                                                boolean available, int availableStock) {
        this.success = true;
        this.productId = productId;
        this.productName = productName;
        this.available = available;
        this.availableStock = availableStock;
        this.errorCode = null;
        this.errorMessage = null;
    }

    // Error constructor
    public CheckInventoryAvailabilityOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.productId = null;
        this.productName = null;
        this.available = false;
        this.availableStock = 0;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isAvailable() {
        return available;
    }

    public int getAvailableStock() {
        return availableStock;
    }

    public String getProductName() {
        return productName;
    }

    public Long getProductId() {
        return productId;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public static CheckInventoryAvailabilityOutputData forError(String errorCode, String errorMessage) {
        return new CheckInventoryAvailabilityOutputData(errorCode, errorMessage);
    }
}
