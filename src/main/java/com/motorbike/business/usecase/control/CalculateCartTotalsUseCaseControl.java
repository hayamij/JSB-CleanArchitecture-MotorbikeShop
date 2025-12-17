package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.calculatecarttotals.CalculateCartTotalsInputData;
import com.motorbike.business.dto.calculatecarttotals.CalculateCartTotalsOutputData;
import com.motorbike.business.usecase.input.CalculateCartTotalsInputBoundary;
import com.motorbike.business.usecase.output.CalculateCartTotalsOutputBoundary;
import com.motorbike.domain.entities.ChiTietGioHang;
import com.motorbike.domain.exceptions.ValidationException;
import java.math.BigDecimal;

public class CalculateCartTotalsUseCaseControl implements CalculateCartTotalsInputBoundary {
    
    private final CalculateCartTotalsOutputBoundary outputBoundary;
    
    public CalculateCartTotalsUseCaseControl(CalculateCartTotalsOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }
    
    @Override
    public void execute(CalculateCartTotalsInputData inputData) {
        CalculateCartTotalsOutputData outputData = calculateInternal(inputData);
        outputBoundary.present(outputData);
    }
    
    /**
     * Internal method for use case composition.
     * Returns OutputData directly without using presenter.
     */
    public CalculateCartTotalsOutputData calculateInternal(CalculateCartTotalsInputData inputData) {
        CalculateCartTotalsOutputData outputData = null;
        Exception errorException = null;
        
        // Step 1: Validation
        try {
            if (inputData == null || inputData.getCartItems() == null) {
                throw ValidationException.invalidInput();
            }
        } catch (Exception e) {
            errorException = e;
        }
        
        // Step 2: Business logic - Calculate totals
        if (errorException == null) {
            try {
                int totalItems = inputData.getCartItems().size();
                int totalQuantity = 0;
                BigDecimal totalAmount = BigDecimal.ZERO;
                
                for (ChiTietGioHang item : inputData.getCartItems()) {
                    totalQuantity += item.getSoLuong();
                    totalAmount = totalAmount.add(item.getTamTinh());
                }
                
                outputData = new CalculateCartTotalsOutputData(totalItems, totalQuantity, totalAmount);
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        // Step 3: Handle error
        if (errorException != null) {
            String errorCode = "SYSTEM_ERROR";
            String message = errorException.getMessage();
            
            if (errorException instanceof ValidationException) {
                errorCode = ((ValidationException) errorException).getErrorCode();
            }
            
            outputData = CalculateCartTotalsOutputData.forError(errorCode, message);
        }
        
        return outputData;
    }
}
