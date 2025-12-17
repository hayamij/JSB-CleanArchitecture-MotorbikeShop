package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.reducestock.ReduceProductStockInputData;
import com.motorbike.business.dto.reducestock.ReduceProductStockOutputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.input.ReduceProductStockInputBoundary;
import com.motorbike.business.usecase.output.ReduceProductStockOutputBoundary;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.exceptions.DomainException;
import com.motorbike.domain.exceptions.ValidationException;

public class ReduceProductStockUseCaseControl implements ReduceProductStockInputBoundary {
    
    private final ReduceProductStockOutputBoundary outputBoundary;
    private final ProductRepository productRepository;
    
    public ReduceProductStockUseCaseControl(
            ReduceProductStockOutputBoundary outputBoundary,
            ProductRepository productRepository) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
    }
    
    @Override
    public void execute(ReduceProductStockInputData inputData) {
        ReduceProductStockOutputData outputData = reduceStockInternal(inputData);
        outputBoundary.present(outputData);
    }
    
    /**
     * Internal method for use case composition.
     * Returns OutputData directly without using presenter.
     */
    public ReduceProductStockOutputData reduceStockInternal(ReduceProductStockInputData inputData) {
        ReduceProductStockOutputData outputData = null;
        Exception errorException = null;
        
        // Step 1: Validation
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
            SanPham.checkInput(inputData.getProductId(), inputData.getQuantity());
        } catch (Exception e) {
            errorException = e;
        }
        
        // Step 2: Business logic - Reduce stock
        SanPham sanPham = null;
        if (errorException == null) {
            try {
                sanPham = productRepository.findById(inputData.getProductId())
                    .orElseThrow(() -> DomainException.productNotFound(String.valueOf(inputData.getProductId())));
                
                sanPham.giamTonKho(inputData.getQuantity());
                SanPham savedProduct = productRepository.save(sanPham);
                
                outputData = new ReduceProductStockOutputData(
                    savedProduct.getMaSanPham(),
                    savedProduct.getTenSanPham(),
                    savedProduct.getSoLuongTonKho()
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
            
            outputData = ReduceProductStockOutputData.forError(errorCode, message);
        }
        
        return outputData;
    }
}
