package com.motorbike.business.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Cart Domain Entity - Business Layer
 * Represents a shopping cart belonging to a user
 * Part of Clean Architecture - Business Layer (independent of frameworks)
 */
public class Cart {
    private Long id;
    private Long userId;
    private List<CartItem> items;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Private constructor for Builder pattern
    private Cart() {
        this.items = new ArrayList<>();
    }
    
    // Business Logic Methods
    
    /**
     * Calculate total amount of all items in cart
     * @return total amount
     */
    public BigDecimal calculateTotalAmount() {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return items.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    /**
     * Get total number of items in cart
     * @return total item count
     */
    public int getTotalItemCount() {
        if (items == null) {
            return 0;
        }
        return items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
    
    /**
     * Check if cart is empty
     * @return true if cart has no items
     */
    public boolean isEmpty() {
        return items == null || items.isEmpty();
    }
    
    /**
     * Check if product already exists in cart
     * @param productId product ID to check
     * @return true if product exists in cart
     */
    public boolean hasProduct(Long productId) {
        if (items == null || productId == null) {
            return false;
        }
        return items.stream()
                .anyMatch(item -> productId.equals(item.getProductId()));
    }
    
    /**
     * Find cart item by product ID
     * @param productId product ID
     * @return cart item or null if not found
     */
    public CartItem findItemByProductId(Long productId) {
        if (items == null || productId == null) {
            return null;
        }
        return items.stream()
                .filter(item -> productId.equals(item.getProductId()))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Add item to cart or update quantity if exists
     * @param item cart item to add
     */
    public void addItem(CartItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Cart item cannot be null");
        }
        
        if (items == null) {
            items = new ArrayList<>();
        }
        
        // If product already exists, increase quantity
        CartItem existingItem = findItemByProductId(item.getProductId());
        if (existingItem != null) {
            existingItem.increaseQuantity(item.getQuantity());
        } else {
            items.add(item);
        }
        
        // Recalculate total
        this.totalAmount = calculateTotalAmount();
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Remove item from cart by product ID
     * @param productId product ID to remove
     */
    public void removeItem(Long productId) {
        if (items == null || productId == null) {
            return;
        }
        items.removeIf(item -> productId.equals(item.getProductId()));
        this.totalAmount = calculateTotalAmount();
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Clear all items from cart
     */
    public void clear() {
        if (items != null) {
            items.clear();
        }
        this.totalAmount = BigDecimal.ZERO;
        this.updatedAt = LocalDateTime.now();
    }
    
    // Builder Pattern
    public static class Builder {
        private final Cart cart = new Cart();
        
        public Builder id(Long id) {
            cart.id = id;
            return this;
        }
        
        public Builder userId(Long userId) {
            cart.userId = userId;
            return this;
        }
        
        public Builder items(List<CartItem> items) {
            cart.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
            return this;
        }
        
        public Builder totalAmount(BigDecimal totalAmount) {
            cart.totalAmount = totalAmount;
            return this;
        }
        
        public Builder createdAt(LocalDateTime createdAt) {
            cart.createdAt = createdAt;
            return this;
        }
        
        public Builder updatedAt(LocalDateTime updatedAt) {
            cart.updatedAt = updatedAt;
            return this;
        }
        
        public Cart build() {
            // Calculate total amount if not set
            if (cart.totalAmount == null) {
                cart.totalAmount = cart.calculateTotalAmount();
            }
            return cart;
        }
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    // Getters
    public Long getId() {
        return id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public List<CartItem> getItems() {
        return items != null ? new ArrayList<>(items) : new ArrayList<>();
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
