package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.CreateUserViewModel;
import com.motorbike.business.dto.user.CreateUserOutputData;
import com.motorbike.business.usecase.output.CreateUserOutputBoundary;

public class CreateUserPresenter implements CreateUserOutputBoundary {
    
    private final CreateUserViewModel viewModel;
    
    public CreateUserPresenter(CreateUserViewModel viewModel) {
        this.viewModel = viewModel;
    }
    
    @Override
    public void present(CreateUserOutputData outputData) {
        if (outputData.isSuccess()) {
            presentSuccess(outputData);
        } else {
            presentError(outputData);
        }
    }
    
    private void presentSuccess(CreateUserOutputData outputData) {
        viewModel.success = true;
        viewModel.hasError = false;
        viewModel.maTaiKhoan = outputData.getMaTaiKhoan();
        viewModel.email = outputData.getEmail();
        viewModel.tenDangNhap = outputData.getTenDangNhap();
    }
    
    private void presentError(CreateUserOutputData outputData) {
        viewModel.success = false;
        viewModel.hasError = true;
        viewModel.errorCode = outputData.getErrorCode();
        viewModel.errorMessage = outputData.getErrorMessage();
    }
}
