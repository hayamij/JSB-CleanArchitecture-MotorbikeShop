package com.motorbike.business.dto.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class GetAllProductsOutputData {
    private final boolean success;
    private final List<ProductInfo> products;
    private final String errorCode;
    private final String errorMessage;

    private GetAllProductsOutputData(boolean success, List<ProductInfo> products, 
                                     String errorCode, String errorMessage) {
        this.success = success;
        this.products = products;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static GetAllProductsOutputData forSuccess(List<ProductInfo> products) {
        return new GetAllProductsOutputData(true, products, null, null);
    }

    public static GetAllProductsOutputData forError(String errorCode, String errorMessage) {
        return new GetAllProductsOutputData(false, null, errorCode, errorMessage);
    }

    public boolean isSuccess() {
        return success;
    }

    public List<ProductInfo> getProducts() {
        return products;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public static class ProductInfo {
        private final Long productId;
        private final String name;
        private final String description;
        private final BigDecimal price;
        private final int stockQuantity;
        private final String imageUrl;
        private final LocalDateTime createdDate;
        private final boolean available;
        private final String category; // "XE_MAY" or "PHU_KIEN" or "KHAC"
        
        // Motorbike specific fields (null for accessories)
        private final String brand;
        private final String model;
        private final String color;
        private final Integer year;
        private final Integer engineCapacity;
        
        // Accessory specific fields (null for motorbikes)
        private final String type;
        private final String material;
        private final String size;

        // Constructor for Motorbike
        public ProductInfo(Long productId, String name, String description, BigDecimal price,
                          int stockQuantity, String imageUrl, LocalDateTime createdDate, boolean available,
                          String brand, String model, String color, Integer year, Integer engineCapacity) {
            this.productId = productId;
            this.name = name;
            this.description = description;
            this.price = price;
            this.stockQuantity = stockQuantity;
            this.imageUrl = imageUrl;
            this.createdDate = createdDate;
            this.available = available;
            this.category = "XE_MAY";
            this.brand = brand;
            this.model = model;
            this.color = color;
            this.year = year;
            this.engineCapacity = engineCapacity;
            this.type = null;
            this.material = null;
            this.size = null;
        }

        // Constructor for Accessory
        public ProductInfo(Long productId, String name, String description, BigDecimal price,
                          int stockQuantity, String imageUrl, LocalDateTime createdDate, boolean available,
                          String type, String brand, String material, String size) {
            this.productId = productId;
            this.name = name;
            this.description = description;
            this.price = price;
            this.stockQuantity = stockQuantity;
            this.imageUrl = imageUrl;
            this.createdDate = createdDate;
            this.available = available;
            this.category = "PHU_KIEN";
            this.brand = brand;
            this.type = type;
            this.material = material;
            this.size = size;
            this.model = null;
            this.color = null;
            this.year = null;
            this.engineCapacity = null;
        }

        // Getters
        public Long getProductId() { return productId; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public BigDecimal getPrice() { return price; }
        public int getStockQuantity() { return stockQuantity; }
        public String getImageUrl() { return imageUrl; }
        public LocalDateTime getCreatedDate() { return createdDate; }
        public boolean isAvailable() { return available; }
        public String getCategory() { return category; }
        public String getBrand() { return brand; }
        public String getModel() { return model; }
        public String getColor() { return color; }
        public Integer getYear() { return year; }
        public Integer getEngineCapacity() { return engineCapacity; }
        public String getType() { return type; }
        public String getMaterial() { return material; }
        public String getSize() { return size; }
    }
}
