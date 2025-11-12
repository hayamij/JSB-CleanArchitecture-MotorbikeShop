package com.motorbike.business.dto.productdetail;

/**
 * Input DTO for Get Product Detail Use Case
 * Carries data INTO the use case
 */
public class GetProductDetailInputData {
    public Long productId;
    
    public GetProductDetailInputData() {
    }
    
    public GetProductDetailInputData(Long productId) {
        this.productId = productId;
    }
}
