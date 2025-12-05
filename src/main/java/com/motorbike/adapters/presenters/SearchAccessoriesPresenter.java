package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.SearchAccessoriesViewModel;
import com.motorbike.business.dto.accessory.SearchAccessoriesOutputData;
import com.motorbike.business.usecase.output.SearchAccessoriesOutputBoundary;

public class SearchAccessoriesPresenter implements SearchAccessoriesOutputBoundary {

    private final SearchAccessoriesViewModel viewModel;

    public SearchAccessoriesPresenter(SearchAccessoriesViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(SearchAccessoriesOutputData outputData) {
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
