package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.CreateAccessoryViewModel;
import com.motorbike.business.dto.accessory.CreateAccessoryOutputData;
import com.motorbike.business.usecase.output.CreateAccessoryOutputBoundary;

public class CreateAccessoryPresenter implements CreateAccessoryOutputBoundary {
    
    private final CreateAccessoryViewModel viewModel;
    
    public CreateAccessoryPresenter(CreateAccessoryViewModel viewModel) {
        this.viewModel = viewModel;
    }
    
    @Override
    public void present(CreateAccessoryOutputData outputData) {
        if (outputData.isSuccess()) {
            presentSuccess(outputData);
        } else {
            presentError(outputData);
        }
    }
    
    private void presentSuccess(CreateAccessoryOutputData outputData) {
        viewModel.success = true;
        viewModel.hasError = false;
        viewModel.maSanPham = outputData.getMaSanPham();
        viewModel.tenSanPham = outputData.getTenSanPham();
        viewModel.gia = outputData.getGia();
    }
    
    private void presentError(CreateAccessoryOutputData outputData) {
        viewModel.success = false;
        viewModel.hasError = true;
        viewModel.errorCode = outputData.getErrorCode();
        viewModel.errorMessage = outputData.getErrorMessage();
    }
}
