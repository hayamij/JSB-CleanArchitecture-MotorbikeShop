package com.motorbike.business.dto.checkproductavailability;

import com.motorbike.domain.entities.SanPham;

public class CheckProductAvailabilityInputData {
    private final Long productId;
    private final int requestedQuantity;
    
    public CheckProductAvailabilityInputData(Long productId, int requestedQuantity) {
        this.productId = productId;
        this.requestedQuantity = requestedQuantity;
    }
    
    // Constructor for tests accepting SanPham
    public CheckProductAvailabilityInputData(SanPham product) {
        this.productId = product != null ? product.getMaSanPham() : null;
        this.requestedQuantity = product != null ? product.getSoLuongTonKho() : 0;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public int getRequestedQuantity() {
        return requestedQuantity;
    }
}
