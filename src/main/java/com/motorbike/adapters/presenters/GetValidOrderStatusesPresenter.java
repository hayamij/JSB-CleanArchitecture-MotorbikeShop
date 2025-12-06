package com.motorbike.adapters.presenters;

import com.motorbike.business.dto.order.GetValidOrderStatusesOutputData;
import com.motorbike.business.usecase.output.GetValidOrderStatusesOutputBoundary;
import com.motorbike.adapters.viewmodels.GetValidOrderStatusesViewModel;
import com.motorbike.adapters.viewmodels.GetValidOrderStatusesViewModel.StatusOption;

import java.util.List;
import java.util.stream.Collectors;

public class GetValidOrderStatusesPresenter implements GetValidOrderStatusesOutputBoundary {

    private final GetValidOrderStatusesViewModel viewModel;

    public GetValidOrderStatusesPresenter(GetValidOrderStatusesViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(GetValidOrderStatusesOutputData outputData) {
        if (outputData.isSuccess()) {
            viewModel.success = true;
            viewModel.validStatuses = outputData.getValidStatuses().stream()
                    .map(this::formatStatusOption)
                    .collect(Collectors.toList());
            viewModel.errorCode = null;
            viewModel.errorMessage = null;
        } else {
            viewModel.success = false;
            viewModel.validStatuses = null;
            viewModel.errorCode = outputData.getErrorCode();
            viewModel.errorMessage = outputData.getErrorMessage();
        }
    }

    private StatusOption formatStatusOption(String statusCode) {
        String display = formatStatusDisplay(statusCode);
        return new StatusOption(statusCode, display);
    }

    private String formatStatusDisplay(String statusCode) {
        switch (statusCode) {
            case "CHO_XAC_NHAN":
                return "Chờ xác nhận";
            case "DA_XAC_NHAN":
                return "Đã xác nhận";
            case "DANG_GIAO":
                return "Đang giao";
            case "DA_GIAO":
                return "Đã giao";
            case "DA_HUY":
                return "Đã hủy";
            default:
                return statusCode;
        }
    }
}
