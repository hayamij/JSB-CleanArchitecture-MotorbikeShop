package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.CreateMotorbikeViewModel;
import com.motorbike.business.dto.motorbike.CreateMotorbikeOutputData;
import com.motorbike.business.usecase.output.CreateMotorbikeOutputBoundary;

public class CreateMotorbikePresenter implements CreateMotorbikeOutputBoundary {
    
    private final CreateMotorbikeViewModel viewModel;
    
    public CreateMotorbikePresenter(CreateMotorbikeViewModel viewModel) {
        this.viewModel = viewModel;
    }
    
    @Override
    public void present(CreateMotorbikeOutputData outputData) {
        if (outputData.isSuccess()) {
            presentSuccess(outputData);
        } else {
            presentError(outputData);
        }
    }
    
    private void presentSuccess(CreateMotorbikeOutputData outputData) {
        viewModel.success = true;
        viewModel.hasError = false;
        viewModel.maSanPham = outputData.getMaSanPham();
        viewModel.tenSanPham = outputData.getTenSanPham();
        viewModel.gia = outputData.getGia();
    }
    
    private void presentError(CreateMotorbikeOutputData outputData) {
        viewModel.success = false;
        viewModel.hasError = true;
        viewModel.errorCode = outputData.getErrorCode();
        viewModel.errorMessage = outputData.getErrorMessage();
    }
}
