package com.motorbike.business.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * CartItem Domain Entity - Business Layer
 * Represents an item in the shopping cart
 * Part of Clean Architecture - Business Layer (independent of frameworks)
 */
public class CartItem {
    private Long id;
    private Long cartId;
    private Long productId;
    private String productName;
    private BigDecimal productPrice;
    private Integer quantity;
    private BigDecimal subtotal;
    private LocalDateTime addedAt;
    
    // Private constructor for Builder pattern
    private CartItem() {}
    
    // Business Logic Methods
    
    /**
     * Calculate subtotal for this cart item
     * @return subtotal = price * quantity
     */
    public BigDecimal calculateSubtotal() {
        if (productPrice == null || quantity == null) {
            return BigDecimal.ZERO;
        }
        return productPrice.multiply(BigDecimal.valueOf(quantity));
    }
    
    /**
     * Check if quantity is valid (> 0)
     * @return true if quantity is valid
     */
    public boolean hasValidQuantity() {
        return quantity != null && quantity > 0;
    }
    
    /**
     * Update quantity and recalculate subtotal
     * @param newQuantity new quantity
     */
    public void updateQuantity(int newQuantity) {
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.quantity = newQuantity;
        this.subtotal = calculateSubtotal();
    }
    
    /**
     * Increase quantity by specified amount
     * @param amount amount to increase
     */
    public void increaseQuantity(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        this.quantity += amount;
        this.subtotal = calculateSubtotal();
    }
    
    // Builder Pattern
    public static class Builder {
        private final CartItem item = new CartItem();
        
        public Builder id(Long id) {
            item.id = id;
            return this;
        }
        
        public Builder cartId(Long cartId) {
            item.cartId = cartId;
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
        
        public Builder addedAt(LocalDateTime addedAt) {
            item.addedAt = addedAt;
            return this;
        }
        
        public CartItem build() {
            // Calculate subtotal if not set
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
    
    public Long getCartId() {
        return cartId;
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
    
    public LocalDateTime getAddedAt() {
        return addedAt;
    }
}
