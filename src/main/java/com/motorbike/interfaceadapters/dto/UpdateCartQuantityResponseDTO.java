package com.motorbike.interfaceadapters.dto;

/**
 * UpdateCartQuantityResponseDTO - Interface Adapters Layer
 * DTO for update cart quantity response
 * Part of Clean Architecture - Interface Adapters Layer
 */
public class UpdateCartQuantityResponseDTO {
    
    private CartDTO cart;
    private String message;
    private boolean itemRemoved;
    private boolean success;
    
    public UpdateCartQuantityResponseDTO() {
    }
    
    public UpdateCartQuantityResponseDTO(CartDTO cart, String message, boolean itemRemoved, boolean success) {
        this.cart = cart;
        this.message = message;
        this.itemRemoved = itemRemoved;
        this.success = success;
    }
    
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
    
    public boolean isItemRemoved() {
        return itemRemoved;
    }
    
    public void setItemRemoved(boolean itemRemoved) {
        this.itemRemoved = itemRemoved;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
}
