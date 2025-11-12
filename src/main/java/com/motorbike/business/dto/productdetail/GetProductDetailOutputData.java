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
    
    // Static factory methods
    public static GetProductDetailOutputData forSuccess(
            Long productId, String name, String description, String specifications,
            BigDecimal originalPrice, BigDecimal discountedPrice, double discountPercent,
            int stockQuantity, boolean available) {
        GetProductDetailOutputData data = new GetProductDetailOutputData();
        data.success = true;
        data.productId = productId;
        data.name = name;
        data.description = description;
        data.specifications = specifications;
        data.price = discountedPrice; // Use discounted price as final price
        data.stockQuantity = stockQuantity;
        data.available = available;
        data.inStock = stockQuantity > 0;
        return data;
    }
    
    public static GetProductDetailOutputData forError(String errorCode, String errorMessage) {
        GetProductDetailOutputData data = new GetProductDetailOutputData();
        data.success = false;
        data.errorCode = errorCode;
        data.errorMessage = errorMessage;
        return data;
    }
}
