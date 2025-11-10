package com.motorbike.business.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Domain Entity: Product
 * Represents a product in the motorbike shop (motorcycle or accessory)
 * This is a pure domain object with business logic, independent of frameworks
 */
public class Product {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private String specifications; // JSON or text format for technical specs
    private String category; // e.g., "MOTORCYCLE", "ACCESSORY"
    private Integer stockQuantity;
    private boolean available;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Private constructor for builder pattern
    private Product(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.price = builder.price;
        this.imageUrl = builder.imageUrl;
        this.specifications = builder.specifications;
        this.category = builder.category;
        this.stockQuantity = builder.stockQuantity;
        this.available = builder.available;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    // Default constructor for frameworks
    public Product() {
    }

    // Business logic methods
    public boolean isInStock() {
        return available && stockQuantity != null && stockQuantity > 0;
    }

    public boolean canPurchase(int requestedQuantity) {
        return isInStock() && stockQuantity >= requestedQuantity;
    }

    public void decreaseStock(int quantity) {
        if (!canPurchase(quantity)) {
            throw new IllegalStateException("Not enough stock available");
        }
        this.stockQuantity -= quantity;
        if (this.stockQuantity == 0) {
            this.available = false;
        }
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getSpecifications() {
        return specifications;
    }

    public String getCategory() {
        return category;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public boolean isAvailable() {
        return available;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Setters (for frameworks and infrastructure)
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Builder pattern
    public static class Builder {
        private Long id;
        private String name;
        private String description;
        private BigDecimal price;
        private String imageUrl;
        private String specifications;
        private String category;
        private Integer stockQuantity;
        private boolean available = true;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder specifications(String specifications) {
            this.specifications = specifications;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder stockQuantity(Integer stockQuantity) {
            this.stockQuantity = stockQuantity;
            return this;
        }

        public Builder available(boolean available) {
            this.available = available;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }
}
