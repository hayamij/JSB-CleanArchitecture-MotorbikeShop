package com.motorbike.business.usecase;

import com.motorbike.business.entity.Cart;

/**
 * Add to Cart Use Case Interface - Business Layer
 * Defines the contract for adding products to cart
 * Part of Clean Architecture - Business Layer (independent of frameworks)
 */
public interface AddToCartUseCase {
    
    /**
     * Execute add to cart use case
     * @param request request containing user ID, product ID, and quantity
     * @return response containing updated cart
     */
    AddToCartResponse execute(AddToCartRequest request);
    
    /**
     * Request DTO for Add to Cart Use Case
     */
    class AddToCartRequest {
        private final Long userId;
        private final Long productId;
        private final Integer quantity;
        
        public AddToCartRequest(Long userId, Long productId, Integer quantity) {
            // Validation
            if (userId == null) {
                throw new IllegalArgumentException("User ID is required");
            }
            if (productId == null) {
                throw new IllegalArgumentException("Product ID is required");
            }
            if (quantity == null || quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than 0");
            }
            
            this.userId = userId;
            this.productId = productId;
            this.quantity = quantity;
        }
        
        public Long getUserId() {
            return userId;
        }
        
        public Long getProductId() {
            return productId;
        }
        
        public Integer getQuantity() {
            return quantity;
        }
    }
    
    /**
     * Response DTO for Add to Cart Use Case
     */
    class AddToCartResponse {
        private final Cart cart;
        private final String message;
        private final boolean success;
        
        public AddToCartResponse(Cart cart, String message, boolean success) {
            this.cart = cart;
            this.message = message;
            this.success = success;
        }
        
        public Cart getCart() {
            return cart;
        }
        
        public String getMessage() {
            return message;
        }
        
        public boolean isSuccess() {
            return success;
        }
    }
}
