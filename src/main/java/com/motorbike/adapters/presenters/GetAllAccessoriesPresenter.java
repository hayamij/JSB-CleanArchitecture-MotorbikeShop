package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.GetAllAccessoriesViewModel;
import com.motorbike.business.dto.accessory.GetAllAccessoriesOutputData;
import com.motorbike.business.usecase.output.GetAllAccessoriesOutputBoundary;

public class GetAllAccessoriesPresenter implements GetAllAccessoriesOutputBoundary {

    private final GetAllAccessoriesViewModel viewModel;

    public GetAllAccessoriesPresenter(GetAllAccessoriesViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(GetAllAccessoriesOutputData outputData) {
        // reset state
        viewModel.hasError = false;
        viewModel.errorCode = null;
        viewModel.errorMessage = null;
        viewModel.accessories = null;

        if (outputData.hasError) {
            viewModel.hasError = true;
            viewModel.errorCode = outputData.errorCode;
            viewModel.errorMessage = outputData.errorMessage;
        } else {
            viewModel.accessories = outputData.accessories;
        }
    }
}
