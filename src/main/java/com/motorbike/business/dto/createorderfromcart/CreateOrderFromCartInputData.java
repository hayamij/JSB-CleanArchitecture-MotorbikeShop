package com.motorbike.business.dto.createorderfromcart;

import com.motorbike.domain.entities.GioHang;
import java.util.List;

public class CreateOrderFromCartInputData {
    private final Long userId;
    private final String shippingAddress;
    private final String paymentMethod;
    private final List<GioHang> cartItems;
    
    public CreateOrderFromCartInputData(Long userId, String shippingAddress, String paymentMethod) {
        this.userId = userId;
        this.shippingAddress = shippingAddress;
        this.paymentMethod = paymentMethod;
        this.cartItems = null;
    }

    public CreateOrderFromCartInputData(Long userId, List<GioHang> cartItems) {
        this.userId = userId;
        this.shippingAddress = null;
        this.paymentMethod = null;
        this.cartItems = cartItems;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public String getShippingAddress() {
        return shippingAddress;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }

    public List<GioHang> getCartItems() {
        return cartItems;
    }
}
