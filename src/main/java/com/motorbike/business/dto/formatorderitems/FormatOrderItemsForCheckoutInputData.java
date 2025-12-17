package com.motorbike.business.dto.formatorderitems;

import com.motorbike.domain.entities.ChiTietDonHang;
import java.util.List;

/**
 * UC-82: Format Order Items For Checkout - Input Data
 * Holds the list of order items (ChiTietDonHang) to be formatted
 */
public class FormatOrderItemsForCheckoutInputData {
    
    private final List<ChiTietDonHang> orderItems;
    
    public FormatOrderItemsForCheckoutInputData(List<ChiTietDonHang> orderItems) {
        this.orderItems = orderItems;
    }
    
    public List<ChiTietDonHang> getOrderItems() {
        return orderItems;
    }
}
