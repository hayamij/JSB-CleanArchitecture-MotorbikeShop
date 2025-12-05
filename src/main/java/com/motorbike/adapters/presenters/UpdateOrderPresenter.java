package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.UpdateOrderViewModel;
import com.motorbike.business.dto.order.UpdateOrderOutputData;
import com.motorbike.business.usecase.output.UpdateOrderOutputBoundary;

public class UpdateOrderPresenter implements UpdateOrderOutputBoundary {
    
    private final UpdateOrderViewModel viewModel;
    
    public UpdateOrderPresenter(UpdateOrderViewModel viewModel) {
        this.viewModel = viewModel;
    }
    
    @Override
    public void present(UpdateOrderOutputData outputData) {
        if (outputData.isSuccess()) {
            presentSuccess(outputData);
        } else {
            presentError(outputData);
        }
    }
    
    private void presentSuccess(UpdateOrderOutputData outputData) {
        viewModel.success = true;
        viewModel.hasError = false;
        viewModel.maDonHang = outputData.getMaDonHang();
        viewModel.message = outputData.getMessage();
    }
    
    private void presentError(UpdateOrderOutputData outputData) {
        viewModel.success = false;
        viewModel.hasError = true;
        viewModel.errorCode = outputData.getErrorCode();
        viewModel.errorMessage = outputData.getErrorMessage();
    }
}
