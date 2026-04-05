package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.ImportAccessoriesViewModel;
import com.motorbike.business.dto.accessory.ImportAccessoriesOutputData;
import com.motorbike.business.usecase.output.ImportAccessoriesOutputBoundary;

/**
 * Presenter cho Import Accessories Use Case.
 * 
 * Chuyển đổi OutputData từ Business Layer thành ViewModel
 * cho Adapter Layer (Controller).
 */
public class ImportAccessoriesPresenter implements ImportAccessoriesOutputBoundary {
    
    private final ImportAccessoriesViewModel viewModel;
    
    public ImportAccessoriesPresenter(ImportAccessoriesViewModel viewModel) {
        this.viewModel = viewModel;
    }
    
    @Override
    public void present(ImportAccessoriesOutputData outputData) {
        if (outputData.hasError()) {
            viewModel.hasError = true;
            viewModel.errorCode = outputData.getErrorCode();
            viewModel.errorMessage = outputData.getErrorMessage();
            viewModel.importResult = null;
        } else {
            viewModel.hasError = false;
            viewModel.errorCode = null;
            viewModel.errorMessage = null;
            viewModel.importResult = outputData;
        }
    }
}
