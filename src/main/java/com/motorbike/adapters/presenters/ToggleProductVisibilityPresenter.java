package com.motorbike.adapters.presenters;

import com.motorbike.business.dto.product.ToggleProductVisibilityOutputData;
import com.motorbike.business.usecase.output.ToggleProductVisibilityOutputBoundary;
import com.motorbike.adapters.viewmodels.ToggleProductVisibilityViewModel;

public class ToggleProductVisibilityPresenter implements ToggleProductVisibilityOutputBoundary {

    private final ToggleProductVisibilityViewModel viewModel;

    public ToggleProductVisibilityPresenter(ToggleProductVisibilityViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(ToggleProductVisibilityOutputData outputData) {
        if (outputData.isSuccess()) {
            viewModel.success = true;
            viewModel.isVisible = outputData.isVisible();
            viewModel.message = outputData.getMessage();
            viewModel.errorCode = null;
            viewModel.errorMessage = null;
        } else {
            viewModel.success = false;
            viewModel.isVisible = false;
            viewModel.message = null;
            viewModel.errorCode = outputData.getErrorCode();
            viewModel.errorMessage = outputData.getErrorMessage();
        }
    }
}
