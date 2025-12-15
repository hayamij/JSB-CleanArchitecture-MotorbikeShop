package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.UpdateAccessoryViewModel;
import com.motorbike.business.dto.accessory.UpdateAccessoryOutputData;
import com.motorbike.business.usecase.output.UpdateAccessoryOutputBoundary;

public class UpdateAccessoryPresenter implements UpdateAccessoryOutputBoundary {

    private final UpdateAccessoryViewModel viewModel;

    public UpdateAccessoryPresenter(UpdateAccessoryViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(UpdateAccessoryOutputData outputData) {
        if (outputData.success) {
            viewModel.hasError = false;
            viewModel.accessory = outputData.accessory;
            viewModel.errorCode = null;
            viewModel.errorMessage = null;
        } else {
            viewModel.hasError = true;
            viewModel.errorCode = outputData.errorCode;
            viewModel.errorMessage = outputData.errorMessage;
            viewModel.accessory = null;
        }
    }
}
