package com.motorbike.business.dto.calculateordertotals;

import com.motorbike.domain.entities.ChiTietDonHang;
import java.util.List;

public class CalculateOrderTotalsInputData {
    private final List<ChiTietDonHang> orderItems;
    
    public CalculateOrderTotalsInputData(List<ChiTietDonHang> orderItems) {
        this.orderItems = orderItems;
    }
    
    public List<ChiTietDonHang> getOrderItems() {
        return orderItems;
    }
}
