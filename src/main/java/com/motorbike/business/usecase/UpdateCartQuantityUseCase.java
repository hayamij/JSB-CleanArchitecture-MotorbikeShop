package com.motorbike.business.usecase;

import com.motorbike.business.entity.Cart;

/**
 * UpdateCartQuantityUseCase - Business Layer
 * Use case interface for updating cart item quantity
 * Part of Clean Architecture - Business Layer
 */
public interface UpdateCartQuantityUseCase {
    
    /**
     * Execute the update cart quantity use case
     * @param request the request containing update information
     * @return the response containing updated cart
     */
    UpdateCartQuantityResponse execute(UpdateCartQuantityRequest request);
    
    /**
     * Request DTO for updating cart quantity
     */
    class UpdateCartQuantityRequest {
        private final Long userId;
        private final Long productId;
        private final Integer newQuantity;
        
        public UpdateCartQuantityRequest(Long userId, Long productId, Integer newQuantity) {
            this.userId = userId;
            this.productId = productId;
            this.newQuantity = newQuantity;
        }
        
        public Long getUserId() {
            return userId;
        }
        
        public Long getProductId() {
            return productId;
        }
        
        public Integer getNewQuantity() {
            return newQuantity;
        }
    }
    
    /**
     * Response DTO for updating cart quantity
     */
    class UpdateCartQuantityResponse {
        private final Cart cart;
        private final String message;
        private final boolean itemRemoved;
        private final boolean success;
        
        public UpdateCartQuantityResponse(Cart cart, String message, boolean itemRemoved, boolean success) {
            this.cart = cart;
            this.message = message;
            this.itemRemoved = itemRemoved;
            this.success = success;
        }
        
        public Cart getCart() {
            return cart;
        }
        
        public String getMessage() {
            return message;
        }
        
        public boolean isItemRemoved() {
            return itemRemoved;
        }
        
        public boolean isSuccess() {
            return success;
        }
    }
}
