package com.motorbike.business.dto.product;

public class ToggleProductVisibilityInputData {
    private final Long productId;

    public ToggleProductVisibilityInputData(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}
