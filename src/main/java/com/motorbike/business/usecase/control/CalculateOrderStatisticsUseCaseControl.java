package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.calculateorderstats.CalculateOrderStatisticsInputData;
import com.motorbike.business.dto.calculateorderstats.CalculateOrderStatisticsOutputData;
import com.motorbike.business.usecase.input.CalculateOrderStatisticsInputBoundary;
import com.motorbike.business.usecase.output.CalculateOrderStatisticsOutputBoundary;
import com.motorbike.domain.entities.DonHang;

import java.math.BigDecimal;

/**
 * UC-80: Calculate Order Statistics Use Case
 * Responsibility: Calculate statistics for a list of orders
 * - Total revenue (sum of all order totals)
 * - Order count
 * - Total items count across all orders
 */
public class CalculateOrderStatisticsUseCaseControl implements CalculateOrderStatisticsInputBoundary {
    
    private final CalculateOrderStatisticsOutputBoundary outputBoundary;
    
    public CalculateOrderStatisticsUseCaseControl(CalculateOrderStatisticsOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }
    
    @Override
    public CalculateOrderStatisticsOutputData execute(CalculateOrderStatisticsInputData inputData) {
        if (outputBoundary != null) {
            CalculateOrderStatisticsOutputData outputData = calculateInternal(inputData);
            outputBoundary.present(outputData);
            return outputData;
        } else {
            return calculateInternal(inputData);
        }
    }
    
    /**
     * Internal method for direct delegation from other usecases
     * Returns output data without going through presenter
     */
    public CalculateOrderStatisticsOutputData calculateInternal(CalculateOrderStatisticsInputData inputData) {
        try {
            if (inputData == null || inputData.getOrders() == null) {
                return CalculateOrderStatisticsOutputData.forError("INVALID_INPUT", "Orders list cannot be null");
            }
            
            // Calculate total revenue
            BigDecimal totalRevenue = inputData.getOrders().stream()
                    .map(DonHang::getTongTien)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            // Order count
            int orderCount = inputData.getOrders().size();
            
            // Total items count
            int totalItems = inputData.getOrders().stream()
                    .mapToInt(order -> order.getDanhSachSanPham().size())
                    .sum();
            
            return CalculateOrderStatisticsOutputData.forSuccess(totalRevenue, orderCount, totalItems);
            
        } catch (Exception e) {
            return CalculateOrderStatisticsOutputData.forError("CALCULATION_ERROR", e.getMessage());
        }
    }
}
