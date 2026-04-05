package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.calculateordertotals.CalculateOrderTotalsInputData;
import com.motorbike.business.dto.calculateordertotals.CalculateOrderTotalsOutputData;
import com.motorbike.business.usecase.input.CalculateOrderTotalsInputBoundary;
import com.motorbike.business.usecase.output.CalculateOrderTotalsOutputBoundary;
import com.motorbike.domain.entities.ChiTietDonHang;
import com.motorbike.domain.exceptions.ValidationException;
import java.math.BigDecimal;

public class CalculateOrderTotalsUseCaseControl implements CalculateOrderTotalsInputBoundary {
    
    private final CalculateOrderTotalsOutputBoundary outputBoundary;
    
    public CalculateOrderTotalsUseCaseControl(CalculateOrderTotalsOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }
    
    @Override
    public void execute(CalculateOrderTotalsInputData inputData) {
        CalculateOrderTotalsOutputData outputData = calculateInternal(inputData);
        outputBoundary.present(outputData);
    }
    
    // Overload to accept DTO from order package
    public CalculateOrderTotalsOutputData calculateInternal(com.motorbike.business.dto.order.CalculateOrderTotalsInputData inputData) {
        // Convert to the expected DTO type
        CalculateOrderTotalsInputData convertedInput = new CalculateOrderTotalsInputData(inputData.getOrderItems());
        return calculateInternal(convertedInput);
    }
    
    public CalculateOrderTotalsOutputData calculateInternal(CalculateOrderTotalsInputData inputData) {
        CalculateOrderTotalsOutputData outputData = null;
        Exception errorException = null;
        
        try {
            if (inputData == null || inputData.getOrderItems() == null) {
                throw ValidationException.invalidInput();
            }
        } catch (Exception e) {
            errorException = e;
        }
        
        if (errorException == null) {
            try {
                int totalItems = inputData.getOrderItems().size();
                int totalQuantity = 0;
                BigDecimal totalAmount = BigDecimal.ZERO;
                
                for (ChiTietDonHang item : inputData.getOrderItems()) {
                    totalQuantity += item.getSoLuong();
                    totalAmount = totalAmount.add(item.getThanhTien());
                }
                
                outputData = new CalculateOrderTotalsOutputData(totalItems, totalQuantity, totalAmount);
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        if (errorException != null) {
            String errorCode = "SYSTEM_ERROR";
            String message = errorException.getMessage();
            
            if (errorException instanceof ValidationException) {
                errorCode = ((ValidationException) errorException).getErrorCode();
            }
            
            outputData = CalculateOrderTotalsOutputData.forError(errorCode, message);
        }
        
        return outputData;
    }
}
