package com.motorbike.adapters.presenters;

import com.motorbike.business.dto.motorbike.UpdateMotorbikeOutputData;
import com.motorbike.business.usecase.output.UpdateMotorbikeOutputBoundary;
import com.motorbike.adapters.viewmodels.UpdateMotorbikeViewModel;

public class UpdateMotorbikePresenter implements UpdateMotorbikeOutputBoundary {

    public UpdateMotorbikeViewModel viewModel;

    public UpdateMotorbikePresenter(UpdateMotorbikeViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(UpdateMotorbikeOutputData outputData) {

        if (outputData.errorCode != null) {
            // Gán thẳng vào viewModel HIỆN TẠI (KHÔNG new object mới)
            viewModel.hasError = true;
            viewModel.errorCode = outputData.errorCode;
            viewModel.errorMessage = outputData.errorMessage;
            viewModel.motorbike = null;
        } else {
            viewModel.hasError = false;
            viewModel.errorCode = null;
            viewModel.errorMessage = null;

            // dữ liệu trả về cho controller
            viewModel.motorbike = outputData.motorbike;
        }
}



}
