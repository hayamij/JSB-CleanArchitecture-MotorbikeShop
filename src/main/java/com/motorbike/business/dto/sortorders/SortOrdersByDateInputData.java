package com.motorbike.business.dto.sortorders;

import com.motorbike.domain.entities.DonHang;
import java.util.List;

/**
 * UC-79: Sort Orders By Date - Input Data
 * Holds the list of orders to be sorted
 */
public class SortOrdersByDateInputData {
    
    private final List<DonHang> orders;
    private final boolean descending; // true = newest first, false = oldest first
    
    public SortOrdersByDateInputData(List<DonHang> orders, boolean descending) {
        this.orders = orders;
        this.descending = descending;
    }
    
    public List<DonHang> getOrders() {
        return orders;
    }
    
    public boolean isDescending() {
        return descending;
    }
}
