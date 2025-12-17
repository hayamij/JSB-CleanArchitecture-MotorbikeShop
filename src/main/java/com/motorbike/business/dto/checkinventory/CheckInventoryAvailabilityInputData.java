package com.motorbike.business.dto.checkinventory;

public class CheckInventoryAvailabilityInputData {
    private final Long productId;
    private final int requestedQuantity;

    public CheckInventoryAvailabilityInputData(Long productId, int requestedQuantity) {
        this.productId = productId;
        this.requestedQuantity = requestedQuantity;
    }

    public Long getProductId() {
        return productId;
    }

    public int getRequestedQuantity() {
        return requestedQuantity;
    }
}
