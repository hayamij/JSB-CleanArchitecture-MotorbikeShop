package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.sortorders.SortOrdersByDateInputData;
import com.motorbike.business.dto.sortorders.SortOrdersByDateOutputData;
import com.motorbike.business.usecase.input.SortOrdersByDateInputBoundary;
import com.motorbike.business.usecase.output.SortOrdersByDateOutputBoundary;
import com.motorbike.domain.entities.DonHang;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * UC-79: Sort Orders By Date Use Case
 * Responsibility: Sort a list of orders by their order date
 * - Supports both ascending and descending order
 * - Uses DonHang.getNgayDat() for comparison
 */
public class SortOrdersByDateUseCaseControl implements SortOrdersByDateInputBoundary {
    
    private final SortOrdersByDateOutputBoundary outputBoundary;
    
    public SortOrdersByDateUseCaseControl(SortOrdersByDateOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }
    
    @Override
    public SortOrdersByDateOutputData execute(SortOrdersByDateInputData inputData) {
        if (outputBoundary != null) {
            SortOrdersByDateOutputData outputData = sortInternal(inputData);
            outputBoundary.present(outputData);
            return outputData;
        } else {
            return sortInternal(inputData);
        }
    }
    
    /**
     * Internal method for direct delegation from other usecases
     * Returns output data without going through presenter
     */
    public SortOrdersByDateOutputData sortInternal(SortOrdersByDateInputData inputData) {
        try {
            if (inputData == null || inputData.getOrders() == null) {
                return SortOrdersByDateOutputData.forError("INVALID_INPUT", "Orders list cannot be null");
            }
            
            Comparator<DonHang> comparator = Comparator.comparing(DonHang::getNgayDat);
            
            // Reverse if descending (newest first)
            if (inputData.isDescending()) {
                comparator = comparator.reversed();
            }
            
            List<DonHang> sortedOrders = inputData.getOrders().stream()
                    .sorted(comparator)
                    .collect(Collectors.toList());
            
            return SortOrdersByDateOutputData.forSuccess(sortedOrders);
            
        } catch (Exception e) {
            return SortOrdersByDateOutputData.forError("SORT_ERROR", e.getMessage());
        }
    }
}
