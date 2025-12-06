package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.GetTopProductsViewModel;
import com.motorbike.adapters.viewmodels.GetTopProductsViewModel.ProductSalesDisplay;
import com.motorbike.business.dto.topproducts.GetTopProductsOutputData;
import com.motorbike.business.dto.topproducts.GetTopProductsOutputData.ProductSalesInfo;
import com.motorbike.business.usecase.output.GetTopProductsOutputBoundary;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GetTopProductsPresenter implements GetTopProductsOutputBoundary {
    
    private final GetTopProductsViewModel viewModel;
    
    public GetTopProductsPresenter(GetTopProductsViewModel viewModel) {
        this.viewModel = viewModel;
    }
    
    @Override
    public void present(GetTopProductsOutputData outputData) {
        if (outputData.isSuccess()) {
            presentSuccess(outputData);
        } else {
            presentError(outputData);
        }
    }
    
    private void presentSuccess(GetTopProductsOutputData outputData) {
        viewModel.success = true;
        viewModel.errorCode = null;
        viewModel.errorMessage = null;
        
        if (outputData.getProducts() != null) {
            viewModel.products = outputData.getProducts().stream()
                    .map(this::mapToDisplay)
                    .collect(Collectors.toList());
        } else {
            viewModel.products = new ArrayList<>();
        }
    }
    
    private void presentError(GetTopProductsOutputData outputData) {
        viewModel.success = false;
        viewModel.products = new ArrayList<>();
        viewModel.errorCode = outputData.getErrorCode();
        viewModel.errorMessage = formatErrorMessage(outputData);
    }
    
    private ProductSalesDisplay mapToDisplay(ProductSalesInfo info) {
        ProductSalesDisplay display = new ProductSalesDisplay();
        display.productId = info.getProductId();
        display.productName = info.getProductName();
        display.totalSold = info.getTotalSold();
        return display;
    }
    
    private String formatErrorMessage(GetTopProductsOutputData outputData) {
        String errorCode = outputData.getErrorCode();
        String errorMessage = outputData.getErrorMessage();
        
        if ("VALIDATION_ERROR".equals(errorCode)) {
            return "Lỗi validation: " + errorMessage;
        } else if ("INTERNAL_ERROR".equals(errorCode)) {
            return "Lỗi hệ thống: " + errorMessage;
        }
        
        return errorMessage != null ? errorMessage : "Đã xảy ra lỗi";
    }
}
