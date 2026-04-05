package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.clearcart.ClearCartInputData;
import com.motorbike.business.dto.clearcart.ClearCartOutputData;
import com.motorbike.business.ports.repository.CartRepository;
import com.motorbike.business.usecase.input.ClearCartInputBoundary;
import com.motorbike.business.usecase.output.ClearCartOutputBoundary;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.exceptions.DomainException;
import com.motorbike.domain.exceptions.ValidationException;

public class ClearCartUseCaseControl implements ClearCartInputBoundary {
    
    private final ClearCartOutputBoundary outputBoundary;
    private final CartRepository cartRepository;
    
    public ClearCartUseCaseControl(
            ClearCartOutputBoundary outputBoundary,
            CartRepository cartRepository) {
        this.outputBoundary = outputBoundary;
        this.cartRepository = cartRepository;
    }
    
    // Constructor for tests with swapped parameters
    public ClearCartUseCaseControl(
            com.motorbike.business.ports.GioHangRepository gioHangRepository,
            ClearCartOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
        this.cartRepository = (CartRepository) gioHangRepository;
    }
    
    @Override
    public void execute(ClearCartInputData inputData) {
        ClearCartOutputData outputData = clearInternal(inputData);
        outputBoundary.present(outputData);
    }
    
    /**
     * Internal method for use case composition.
     * Returns OutputData directly without using presenter.
     */
    public ClearCartOutputData clearInternal(ClearCartInputData inputData) {
        ClearCartOutputData outputData = null;
        Exception errorException = null;
        
        // Step 1: Validation
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
            if (inputData.getCartId() == null) {
                throw ValidationException.invalidCartId();
            }
        } catch (Exception e) {
            errorException = e;
        }
        
        // Step 2: Business logic - Clear cart
        GioHang gioHang = null;
        if (errorException == null) {
            try {
                gioHang = cartRepository.findById(inputData.getCartId())
                    .orElseThrow(DomainException::cartNotFound);
                
                gioHang.xoaToanBoGioHang();
                cartRepository.save(gioHang);
                
                outputData = new ClearCartOutputData(gioHang.getMaGioHang());
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
            
            outputData = ClearCartOutputData.forError(errorCode, message);
        }
        
        return outputData;
    }
}
