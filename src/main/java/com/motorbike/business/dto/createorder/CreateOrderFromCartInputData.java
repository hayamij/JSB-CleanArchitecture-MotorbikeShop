package com.motorbike.business.dto.createorder;

import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.entities.PhuongThucThanhToan;

public class CreateOrderFromCartInputData {
    private final GioHang cart;
    private final String receiverName;
    private final String phoneNumber;
    private final String shippingAddress;
    private final String note;
    private final PhuongThucThanhToan paymentMethod;
    
    public CreateOrderFromCartInputData(
            GioHang cart,
            String receiverName,
            String phoneNumber,
            String shippingAddress,
            String note,
            PhuongThucThanhToan paymentMethod) {
        this.cart = cart;
        this.receiverName = receiverName;
        this.phoneNumber = phoneNumber;
        this.shippingAddress = shippingAddress;
        this.note = note;
        this.paymentMethod = paymentMethod;
    }
    
    public GioHang getCart() {
        return cart;
    }
    
    public String getReceiverName() {
        return receiverName;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public String getShippingAddress() {
        return shippingAddress;
    }
    
    public String getNote() {
        return note;
    }
    
    public PhuongThucThanhToan getPaymentMethod() {
        return paymentMethod;
    }
}
