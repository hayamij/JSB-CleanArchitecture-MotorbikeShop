package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.archiveproduct.ArchiveProductInputData;
import com.motorbike.business.dto.archiveproduct.ArchiveProductOutputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.input.ArchiveProductInputBoundary;
import com.motorbike.business.usecase.output.ArchiveProductOutputBoundary;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.exceptions.DomainException;
import com.motorbike.domain.exceptions.ValidationException;

import java.time.LocalDateTime;

public class ArchiveProductUseCaseControl implements ArchiveProductInputBoundary {
    private final ArchiveProductOutputBoundary outputBoundary;
    private final ProductRepository productRepository;

    public ArchiveProductUseCaseControl(
            ArchiveProductOutputBoundary outputBoundary,
            ProductRepository productRepository) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
    }

    // Constructor with SanPhamRepository (for backward compatibility)
    public ArchiveProductUseCaseControl(
            com.motorbike.business.ports.SanPhamRepository sanPhamRepository,
            ArchiveProductOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
        this.productRepository = (ProductRepository) sanPhamRepository;
    }

    @Override
    public void execute(ArchiveProductInputData inputData) {
        ArchiveProductOutputData outputData = archiveInternal(inputData);
        outputBoundary.present(outputData);
    }

    public ArchiveProductOutputData archiveInternal(ArchiveProductInputData inputData) {
        ArchiveProductOutputData outputData = null;
        Exception errorException = null;

        // Step 1: Validation
        try {
            if (inputData == null) {
                throw ValidationException.invalidInput("ArchiveProduct");
            }
            if (inputData.getProductId() == null) {
                throw ValidationException.invalidInput("Product ID is required");
            }
        } catch (Exception e) {
            errorException = e;
        }

        // Step 2: Business logic - archive product (soft delete)
        if (errorException == null) {
            try {
                Long productId = inputData.getProductId();

                // Find product
                SanPham product = productRepository.findById(productId)
                        .orElseThrow(() -> DomainException.notFound(
                                "Không tìm thấy sản phẩm với ID: " + productId,
                                "PRODUCT_NOT_FOUND"
                        ));

                // Mark as archived (set stock to 0 and add archived flag if implemented)
                // For now, we'll set stock to 0 to indicate archived status
                // In a real implementation, you might add an 'archived' boolean field to SanPham
                product.setSoLuongTonKho(0);
                
                // Save the archived product
                productRepository.save(product);

                LocalDateTime archivedAt = LocalDateTime.now();

                outputData = new ArchiveProductOutputData(
                        product.getMaSanPham(),
                        product.getTenSanPham(),
                        archivedAt
                );

            } catch (Exception e) {
                errorException = e;
            }
        }

        // Step 3: Handle error
        if (errorException != null) {
            String errorCode;
            if (errorException instanceof ValidationException) {
                errorCode = ((ValidationException) errorException).getErrorCode();
            } else if (errorException instanceof DomainException) {
                errorCode = ((DomainException) errorException).getErrorCode();
            } else {
                errorCode = "ARCHIVE_ERROR";
            }
            outputData = ArchiveProductOutputData.forError(errorCode, errorException.getMessage());
        }

        // Step 4: Return result
        return outputData;
    }
}
