package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.DeleteAccessoryViewModel;
import com.motorbike.business.dto.accessory.DeleteAccessoryOutputData;
import com.motorbike.business.usecase.output.DeleteAccessoryOutputBoundary;

public class DeleteAccessoryPresenter implements DeleteAccessoryOutputBoundary {
    
    private final DeleteAccessoryViewModel viewModel;
    
    public DeleteAccessoryPresenter(DeleteAccessoryViewModel viewModel) {
        this.viewModel = viewModel;
    }
    
    @Override
    public void present(DeleteAccessoryOutputData outputData) {
        if (outputData.isSuccess()) {
            presentSuccess(outputData);
        } else {
            presentError(outputData);
        }
    }
    
    private void presentSuccess(DeleteAccessoryOutputData outputData) {
        viewModel.success = true;
        viewModel.hasError = false;
        viewModel.maSanPham = outputData.getMaSanPham();
        viewModel.message = outputData.getMessage();
    }
    
    private void presentError(DeleteAccessoryOutputData outputData) {
        viewModel.success = false;
        viewModel.hasError = true;
        viewModel.errorCode = outputData.getErrorCode();
        viewModel.errorMessage = formatErrorMessage(outputData);
    }
    
    private String formatErrorMessage(DeleteAccessoryOutputData outputData) {
        String message = outputData.getErrorMessage();
        if (message == null || message.isEmpty()) {
            return "Có lỗi xảy ra khi xóa phụ kiện";
        }
        return message;
    }
}
