package com.motorbike.interfaceadapters.dto;

/**
 * View Cart Response DTO - Interface Adapters Layer
 * Response DTO for viewing cart
 * Part of Clean Architecture - Interface Adapters Layer
 */
public class ViewCartResponseDTO {
    private CartDTO cart;
    private boolean isEmpty;
    private String message;
    
    // Constructors
    public ViewCartResponseDTO() {}
    
    public ViewCartResponseDTO(CartDTO cart, boolean isEmpty, String message) {
        this.cart = cart;
        this.isEmpty = isEmpty;
        this.message = message;
    }
    
    // Getters and Setters
    public CartDTO getCart() {
        return cart;
    }
    
    public void setCart(CartDTO cart) {
        this.cart = cart;
    }
    
    public boolean isEmpty() {
        return isEmpty;
    }
    
    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}
