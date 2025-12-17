package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.CheckInventoryAvailabilityViewModel;
import com.motorbike.business.dto.checkinventory.CheckInventoryAvailabilityOutputData;
import com.motorbike.business.usecase.output.CheckInventoryAvailabilityOutputBoundary;

public class CheckInventoryAvailabilityPresenter implements CheckInventoryAvailabilityOutputBoundary {
    
    private final CheckInventoryAvailabilityViewModel viewModel;
    
    public CheckInventoryAvailabilityPresenter(CheckInventoryAvailabilityViewModel viewModel) {
        this.viewModel = viewModel;
    }
    
    @Override
    public void present(CheckInventoryAvailabilityOutputData outputData) {
        if (outputData.isSuccess()) {
            presentSuccess(outputData);
        } else {
            presentError(outputData);
        }
    }
    
    private void presentSuccess(CheckInventoryAvailabilityOutputData outputData) {
        viewModel.success = true;
        viewModel.hasError = false;
        viewModel.productId = outputData.getProductId();
        viewModel.productName = outputData.getProductName();
        viewModel.available = outputData.isAvailable();
        viewModel.availableStock = outputData.getAvailableStock();
        
        if (outputData.isAvailable()) {
            viewModel.message = String.format("Sản phẩm '%s' có sẵn trong kho (còn %d sản phẩm)",
                outputData.getProductName(), outputData.getAvailableStock());
        } else {
            viewModel.message = String.format("Sản phẩm '%s' không đủ số lượng (chỉ còn %d sản phẩm)",
                outputData.getProductName(), outputData.getAvailableStock());
        }
        
        viewModel.errorCode = null;
        viewModel.errorMessage = null;
    }
    
    private void presentError(CheckInventoryAvailabilityOutputData outputData) {
        viewModel.success = false;
        viewModel.hasError = true;
        viewModel.available = false;
        viewModel.availableStock = 0;
        viewModel.productId = null;
        viewModel.productName = null;
        viewModel.errorCode = outputData.getErrorCode();
        viewModel.errorMessage = translateErrorMessage(outputData.getErrorCode(), outputData.getErrorMessage());
        viewModel.message = viewModel.errorMessage;
    }
    
    private String translateErrorMessage(String errorCode, String originalMessage) {
        if (errorCode == null) return originalMessage;
        
        switch (errorCode) {
            case "INVALID_PRODUCT_ID":
                return "Mã sản phẩm không hợp lệ";
            case "INVALID_QUANTITY":
                return "Số lượng không hợp lệ";
            case "PRODUCT_NOT_FOUND":
                return "Không tìm thấy sản phẩm";
            case "INVALID_INPUT":
                return "Dữ liệu đầu vào không hợp lệ";
            default:
                return "Lỗi hệ thống: " + originalMessage;
        }
    }
    
    public CheckInventoryAvailabilityViewModel getViewModel() {
        return viewModel;
    }
}
