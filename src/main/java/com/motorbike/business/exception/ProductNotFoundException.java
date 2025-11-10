package com.motorbike.business.exception;

/**
 * Exception thrown when a product is not found
 * Part of Clean Architecture - Business Layer
 */
public class ProductNotFoundException extends RuntimeException {
    
    public ProductNotFoundException(String message) {
        super(message);
    }
    
    public ProductNotFoundException(Long productId) {
        super("Product not found with ID: " + productId);
    }
}
