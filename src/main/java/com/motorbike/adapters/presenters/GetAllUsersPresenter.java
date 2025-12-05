package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.GetAllUsersViewModel;
import com.motorbike.business.dto.user.GetAllUsersOutputData;
import com.motorbike.business.usecase.output.GetAllUsersOutputBoundary;

public class GetAllUsersPresenter implements GetAllUsersOutputBoundary {

    private final GetAllUsersViewModel viewModel;

    public GetAllUsersPresenter(GetAllUsersViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(GetAllUsersOutputData outputData) {
        if (outputData.isSuccess()) {
            viewModel.users = outputData.getUsers();
            viewModel.hasError = false;
            viewModel.errorCode = null;
            viewModel.errorMessage = null;
        } else {
            viewModel.users = null;
            viewModel.hasError = true;
            viewModel.errorCode = outputData.getErrorCode();
            viewModel.errorMessage = outputData.getErrorMessage();
        }
    }
}
