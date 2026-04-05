package com.motorbike.business.dto.listmyorders;

import com.motorbike.domain.entities.DonHang;
import java.util.List;

public class ListMyOrdersOutputData {
    private final List<DonHang> orders;
    private final boolean success;
    private final String message;
    
    public ListMyOrdersOutputData(List<DonHang> orders, boolean success, String message) {
        this.orders = orders;
        this.success = success;
        this.message = message;
    }
    
    public List<DonHang> getOrders() {
        return orders;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public String getMessage() {
        return message;
    }
}
