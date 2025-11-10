package com.motorbike.business.exception;

/**
 * Exception thrown when product is out of stock
 * Part of Clean Architecture - Business Layer
 */
public class ProductOutOfStockException extends RuntimeException {
    
    public ProductOutOfStockException(String message) {
        super(message);
    }
    
    public ProductOutOfStockException(String productName, int requestedQuantity, int availableQuantity) {
        super(String.format("Product '%s' is out of stock. Requested: %d, Available: %d", 
            productName, requestedQuantity, availableQuantity));
    }
}
