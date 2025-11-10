package com.motorbike.business.usecase;

import com.motorbike.business.entity.Cart;

/**
 * View Cart Use Case Interface - Business Layer
 * Defines the contract for viewing cart contents
 * Part of Clean Architecture - Business Layer (independent of frameworks)
 */
public interface ViewCartUseCase {
    
    /**
     * Execute view cart use case
     * @param request request containing user ID
     * @return response containing cart with items
     */
    ViewCartResponse execute(ViewCartRequest request);
    
    /**
     * Request DTO for View Cart Use Case
     */
    class ViewCartRequest {
        private final Long userId;
        
        public ViewCartRequest(Long userId) {
            if (userId == null) {
                throw new IllegalArgumentException("User ID is required");
            }
            this.userId = userId;
        }
        
        public Long getUserId() {
            return userId;
        }
    }
    
    /**
     * Response DTO for View Cart Use Case
     */
    class ViewCartResponse {
        private final Cart cart;
        private final boolean isEmpty;
        private final String message;
        
        public ViewCartResponse(Cart cart, boolean isEmpty, String message) {
            this.cart = cart;
            this.isEmpty = isEmpty;
            this.message = message;
        }
        
        public Cart getCart() {
            return cart;
        }
        
        public boolean isEmpty() {
            return isEmpty;
        }
        
        public String getMessage() {
            return message;
        }
    }
}
