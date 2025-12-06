package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.product.ToggleProductVisibilityInputData;
import com.motorbike.business.dto.product.ToggleProductVisibilityOutputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.input.ToggleProductVisibilityInputBoundary;
import com.motorbike.business.usecase.output.ToggleProductVisibilityOutputBoundary;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.exceptions.ValidationException;

import java.util.Optional;

public class ToggleProductVisibilityUseCaseControl implements ToggleProductVisibilityInputBoundary {

    private final ToggleProductVisibilityOutputBoundary outputBoundary;
    private final ProductRepository productRepository;

    public ToggleProductVisibilityUseCaseControl(
            ToggleProductVisibilityOutputBoundary outputBoundary,
            ProductRepository productRepository
    ) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
    }

    @Override
    public void execute(ToggleProductVisibilityInputData inputData) {
        ToggleProductVisibilityOutputData outputData = null;
        Exception errorException = null;

        // Validation
        try {
            if (inputData == null || inputData.getProductId() == null) {
                throw ValidationException.invalidInput();
            }
        } catch (Exception e) {
            errorException = e;
        }

        // Business logic
        if (errorException == null) {
            try {
                Optional<SanPham> productOpt = productRepository.findById(inputData.getProductId());
                
                if (!productOpt.isPresent()) {
                    throw new ValidationException("PRODUCT_NOT_FOUND", "Không tìm thấy sản phẩm");
                }
                
                SanPham product = productOpt.get();
                
                // Use domain methods to toggle visibility
                if (product.isConHang()) {
                    product.ngungKinhDoanh();
                } else {
                    product.khoiPhucKinhDoanh();
                }
                
                productRepository.save(product);
                
                String message = product.isConHang() 
                        ? "Hiện sản phẩm thành công" 
                        : "Ẩn sản phẩm thành công";
                
                outputData = ToggleProductVisibilityOutputData.forSuccess(product.isConHang(), message);
            } catch (Exception e) {
                errorException = e;
            }
        }

        // Error handling
        if (errorException != null) {
            String errorCode = errorException instanceof ValidationException 
                    ? ((ValidationException) errorException).getErrorCode() 
                    : "TOGGLE_VISIBILITY_ERROR";
            outputData = ToggleProductVisibilityOutputData.forError(errorCode, errorException.getMessage());
        }

        // Present
        outputBoundary.present(outputData);
    }
}
