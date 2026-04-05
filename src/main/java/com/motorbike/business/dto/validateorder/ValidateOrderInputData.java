package com.motorbike.business.dto.validateorder;

import com.motorbike.domain.entities.GioHang;
import java.util.List;

public class ValidateOrderInputData {
    private final Long orderId;
    private final List<GioHang> cartItems;
    
    public ValidateOrderInputData(Long orderId) {
        this.orderId = orderId;
        this.cartItems = null;
    }
    
    // Constructor with List<GioHang>
    public ValidateOrderInputData(List<GioHang> cartItems) {
        this.orderId = null;
        this.cartItems = cartItems;
    }
    
    public Long getOrderId() {
        return orderId;
    }
    
    public List<GioHang> getCartItems() {
        return cartItems;
    }
}
