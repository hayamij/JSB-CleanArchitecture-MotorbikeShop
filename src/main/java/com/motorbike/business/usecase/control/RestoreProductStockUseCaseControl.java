package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.restorestock.RestoreProductStockInputData;
import com.motorbike.business.dto.restorestock.RestoreProductStockOutputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.input.RestoreProductStockInputBoundary;
import com.motorbike.business.usecase.output.RestoreProductStockOutputBoundary;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.exceptions.DomainException;
import com.motorbike.domain.exceptions.ValidationException;

public class RestoreProductStockUseCaseControl implements RestoreProductStockInputBoundary {
    
    private final RestoreProductStockOutputBoundary outputBoundary;
    private final ProductRepository productRepository;
    
    public RestoreProductStockUseCaseControl(
            RestoreProductStockOutputBoundary outputBoundary,
            ProductRepository productRepository) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
    }
    
    @Override
    public void execute(RestoreProductStockInputData inputData) {
        RestoreProductStockOutputData outputData = restoreStockInternal(inputData);
        outputBoundary.present(outputData);
    }
    
    public RestoreProductStockOutputData restoreStockInternal(RestoreProductStockInputData inputData) {
        RestoreProductStockOutputData outputData = null;
        Exception errorException = null;
        
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
            SanPham.checkInput(inputData.getProductId(), inputData.getQuantity());
        } catch (Exception e) {
            errorException = e;
        }
        
        SanPham sanPham = null;
        if (errorException == null) {
            try {
                sanPham = productRepository.findById(inputData.getProductId())
                    .orElseThrow(() -> DomainException.productNotFound(String.valueOf(inputData.getProductId())));
                
                sanPham.tangTonKho(inputData.getQuantity());
                SanPham savedProduct = productRepository.save(sanPham);
                
                outputData = new RestoreProductStockOutputData(
                    savedProduct.getMaSanPham(),
                    savedProduct.getTenSanPham(),
                    savedProduct.getSoLuongTonKho()
                );
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        if (errorException != null) {
            String errorCode = "SYSTEM_ERROR";
            String message = errorException.getMessage();
            
            if (errorException instanceof ValidationException) {
                errorCode = ((ValidationException) errorException).getErrorCode();
            } else if (errorException instanceof DomainException) {
                errorCode = ((DomainException) errorException).getErrorCode();
            }
            
            outputData = RestoreProductStockOutputData.forError(errorCode, message);
        }
        
        return outputData;
    }
}
