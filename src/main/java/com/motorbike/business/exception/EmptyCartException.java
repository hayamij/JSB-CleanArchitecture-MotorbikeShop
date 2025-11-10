package com.motorbike.business.exception;

/**
 * EmptyCartException - Business Layer
 * Exception thrown when trying to checkout with an empty cart
 * Part of Clean Architecture - Business Layer
 */
public class EmptyCartException extends RuntimeException {
    
    public EmptyCartException(String message) {
        super(message);
    }
    
    public EmptyCartException(Long userId) {
        super(String.format("Cart is empty for user %d. Cannot proceed with checkout.", userId));
    }
}
