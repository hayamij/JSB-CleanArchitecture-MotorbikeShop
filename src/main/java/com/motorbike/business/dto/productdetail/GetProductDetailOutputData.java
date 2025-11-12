package com.motorbike.business.dto.productdetail;

import java.math.BigDecimal;

/**
 * Output DTO for Get Product Detail Use Case
 * Carries data OUT OF the use case
 */
public class GetProductDetailOutputData {
    public boolean success;
    public String errorCode;
    public String errorMessage;
    
    // Product details
    public Long productId;
    public String name;
    public String description;
    public BigDecimal price;
    public String imageUrl;
    public String specifications;
    public String category;
    public int stockQuantity;
    public boolean available;
    public boolean inStock;
    
    public GetProductDetailOutputData() {
    }
}
