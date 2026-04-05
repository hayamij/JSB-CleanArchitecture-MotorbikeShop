package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.motorbike.DeleteMotorbikeInputData;
import com.motorbike.business.dto.motorbike.DeleteMotorbikeOutputData;
import com.motorbike.business.dto.archiveproduct.ArchiveProductInputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.input.DeleteMotorbikeInputBoundary;
import com.motorbike.business.usecase.input.ArchiveProductInputBoundary;
import com.motorbike.business.usecase.output.DeleteMotorbikeOutputBoundary;
import com.motorbike.domain.entities.XeMay;
import com.motorbike.domain.exceptions.DomainException;
import com.motorbike.domain.exceptions.ValidationException;
import com.motorbike.domain.exceptions.SystemException;

public class DeleteMotorbikeUseCaseControl implements DeleteMotorbikeInputBoundary {
    
    private final DeleteMotorbikeOutputBoundary outputBoundary;
    private final ProductRepository productRepository;
    private final ArchiveProductInputBoundary archiveProductUseCase;
    
    public DeleteMotorbikeUseCaseControl(DeleteMotorbikeOutputBoundary outputBoundary,
                                        ProductRepository productRepository,
                                        ArchiveProductInputBoundary archiveProductUseCase) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
        this.archiveProductUseCase = archiveProductUseCase;
    }
    
    // Constructor for tests with 2 params
    public DeleteMotorbikeUseCaseControl(
            DeleteMotorbikeOutputBoundary outputBoundary,
            ProductRepository productRepository) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
        this.archiveProductUseCase = new ArchiveProductUseCaseControl(null, productRepository);
    }
    
    @Override
    public void execute(DeleteMotorbikeInputData inputData) {
        DeleteMotorbikeOutputData outputData = null;
        Exception errorException = null;
        XeMay xeMay = null;
        
        // Step 1: Validation
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput();
            }
            
            if (inputData.getMaSanPham() == null) {
                throw ValidationException.invalidProductId();
            }
        } catch (Exception e) {
            errorException = e;
        }
        
        // Step 2: Check if motorbike exists
        if (errorException == null) {
            try {
                xeMay = (XeMay) productRepository.findById(inputData.getMaSanPham())
                    .orElseThrow(() -> DomainException.productNotFound(inputData.getMaSanPham()));
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        // Step 3: UC-56 - Archive motorbike (soft delete)
        if (errorException == null && xeMay != null) {
            try {
                String tenSanPham = xeMay.getTenSanPham();
                Long maSanPham = xeMay.getMaSanPham();
                
                // Use UC-56 for soft delete instead of hard delete
                ArchiveProductInputData archiveInput = new ArchiveProductInputData(maSanPham);
                var archiveResult = ((ArchiveProductUseCaseControl) archiveProductUseCase)
                    .archiveInternal(archiveInput);
                
                if (!archiveResult.isSuccess()) {
                    throw new DomainException(archiveResult.getErrorMessage(), archiveResult.getErrorCode());
                }
                
                outputData = DeleteMotorbikeOutputData.forSuccess(maSanPham, tenSanPham);
            } catch (Exception e) {
                errorException = e;
            }
        }
        
        // Step 4: Handle error
        if (errorException != null) {
            String errorCode = extractErrorCode(errorException);
            outputData = DeleteMotorbikeOutputData.forError(errorCode, errorException.getMessage());
        }
        
        // Step 5: Present result
        outputBoundary.present(outputData);
    }
    
    private String extractErrorCode(Exception exception) {
        if (exception instanceof ValidationException) {
            return ((ValidationException) exception).getErrorCode();
        } else if (exception instanceof DomainException) {
            return ((DomainException) exception).getErrorCode();
        } else if (exception instanceof SystemException) {
            return ((SystemException) exception).getErrorCode();
        }
        return "SYSTEM_ERROR";
    }
}
