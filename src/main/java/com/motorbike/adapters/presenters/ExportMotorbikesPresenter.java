package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.ExportMotorbikesViewModel;
import com.motorbike.business.dto.motorbike.ExportMotorbikesOutputData;
import com.motorbike.business.usecase.output.ExportMotorbikesOutputBoundary;

/**
 * Presenter cho Export Motorbikes Use Case.
 * 
 * Chuyển đổi OutputData từ Business Layer thành ViewModel
 * cho Adapter Layer (Controller).
 */
public class ExportMotorbikesPresenter implements ExportMotorbikesOutputBoundary {
    
    private final ExportMotorbikesViewModel viewModel;
    
    public ExportMotorbikesPresenter(ExportMotorbikesViewModel viewModel) {
        this.viewModel = viewModel;
    }
    
    @Override
    public void presentSuccess(ExportMotorbikesOutputData outputData) {
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
