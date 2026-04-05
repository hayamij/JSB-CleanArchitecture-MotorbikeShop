package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.DeleteMotorbikeViewModel;
import com.motorbike.business.dto.motorbike.DeleteMotorbikeOutputData;
import com.motorbike.business.usecase.output.DeleteMotorbikeOutputBoundary;

public class DeleteMotorbikePresenter implements DeleteMotorbikeOutputBoundary {
    
    private final DeleteMotorbikeViewModel viewModel;
    
    public DeleteMotorbikePresenter(DeleteMotorbikeViewModel viewModel) {
        this.viewModel = viewModel;
    }
    
    @Override
    public void present(DeleteMotorbikeOutputData outputData) {
        if (outputData.isSuccess()) {
            presentSuccess(outputData);
        } else {
            presentError(outputData);
        }
    }
    
    private void presentSuccess(DeleteMotorbikeOutputData outputData) {
        viewModel.success = true;
        viewModel.hasError = false;
        viewModel.maSanPham = outputData.getMaSanPham();
        viewModel.tenSanPham = outputData.getTenSanPham();
        viewModel.successMessage = "Xóa xe máy thành công: " + outputData.getTenSanPham();
    }
    
    private void presentError(DeleteMotorbikeOutputData outputData) {
        viewModel.success = false;
        viewModel.hasError = true;
        viewModel.errorCode = outputData.getErrorCode();
        viewModel.errorMessage = formatErrorMessage(outputData);
    }
    
    private String formatErrorMessage(DeleteMotorbikeOutputData outputData) {
        String message = outputData.getErrorMessage();
        if (message == null || message.isEmpty()) {
            return "Có lỗi xảy ra khi xóa xe máy";
        }
        return message;
    }
}
