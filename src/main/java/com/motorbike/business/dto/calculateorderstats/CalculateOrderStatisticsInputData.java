package com.motorbike.business.dto.calculateorderstats;

import com.motorbike.domain.entities.DonHang;
import java.util.List;

/**
 * UC-80: Calculate Order Statistics - Input Data
 * Holds the list of orders to calculate statistics from
 */
public class CalculateOrderStatisticsInputData {
    
    private final List<DonHang> orders;
    
    public CalculateOrderStatisticsInputData(List<DonHang> orders) {
        this.orders = orders;
    }
    
    public List<DonHang> getOrders() {
        return orders;
    }
}
