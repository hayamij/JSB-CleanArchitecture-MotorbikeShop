package com.motorbike.business.dto.createorderfromcart;

import com.motorbike.domain.entities.DonHang;

public class CreateOrderFromCartOutputData {
    private final boolean success;
    private final String message;
    private final DonHang order;
    
    public CreateOrderFromCartOutputData(boolean success, String message, DonHang order) {
        this.success = success;
        this.message = message;
        this.order = order;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public DonHang getOrder() {
        return order;
    }
}
