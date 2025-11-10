package com.motorbike.business.exception;

/**
 * CartItemNotFoundException - Business Layer
 * Exception thrown when a cart item is not found
 * Part of Clean Architecture - Business Layer
 */
public class CartItemNotFoundException extends RuntimeException {
    
    public CartItemNotFoundException(String message) {
        super(message);
    }
    
    public CartItemNotFoundException(Long userId, Long productId) {
        super(String.format("Product with ID %d not found in cart of user %d", productId, userId));
    }
}
