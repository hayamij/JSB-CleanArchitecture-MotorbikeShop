package com.motorbike.business.dto.findcartitem;

import com.motorbike.domain.entities.GioHang;

public class FindCartItemOutputData {
    private final GioHang cartItem;
    private final boolean found;
    private final String message;
    
    public FindCartItemOutputData(GioHang cartItem, boolean found, String message) {
        this.cartItem = cartItem;
        this.found = found;
        this.message = message;
    }
    
    // Constructor with just found and message
    public FindCartItemOutputData(boolean found, String message) {
        this.cartItem = null;
        this.found = found;
        this.message = message;
    }
    
    // Constructor with 4 params for tests (boolean, null, String, ChiTietGioHang)
    public FindCartItemOutputData(boolean found, Object nullValue, String message, com.motorbike.domain.entities.ChiTietGioHang item) {
        this.cartItem = null; // Ignore nullValue and item for now
        this.found = found;
        this.message = message;
    }
    
    // Static factory methods for clarity
    public static FindCartItemOutputData notFound(String message) {
        return new FindCartItemOutputData(false, message);
    }
    
    public static FindCartItemOutputData found(GioHang cart) {
        return new FindCartItemOutputData(cart, true, "Found");
    }
    
    public GioHang getCartItem() {
        return cartItem;
    }
    
    public boolean isFound() {
        return found;
    }
    
    public String getMessage() {
        return message;
    }
}
