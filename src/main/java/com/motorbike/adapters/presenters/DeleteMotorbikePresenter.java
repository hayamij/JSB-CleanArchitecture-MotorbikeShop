package com.motorbike.adapters.presenters;

import com.motorbike.business.dto.motorbike.DeleteMotorbikeOutputData;
import com.motorbike.business.usecase.output.DeleteMotorbikeOutputBoundary;
import com.motorbike.adapters.viewmodels.DeleteMotorbikeViewModel;

public class DeleteMotorbikePresenter implements DeleteMotorbikeOutputBoundary {

    public DeleteMotorbikeViewModel viewModel;

    public DeleteMotorbikePresenter(DeleteMotorbikeViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(DeleteMotorbikeOutputData outputData) {

        if (!outputData.success) {
            viewModel.success = false;
            viewModel.errorCode = outputData.errorCode;
            viewModel.errorMessage = outputData.errorMessage;
        } else {
            viewModel.success = true;
            viewModel.errorCode = null;
            viewModel.errorMessage = null;
        }
    }

}
