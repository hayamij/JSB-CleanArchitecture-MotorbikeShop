package com.motorbike.business.dto.formatordersforlist;

import com.motorbike.domain.entities.DonHang;
import java.util.List;

/**
 * UC-81: Format Orders For List - Input Data
 * Holds the list of orders to be formatted for list display
 */
public class FormatOrdersForListInputData {
    
    private final List<DonHang> orders;
    
    public FormatOrdersForListInputData(List<DonHang> orders) {
        this.orders = orders;
    }
    
    public List<DonHang> getOrders() {
        return orders;
    }
}
