package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.DeleteUserViewModel;
import com.motorbike.business.dto.user.DeleteUserOutputData;
import com.motorbike.business.usecase.output.DeleteUserOutputBoundary;

public class DeleteUserPresenter implements DeleteUserOutputBoundary {
    
    private final DeleteUserViewModel viewModel;
    
    public DeleteUserPresenter(DeleteUserViewModel viewModel) {
        this.viewModel = viewModel;
    }
    
    @Override
    public void present(DeleteUserOutputData outputData) {
        if (outputData.isSuccess()) {
            presentSuccess(outputData);
        } else {
            presentError(outputData);
        }
    }
    
    private void presentSuccess(DeleteUserOutputData outputData) {
        viewModel.success = true;
        viewModel.hasError = false;
        viewModel.maTaiKhoan = outputData.getMaTaiKhoan();
        viewModel.tenDangNhap = outputData.getTenDangNhap();
        viewModel.successMessage = "Xóa người dùng thành công: " + outputData.getTenDangNhap();
    }
    
    private void presentError(DeleteUserOutputData outputData) {
        viewModel.success = false;
        viewModel.hasError = true;
        viewModel.errorCode = outputData.getErrorCode();
        viewModel.errorMessage = formatErrorMessage(outputData);
    }
    
    private String formatErrorMessage(DeleteUserOutputData outputData) {
        String message = outputData.getErrorMessage();
        if (message == null || message.isEmpty()) {
            return "Có lỗi xảy ra khi xóa người dùng";
        }
        return message;
    }
}
