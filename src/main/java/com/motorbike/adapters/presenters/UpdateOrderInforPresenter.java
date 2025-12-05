package com.motorbike.adapters.presenters;

import java.time.format.DateTimeFormatter;

import com.motorbike.adapters.viewmodels.UpdateOrderInforViewModel;
import com.motorbike.business.dto.updateorderinfor.UpdateOrderInforOutputData;
import com.motorbike.business.usecase.output.UpdateOrderInforOutputBoundary;

public class UpdateOrderInforPresenter implements UpdateOrderInforOutputBoundary {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final UpdateOrderInforViewModel viewModel;

    public UpdateOrderInforPresenter(UpdateOrderInforViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(UpdateOrderInforOutputData outputData) {
        if (outputData.isSuccess()) {
            presentSuccess(outputData);
        } else {
            presentError(outputData);
        }
    }

    private void presentSuccess(UpdateOrderInforOutputData outputData) {
        viewModel.success = true;
        viewModel.hasError = false;

        viewModel.orderId = outputData.getOrderId();
        viewModel.customerId = outputData.getCustomerId();
        viewModel.receiverName = outputData.getReceiverName();
        viewModel.phoneNumber = outputData.getPhoneNumber();
        viewModel.shippingAddress = outputData.getShippingAddress();
        viewModel.note = outputData.getNote();
        viewModel.orderStatus = formatOrderStatus(outputData.getOrderStatus());
        viewModel.updatedAtDisplay = formatDateTime(outputData.getUpdatedAt());

        viewModel.message = "Cập nhật thông tin giao hàng thành công";
        viewModel.errorCode = null;
        viewModel.errorMessage = null;
        viewModel.errorColor = null;
    }

    private void presentError(UpdateOrderInforOutputData outputData) {
        viewModel.success = false;
        viewModel.hasError = true;

        viewModel.orderId = null;
        viewModel.customerId = null;
        viewModel.receiverName = null;
        viewModel.phoneNumber = null;
        viewModel.shippingAddress = null;
        viewModel.note = null;
        viewModel.orderStatus = null;
        viewModel.updatedAtDisplay = null;

        viewModel.message = "Cập nhật thông tin giao hàng thất bại";
        viewModel.errorCode = outputData.getErrorCode();
        viewModel.errorMessage = outputData.getErrorMessage();
        viewModel.errorColor = determineErrorColor(outputData.getErrorCode());
    }

    private String formatOrderStatus(String status) {
        if (status == null) {
            return "Không xác định";
        }

        switch (status) {
            case "CHO_XAC_NHAN":
                return "Chờ xác nhận";
            case "DA_XAC_NHAN":
                return "Đã xác nhận";
            case "DANG_GIAO":
                return "Đang giao hàng";
            case "DA_GIAO":
                return "Đã giao hàng";
            case "DA_HUY":
                return "Đã hủy";
            default:
                return status;
        }
    }

    private String formatDateTime(java.time.LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DATE_TIME_FORMATTER);
    }

    private String determineErrorColor(String errorCode) {
        if (errorCode == null) {
            return "RED";
        }

        switch (errorCode) {
            case "INVALID_USER_ID":
            case "NULL_ORDER_ID":
            case "MISSING_RECEIVER_NAME":
            case "MISSING_PHONE":
            case "MISSING_ADDRESS":
            case "CANNOT_UPDATE_ORDER":
                return "ORANGE";
            default:
                return "RED";
        }
    }
}
