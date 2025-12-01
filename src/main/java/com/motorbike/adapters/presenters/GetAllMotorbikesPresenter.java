package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.GetAllMotorbikesViewModel;
import com.motorbike.business.dto.motorbike.GetAllMotorbikesOutputData;
import com.motorbike.business.usecase.output.GetAllMotorbikesOutputBoundary;

public class GetAllMotorbikesPresenter implements GetAllMotorbikesOutputBoundary {

    private final GetAllMotorbikesViewModel viewModel;

    public GetAllMotorbikesPresenter(GetAllMotorbikesViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(GetAllMotorbikesOutputData outputData) {
        if (outputData.isSuccess()) {
            viewModel.motorbikes = outputData.getMotorbikes();
            viewModel.hasError = false;
            viewModel.errorCode = null;
            viewModel.errorMessage = null;
        } else {
            viewModel.motorbikes = null;
            viewModel.hasError = true;
            viewModel.errorCode = outputData.getErrorCode();
            viewModel.errorMessage = outputData.getErrorMessage();
        }
    }
}
