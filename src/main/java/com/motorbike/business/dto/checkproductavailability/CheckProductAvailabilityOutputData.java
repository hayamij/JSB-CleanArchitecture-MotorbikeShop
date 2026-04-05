package com.motorbike.business.dto.checkproductavailability;

public class CheckProductAvailabilityOutputData {
    private final boolean available;
    private final String message;
    private final Long productId;
    private final int availableQuantity;
    
    public CheckProductAvailabilityOutputData(boolean available, String message, Long productId, int availableQuantity) {
        this.available = available;
        this.message = message;
        this.productId = productId;
        this.availableQuantity = availableQuantity;
    }
    
    public boolean isAvailable() {
        return available;
    }
    
    public String getMessage() {
        return message;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public int getAvailableQuantity() {
        return availableQuantity;
    }
}
