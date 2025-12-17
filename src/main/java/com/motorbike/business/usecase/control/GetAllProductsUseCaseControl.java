package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.product.GetAllProductsOutputData;
import com.motorbike.business.dto.product.GetAllProductsOutputData.ProductInfo;
import com.motorbike.business.dto.product.FormatProductsForDisplayInputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.input.GetAllProductsInputBoundary;
import com.motorbike.business.usecase.input.FormatProductsForDisplayInputBoundary;
import com.motorbike.business.usecase.output.GetAllProductsOutputBoundary;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.entities.XeMay;
import com.motorbike.domain.entities.PhuKienXeMay;
import com.motorbike.domain.exceptions.SystemException;

import java.util.List;
import java.util.stream.Collectors;

public class GetAllProductsUseCaseControl implements GetAllProductsInputBoundary {

    private final GetAllProductsOutputBoundary outputBoundary;
    private final ProductRepository productRepository;
    private final FormatProductsForDisplayInputBoundary formatProductsUseCase;

    public GetAllProductsUseCaseControl(
            GetAllProductsOutputBoundary outputBoundary,
            ProductRepository productRepository,
            FormatProductsForDisplayInputBoundary formatProductsUseCase
    ) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
        this.formatProductsUseCase = formatProductsUseCase;
    }

    // Backward compatibility constructor
    public GetAllProductsUseCaseControl(
            GetAllProductsOutputBoundary outputBoundary,
            ProductRepository productRepository
    ) {
        this.outputBoundary = outputBoundary;
        this.productRepository = productRepository;
        this.formatProductsUseCase = new FormatProductsForDisplayUseCaseControl(null);
    }

    @Override
    public void execute() {
        GetAllProductsOutputData outputData = null;
        Exception errorException = null;

        // Business logic
        try {
            // Step 1: Get all products from repository
            List<SanPham> allProducts = productRepository.findAll();

            // Step 2: UC-77 Format products for display
            FormatProductsForDisplayInputData formatInput = new FormatProductsForDisplayInputData(allProducts);
            var formatResult = ((FormatProductsForDisplayUseCaseControl) formatProductsUseCase).formatInternal(formatInput);
            
            if (!formatResult.isSuccess()) {
                throw new SystemException(formatResult.getErrorMessage(), formatResult.getErrorCode());
            }

            outputData = GetAllProductsOutputData.forSuccess(formatResult.getProductInfos());
        } catch (Exception e) {
            errorException = e;
        }

        // Error handling
        if (errorException != null) {
            String errorCode = "GET_ALL_PRODUCTS_ERROR";
            outputData = GetAllProductsOutputData.forError(errorCode, errorException.getMessage());
        }

        // Present
        outputBoundary.present(outputData);
    }
}
