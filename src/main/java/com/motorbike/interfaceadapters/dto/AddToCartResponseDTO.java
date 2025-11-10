package com.motorbike.interfaceadapters.dto;

/**
 * Add to Cart Response DTO - Interface Adapters Layer
 * Response DTO for adding items to cart
 * Part of Clean Architecture - Interface Adapters Layer
 */
public class AddToCartResponseDTO {
    private CartDTO cart;
    private String message;
    private boolean success;
    
    // Constructors
    public AddToCartResponseDTO() {}
    
    public AddToCartResponseDTO(CartDTO cart, String message, boolean success) {
        this.cart = cart;
        this.message = message;
        this.success = success;
    }
    
    // Getters and Setters
    public CartDTO getCart() {
        return cart;
    }
    
    public void setCart(CartDTO cart) {
        this.cart = cart;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
}
