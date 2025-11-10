package com.motorbike.business.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Order Domain Entity - Business Layer
 * Represents an order placed by a customer
 * Part of Clean Architecture - Business Layer (independent of frameworks)
 */
public class Order {
    private Long id;
    private Long userId;
    private List<OrderItem> items;
    private BigDecimal totalAmount;
    private String shippingAddress;
    private String shippingCity;
    private String shippingPhone;
    private String paymentMethod; // COD, ONLINE
    private String status; // PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED
    private LocalDateTime orderDate;
    private LocalDateTime updatedAt;
    
    // Private constructor for Builder pattern
    private Order() {
        this.items = new ArrayList<>();
    }
    
    // Business Logic Methods
    
    /**
     * Calculate total amount of all items in order
     * @return total amount
     */
    public BigDecimal calculateTotalAmount() {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    /**
     * Get total number of items in order
     * @return total item count
     */
    public int getTotalItemCount() {
        if (items == null) {
            return 0;
        }
        return items.stream()
                .mapToInt(OrderItem::getQuantity)
                .sum();
    }
    
    /**
     * Check if order is empty
     * @return true if order has no items
     */
    public boolean isEmpty() {
        return items == null || items.isEmpty();
    }
    
    /**
     * Add item to order
     * @param item order item to add
     */
    public void addItem(OrderItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Order item cannot be null");
        }
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(item);
        this.totalAmount = calculateTotalAmount();
    }
    
    /**
     * Validate order data
     * @throws IllegalArgumentException if order data is invalid
     */
    public void validate() {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one item");
        }
        if (shippingAddress == null || shippingAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Shipping address is required");
        }
        if (shippingCity == null || shippingCity.trim().isEmpty()) {
            throw new IllegalArgumentException("Shipping city is required");
        }
        if (shippingPhone == null || shippingPhone.trim().isEmpty()) {
            throw new IllegalArgumentException("Shipping phone is required");
        }
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            throw new IllegalArgumentException("Payment method is required");
        }
        if (!isValidPaymentMethod(paymentMethod)) {
            throw new IllegalArgumentException("Invalid payment method. Must be COD or ONLINE");
        }
    }
    
    /**
     * Check if payment method is valid
     * @param method payment method
     * @return true if valid
     */
    private boolean isValidPaymentMethod(String method) {
        return "COD".equalsIgnoreCase(method) || "ONLINE".equalsIgnoreCase(method);
    }
    
    /**
     * Check if order can be cancelled
     * @return true if order can be cancelled
     */
    public boolean canBeCancelled() {
        return "PENDING".equalsIgnoreCase(status) || "PROCESSING".equalsIgnoreCase(status);
    }
    
    // Builder Pattern
    public static class Builder {
        private final Order order = new Order();
        
        public Builder id(Long id) {
            order.id = id;
            return this;
        }
        
        public Builder userId(Long userId) {
            order.userId = userId;
            return this;
        }
        
        public Builder items(List<OrderItem> items) {
            order.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
            return this;
        }
        
        public Builder totalAmount(BigDecimal totalAmount) {
            order.totalAmount = totalAmount;
            return this;
        }
        
        public Builder shippingAddress(String shippingAddress) {
            order.shippingAddress = shippingAddress;
            return this;
        }
        
        public Builder shippingCity(String shippingCity) {
            order.shippingCity = shippingCity;
            return this;
        }
        
        public Builder shippingPhone(String shippingPhone) {
            order.shippingPhone = shippingPhone;
            return this;
        }
        
        public Builder paymentMethod(String paymentMethod) {
            order.paymentMethod = paymentMethod;
            return this;
        }
        
        public Builder status(String status) {
            order.status = status;
            return this;
        }
        
        public Builder orderDate(LocalDateTime orderDate) {
            order.orderDate = orderDate;
            return this;
        }
        
        public Builder updatedAt(LocalDateTime updatedAt) {
            order.updatedAt = updatedAt;
            return this;
        }
        
        public Order build() {
            // Set defaults
            if (order.status == null) {
                order.status = "PENDING";
            }
            if (order.orderDate == null) {
                order.orderDate = LocalDateTime.now();
            }
            if (order.updatedAt == null) {
                order.updatedAt = LocalDateTime.now();
            }
            // Calculate total amount if not set
            if (order.totalAmount == null) {
                order.totalAmount = order.calculateTotalAmount();
            }
            return order;
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
    
    public List<OrderItem> getItems() {
        return items != null ? new ArrayList<>(items) : new ArrayList<>();
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public String getShippingAddress() {
        return shippingAddress;
    }
    
    public String getShippingCity() {
        return shippingCity;
    }
    
    public String getShippingPhone() {
        return shippingPhone;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public String getStatus() {
        return status;
    }
    
    public LocalDateTime getOrderDate() {
        return orderDate;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
