package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.validatecartitem.ValidateCartItemInputData;
import com.motorbike.business.dto.validatecartitem.ValidateCartItemOutputData;
import com.motorbike.business.usecase.input.ValidateCartItemInputBoundary;
import com.motorbike.business.usecase.output.ValidateCartItemOutputBoundary;
import com.motorbike.domain.exceptions.ValidationException;

public class ValidateCartItemUseCaseControl implements ValidateCartItemInputBoundary {
    
    private final ValidateCartItemOutputBoundary outputBoundary;
    
    public ValidateCartItemUseCaseControl(ValidateCartItemOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }
    
    // Constructor for tests with swapped parameters (repository first)
    public ValidateCartItemUseCaseControl(
            com.motorbike.business.ports.SanPhamRepository sanPhamRepository,
            ValidateCartItemOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }
    
    @Override
    public void execute(ValidateCartItemInputData inputData) {
        ValidateCartItemOutputData outputData = validateInternal(inputData);
        outputBoundary.present(outputData);
    }
    
    public ValidateCartItemOutputData validateInternal(ValidateCartItemInputData inputData) {
        ValidateCartItemOutputData outputData = null;
        Exception errorException = null;
        
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
            if (inputData.getProductId() == null) {
                throw ValidationException.nullProductId();
            }
            if (inputData.getQuantity() <= 0) {
                throw ValidationException.invalidQuantity();
            }
            
            outputData = new ValidateCartItemOutputData(true, "Cart item is valid", inputData.getProductId());
            
        } catch (Exception e) {
            errorException = e;
            outputData = new ValidateCartItemOutputData(false, e.getMessage(), inputData != null ? inputData.getProductId() : null);
        }
        
        return outputData;
    }
}
