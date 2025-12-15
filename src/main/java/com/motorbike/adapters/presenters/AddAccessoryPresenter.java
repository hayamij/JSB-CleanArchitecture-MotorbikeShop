package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.AddAccessoryViewModel;
import com.motorbike.business.dto.accessory.AddAccessoryOutputData;
import com.motorbike.business.usecase.output.AddAccessoryOutputBoundary;

public class AddAccessoryPresenter implements AddAccessoryOutputBoundary {

    private final AddAccessoryViewModel viewModel;

    public AddAccessoryPresenter(AddAccessoryViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(AddAccessoryOutputData outputData) {
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
