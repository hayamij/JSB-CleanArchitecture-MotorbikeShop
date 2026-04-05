package com.motorbike.business.dto.order;

import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.entities.ChiTietDonHang;
import java.math.BigDecimal;
import java.util.List;

public class CalculateOrderTotalsInputData {
    private final DonHang donHang;
    private final List<ChiTietDonHang> orderItems;
    
    public CalculateOrderTotalsInputData(DonHang donHang) {
        this.donHang = donHang;
        this.orderItems = null;
    }
    
    // Constructor with List<ChiTietDonHang>
    public CalculateOrderTotalsInputData(List<ChiTietDonHang> orderItems) {
        this.donHang = null;
        this.orderItems = orderItems;
    }
    
    public DonHang getDonHang() { return donHang; }
    public List<ChiTietDonHang> getOrderItems() { return orderItems; }
}
