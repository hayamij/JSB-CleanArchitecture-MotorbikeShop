package com.motorbike.application.usecase;

import com.motorbike.domain.entity.Product;

/**
 * Input Port (Interface) for Get Product Detail Use Case
 * This defines the contract for the use case from the perspective of the caller
 */
public interface GetProductDetailUseCase {
    
    /**
     * Execute the use case to get product details
     * @param request Input request containing product ID
     * @return Output response containing product details
     * @throws ProductNotFoundException if product not found
     */
    ProductDetailResponse execute(ProductDetailRequest request);
    
    /**
     * Input Data Transfer Object
     */
    class ProductDetailRequest {
        private final Long productId;
        
        public ProductDetailRequest(Long productId) {
            if (productId == null || productId <= 0) {
                throw new IllegalArgumentException("Product ID must be positive");
            }
            this.productId = productId;
        }
        
        public Long getProductId() {
            return productId;
        }
    }
    
    /**
     * Output Data Transfer Object
     */
    class ProductDetailResponse {
        private final Long id;
        private final String name;
        private final String description;
        private final String price;
        private final String imageUrl;
        private final String specifications;
        private final String category;
        private final Integer stockQuantity;
        private final boolean available;
        private final boolean inStock;
        
        public ProductDetailResponse(Product product) {
            this.id = product.getId();
            this.name = product.getName();
            this.description = product.getDescription();
            this.price = product.getPrice() != null ? product.getPrice().toString() : "0";
            this.imageUrl = product.getImageUrl();
            this.specifications = product.getSpecifications();
            this.category = product.getCategory();
            this.stockQuantity = product.getStockQuantity();
            this.available = product.isAvailable();
            this.inStock = product.isInStock();
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
        
        public String getPrice() {
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
        
        public boolean isInStock() {
            return inStock;
        }
    }
}
