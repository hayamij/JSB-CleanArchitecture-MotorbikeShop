package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.ExportAccessoriesViewModel;
import com.motorbike.business.dto.accessory.ExportAccessoriesOutputData;
import com.motorbike.business.usecase.output.ExportAccessoriesOutputBoundary;

/**
 * Presenter cho Export Accessories Use Case.
 * 
 * Chuyển đổi OutputData từ Business Layer thành ViewModel
 * cho Adapter Layer (Controller).
 */
public class ExportAccessoriesPresenter implements ExportAccessoriesOutputBoundary {
    
    private final ExportAccessoriesViewModel viewModel;
    
    public ExportAccessoriesPresenter(ExportAccessoriesViewModel viewModel) {
        this.viewModel = viewModel;
    }
    
    @Override
    public void presentSuccess(ExportAccessoriesOutputData outputData) {
        viewModel.hasError = false;
        viewModel.errorMessage = null;
        viewModel.exportResult = outputData;
    }
    
    @Override
    public void presentError(String errorMessage) {
        viewModel.hasError = true;
        viewModel.errorMessage = errorMessage;
        viewModel.exportResult = null;
    }
}
