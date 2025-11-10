package com.motorbike.interfaceadapters.dto;

/**
 * Add to Cart Request DTO - Interface Adapters Layer
 * Request DTO for adding items to cart
 * Part of Clean Architecture - Interface Adapters Layer
 */
public class AddToCartRequestDTO {
    private Long userId;
    private Long productId;
    private Integer quantity;
    
    // Constructors
    public AddToCartRequestDTO() {}
    
    public AddToCartRequestDTO(Long userId, Long productId, Integer quantity) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }
    
    // Getters and Setters
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
