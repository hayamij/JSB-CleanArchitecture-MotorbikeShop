package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.checkinventory.CheckInventoryAvailabilityInputData;
import com.motorbike.business.dto.checkinventory.CheckInventoryAvailabilityOutputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.input.CheckInventoryAvailabilityInputBoundary;
import com.motorbike.business.usecase.output.CheckInventoryAvailabilityOutputBoundary;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.exceptions.DomainException;
import com.motorbike.domain.exceptions.ValidationException;

public class CheckInventoryAvailabilityUseCaseControl implements CheckInventoryAvailabilityInputBoundary {
    
    private final CheckInventoryAvailabilityOutputBoundary outputBoundary;
    private final ProductRepository productRepository;
    
    public CheckInventoryAvailabilityUseCaseControl(
            CheckInventoryAvailabilityOutputBoundary outputBoundary,
            ProductRepository productRepository) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
    }
    
    @Override
    public void execute(CheckInventoryAvailabilityInputData inputData) {
        CheckInventoryAvailabilityOutputData outputData = checkInventoryInternal(inputData);
        outputBoundary.present(outputData);
    }
    
    /**
     * Internal method for use case composition.
     * Returns OutputData directly without using presenter.
     */
    public CheckInventoryAvailabilityOutputData checkInventoryInternal(CheckInventoryAvailabilityInputData inputData) {
        CheckInventoryAvailabilityOutputData outputData = null;
        Exception errorException = null;
        
        // Step 1: Validation
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
            SanPham.checkInput(inputData.getProductId(), inputData.getRequestedQuantity());
        } catch (Exception e) {
            errorException = e;
        }
        
        // Step 2: Business logic - Check inventory
        SanPham sanPham = null;
        if (errorException == null) {
            try {
                sanPham = productRepository.findById(inputData.getProductId())
                    .orElseThrow(() -> DomainException.productNotFound(String.valueOf(inputData.getProductId())));
                
                int availableStock = sanPham.getSoLuongTonKho();
                boolean isAvailable = availableStock >= inputData.getRequestedQuantity();
                
                outputData = new CheckInventoryAvailabilityOutputData(
                    sanPham.getMaSanPham(),
                    sanPham.getTenSanPham(),
                    isAvailable,
                    availableStock
                );
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
            } else if (errorException instanceof DomainException) {
                errorCode = ((DomainException) errorException).getErrorCode();
            }
            
            outputData = CheckInventoryAvailabilityOutputData.forError(errorCode, message);
        }
        
        return outputData;
    }
}
