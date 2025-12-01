package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.SearchMotorbikesViewModel;
import com.motorbike.business.dto.motorbike.SearchMotorbikesOutputData;
import com.motorbike.business.usecase.output.SearchMotorbikesOutputBoundary;

public class SearchMotorbikesPresenter implements SearchMotorbikesOutputBoundary {

    private final SearchMotorbikesViewModel viewModel;

    public SearchMotorbikesPresenter(SearchMotorbikesViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(SearchMotorbikesOutputData outputData) {
        // reset state
        viewModel.hasError = false;
        viewModel.errorCode = null;
        viewModel.errorMessage = null;
        viewModel.motorbikes = null;

        if (outputData.hasError) {       // dùng field, không dùng hasError()
            viewModel.hasError = true;
            viewModel.errorCode = outputData.errorCode;          // field
            viewModel.errorMessage = outputData.errorMessage;    // field
            // motorbikes để null trong trường hợp lỗi
        } else {
            viewModel.motorbikes = outputData.motorbikes;        // field
        }
    }
}

