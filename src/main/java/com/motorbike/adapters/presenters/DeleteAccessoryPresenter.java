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
        if (outputData.success) {
            viewModel.success = true;
            viewModel.message = outputData.message;
            viewModel.errorCode = null;
            viewModel.errorMessage = null;
        } else {
            viewModel.success = false;
            viewModel.errorCode = outputData.errorCode;
            viewModel.errorMessage = outputData.errorMessage;
            viewModel.message = null;
        }
    }
}
