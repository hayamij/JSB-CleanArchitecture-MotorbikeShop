package com.motorbike.business.entity;

import java.math.BigDecimal;

/**
 * OrderItem Domain Entity - Business Layer
 * Represents an item in an order
 * Part of Clean Architecture - Business Layer (independent of frameworks)
 */
public class OrderItem {
    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    private Integer quantity;
    private BigDecimal subtotal;
    
    // Private constructor for Builder pattern
    private OrderItem() {
    }
    
    // Business Logic Methods
    
    /**
     * Calculate subtotal (price * quantity)
     * @return subtotal amount
     */
    public BigDecimal calculateSubtotal() {
        if (productPrice == null || quantity == null) {
            return BigDecimal.ZERO;
        }
        return productPrice.multiply(BigDecimal.valueOf(quantity));
    }
    
    /**
     * Validate order item data
     * @throws IllegalArgumentException if data is invalid
     */
    public void validate() {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        if (productName == null || productName.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        if (productPrice == null || productPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Product price must be greater than zero");
        }
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
    }
    
    /**
     * Check if quantity is valid (positive number)
     * @return true if quantity is valid
     */
    public boolean hasValidQuantity() {
        return quantity != null && quantity > 0;
    }
    
    // Builder Pattern
    public static class Builder {
        private final OrderItem item = new OrderItem();
        
        public Builder id(Long id) {
            item.id = id;
            return this;
        }
        
        public Builder productId(Long productId) {
            item.productId = productId;
            return this;
        }
        
        public Builder productName(String productName) {
            item.productName = productName;
            return this;
        }
        
        public Builder productPrice(BigDecimal productPrice) {
            item.productPrice = productPrice;
            return this;
        }
        
        public Builder quantity(Integer quantity) {
            item.quantity = quantity;
            return this;
        }
        
        public Builder subtotal(BigDecimal subtotal) {
            item.subtotal = subtotal;
            return this;
        }
        
        public OrderItem build() {
            // Auto-calculate subtotal if not set
            if (item.subtotal == null) {
                item.subtotal = item.calculateSubtotal();
            }
            return item;
        }
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    // Getters
    public Long getId() {
        return id;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public BigDecimal getProductPrice() {
        return productPrice;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public BigDecimal getSubtotal() {
        return subtotal;
    }
}
