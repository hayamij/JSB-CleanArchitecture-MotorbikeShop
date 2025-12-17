package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.checkproductduplication.CheckProductDuplicationInputData;
import com.motorbike.business.dto.checkproductduplication.CheckProductDuplicationOutputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.input.CheckProductDuplicationInputBoundary;
import com.motorbike.business.usecase.output.CheckProductDuplicationOutputBoundary;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.exceptions.ValidationException;

import java.util.Optional;

public class CheckProductDuplicationUseCaseControl implements CheckProductDuplicationInputBoundary {
    private final CheckProductDuplicationOutputBoundary outputBoundary;
    private final ProductRepository productRepository;

    public CheckProductDuplicationUseCaseControl(
            CheckProductDuplicationOutputBoundary outputBoundary,
            ProductRepository productRepository) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
    }

    // Constructor with SanPhamRepository (for backward compatibility)
    public CheckProductDuplicationUseCaseControl(
            com.motorbike.business.ports.SanPhamRepository sanPhamRepository,
            CheckProductDuplicationOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
        this.productRepository = (ProductRepository) sanPhamRepository;
    }

    @Override
    public void execute(CheckProductDuplicationInputData inputData) {
        CheckProductDuplicationOutputData outputData = checkDuplicationInternal(inputData);
        outputBoundary.present(outputData);
    }

    public CheckProductDuplicationOutputData checkDuplicationInternal(CheckProductDuplicationInputData inputData) {
        CheckProductDuplicationOutputData outputData = null;
        Exception errorException = null;

        // Step 1: Validation
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput("CheckProductDuplication");
            }
            if (inputData.getProductName() == null || inputData.getProductName().trim().isEmpty()) {
                throw ValidationException.invalidInput("Product name is required");
            }
            // productCode is optional - only validate if provided
        } catch (Exception e) {
            errorException = e;
        }

        // Step 2: Business logic - check for duplicates
        if (errorException == null) {
            try {
                String productName = inputData.getProductName().trim();
                String productCode = inputData.getProductCode() != null ? inputData.getProductCode().trim() : null;
                Long excludeId = inputData.getExcludeProductId();

                // Check product code duplication first (more specific identifier)
                if (productCode != null && !productCode.isEmpty()) {
                    Optional<SanPham> existingByCode = productRepository.findByMaSanPham(productCode);
                    if (existingByCode.isPresent()) {
                        SanPham existing = existingByCode.get();
                        // If excludeId is provided, ignore if it's the same product
                        if (excludeId == null || !existing.getMaSanPham().equals(excludeId)) {
                            outputData = new CheckProductDuplicationOutputData(
                                    true,
                                    "code",
                                    existing.getMaSanPham()
                            );
                            return outputData;
                        }
                    }
                }

                // Check product name duplication
                Optional<SanPham> existingByName = productRepository.findByTenSanPham(productName);
                if (existingByName.isPresent()) {
                    SanPham existing = existingByName.get();
                    // If excludeId is provided, ignore if it's the same product
                    if (excludeId == null || !existing.getMaSanPham().equals(excludeId)) {
                        outputData = new CheckProductDuplicationOutputData(
                                true,
                                "name",
                                existing.getMaSanPham()
                        );
                        return outputData;
                    }
                }

                // No duplication found
                outputData = new CheckProductDuplicationOutputData(false, null, null);

            } catch (Exception e) {
                errorException = e;
            }
        }

        // Step 3: Handle error
        if (errorException != null) {
            String errorCode = errorException instanceof ValidationException
                    ? ((ValidationException) errorException).getErrorCode()
                    : "CHECK_DUPLICATION_ERROR";
            outputData = CheckProductDuplicationOutputData.forError(errorCode, errorException.getMessage());
        }

        // Step 4: Return result
        return outputData;
    }
    
    // Alias method for compatibility
    public CheckProductDuplicationOutputData checkInternal(CheckProductDuplicationInputData inputData) {
        return checkDuplicationInternal(inputData);
    }
}
