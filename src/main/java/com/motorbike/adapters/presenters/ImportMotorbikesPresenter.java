package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.ImportMotorbikesViewModel;
import com.motorbike.business.dto.motorbike.ImportMotorbikesOutputData;
import com.motorbike.business.usecase.output.ImportMotorbikesOutputBoundary;

/**
 * Presenter cho Import Motorbikes Use Case.
 * 
 * Chuyển đổi OutputData từ Business Layer thành ViewModel
 * cho Adapter Layer (Controller).
 */
public class ImportMotorbikesPresenter implements ImportMotorbikesOutputBoundary {
    
    private final ImportMotorbikesViewModel viewModel;
    
    public ImportMotorbikesPresenter(ImportMotorbikesViewModel viewModel) {
        this.viewModel = viewModel;
    }
    
    @Override
    public void present(ImportMotorbikesOutputData outputData) {
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
