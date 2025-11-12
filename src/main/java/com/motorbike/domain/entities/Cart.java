package com.motorbike.domain.entities;

import com.motorbike.domain.exceptions.InvalidCartException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Domain Entity: Cart (Shopping Cart)
 * Contains pure business logic for Cart entity
 * NO dependencies on frameworks or UI
 */
public class Cart {
    private Long id;
    private Long userId;
    private List<CartItem> items;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // constructor for new cart
    public Cart(Long userId) {
        this.userId = userId;
        this.items = new ArrayList<>();
        this.totalAmount = BigDecimal.ZERO;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Full constructor
    public Cart(Long id, Long userId, List<CartItem> items, 
                BigDecimal totalAmount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.items = items != null ? items : new ArrayList<>();
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Add item to cart
    public void addItem(CartItem item) {
        if (item == null) {
            throw new InvalidCartException("NULL_ITEM", "Cart item ko được null");
        }
        
        // Check if product already exists in cart
        CartItem existingItem = findItemByProductId(item.getProductId());
        if (existingItem != null) {
            // Update quantity of existing item
            existingItem.increaseQuantity(item.getQuantity());
        } else {
            // Add new item
            this.items.add(item);
        }
        
        recalculateTotal();
        this.updatedAt = LocalDateTime.now();
    }

    // Remove item from cart
    public void removeItem(Long productId) {
        if (productId == null) {
            throw new InvalidCartException("NULL_PRODUCT_ID", "Product ID ko được null");
        }
        
        CartItem item = findItemByProductId(productId);
        if (item != null) {
            this.items.remove(item);
            recalculateTotal();
            this.updatedAt = LocalDateTime.now();
        }
    }

    // Update item quantity
    public void updateItemQuantity(Long productId, int newQuantity) {
        if (productId == null) {
            throw new InvalidCartException("NULL_PRODUCT_ID", "Product ID ko được null");
        }
        
        if (newQuantity < 0) {
            throw new InvalidCartException("NEGATIVE_QUANTITY", "Quantity ko được âm");
        }
        
        if (newQuantity == 0) {
            // Remove item if quantity is 0
            removeItem(productId);
            return;
        }
        
        CartItem item = findItemByProductId(productId);
        if (item != null) {
            item.setQuantity(newQuantity);
            recalculateTotal();
            this.updatedAt = LocalDateTime.now();
        } else {
            throw new InvalidCartException("ITEM_NOT_FOUND", "Product not found in cart");
        }
    }

    // Clear all items
    public void clear() {
        this.items.clear();
        this.totalAmount = BigDecimal.ZERO;
        this.updatedAt = LocalDateTime.now();
    }

    // Check if cart is empty
    public boolean isEmpty() {
        return this.items == null || this.items.isEmpty();
    }

    // Get total item count
    public int getTotalItemCount() {
        return items.stream()
                   .mapToInt(CartItem::getQuantity)
                   .sum();
    }

    // Get item count (number of different products)
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    // Assign cart to user (for guest cart conversion)
    public void assignToUser(Long userId) {
        if (userId == null) {
            throw new InvalidCartException("NULL_USER_ID", "User ID ko được null");
        }
        this.userId = userId;
        this.updatedAt = LocalDateTime.now();
    }

    // Validate cart for checkout
    public void validateForCheckout() {
        if (isEmpty()) {
            throw new InvalidCartException("EMPTY_CART", "Cart is empty - cannot checkout");
        }
        
        // Validate each item
        for (CartItem item : items) {
            item.validate();
        }
    }

    // helper: Find item by product ID
    private CartItem findItemByProductId(Long productId) {
        return items.stream()
                   .filter(item -> item.getProductId().equals(productId))
                   .findFirst()
                   .orElse(null);
    }

    // helper: Recalculate total amount
    private void recalculateTotal() {
        this.totalAmount = items.stream()
                               .map(CartItem::getSubtotal)
                               .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // getters
    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public List<CartItem> getItems() {
        return new ArrayList<>(items); // Return copy to prevent external modification
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

    // setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setItems(List<CartItem> items) {
        this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
        recalculateTotal();
        this.updatedAt = LocalDateTime.now();
    }
}
