package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.SearchUsersViewModel;
import com.motorbike.business.dto.user.SearchUsersOutputData;
import com.motorbike.business.usecase.output.SearchUsersOutputBoundary;

public class SearchUsersPresenter implements SearchUsersOutputBoundary {

    private final SearchUsersViewModel viewModel;

    public SearchUsersPresenter(SearchUsersViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(SearchUsersOutputData outputData) {
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
