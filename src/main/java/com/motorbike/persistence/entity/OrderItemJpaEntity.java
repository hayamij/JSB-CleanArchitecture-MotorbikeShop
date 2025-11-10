package com.motorbike.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * OrderItem JPA Entity - Persistence Layer
 * Maps to 'order_items' table in database
 * Part of Clean Architecture - Persistence Layer (framework-dependent)
 */
@Entity
@Table(name = "order_items")
public class OrderItemJpaEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private OrderJpaEntity order;
    
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    @Column(name = "product_name", nullable = false)
    private String productName;
    
    @Column(name = "product_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal productPrice;
    
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
    
    @Column(name = "subtotal", nullable = false, precision = 15, scale = 2)
    private BigDecimal subtotal;
    
    @PrePersist
    protected void onCreate() {
        if (subtotal == null && productPrice != null && quantity != null) {
            subtotal = productPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }
    
    // Constructors
    public OrderItemJpaEntity() {
    }
    
    // Helper method to get orderId for mapping
    public Long getOrderId() {
        return order != null ? order.getId() : null;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public OrderJpaEntity getOrder() {
        return order;
    }
    
    public void setOrder(OrderJpaEntity order) {
        this.order = order;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public BigDecimal getProductPrice() {
        return productPrice;
    }
    
    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public BigDecimal getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}
