package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.AddMotorbikeViewModel;
import com.motorbike.business.dto.motorbike.AddMotorbikeOutputData;
import com.motorbike.business.usecase.output.AddMotorbikeOutputBoundary;

public class AddMotorbikePresenter implements AddMotorbikeOutputBoundary {

    private final AddMotorbikeViewModel viewModel;

    public AddMotorbikePresenter(AddMotorbikeViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(AddMotorbikeOutputData outputData) {

        if (outputData.hasError) {
            viewModel.hasError = true;
            viewModel.errorCode = outputData.errorCode;
            viewModel.errorMessage = outputData.errorMessage;
            viewModel.motorbike = null;
        } else {
            viewModel.hasError = false;
            viewModel.errorCode = null;
            viewModel.errorMessage = null;
            viewModel.motorbike = outputData.motorbike; 
        }
    }
}
