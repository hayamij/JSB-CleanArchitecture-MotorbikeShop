package com.motorbike.adapters.presenters;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.motorbike.adapters.viewmodels.ListMyOrdersViewModel;
import com.motorbike.business.dto.listmyorders.ListMyOrdersOutputData;
import com.motorbike.business.usecase.output.ListMyOrdersOutputBoundary;

public class ListMyOrdersPresenter implements ListMyOrdersOutputBoundary {
    
    private final ListMyOrdersViewModel viewModel;
    private static final NumberFormat VND_FORMAT = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public ListMyOrdersPresenter(ListMyOrdersViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(ListMyOrdersOutputData outputData) {
        if (!outputData.isSuccess()) {
            presentError(outputData);
        } else if (outputData.isEmpty()) {
            presentEmptyResult();
        } else {
            presentSuccess(outputData);
        }
    }

    private void presentSuccess(ListMyOrdersOutputData outputData) {
        List<ListMyOrdersViewModel.OrderItemViewModel> formattedOrders = new ArrayList<>();

        for (ListMyOrdersOutputData.OrderItemData order : outputData.getOrders()) {
            formattedOrders.add(new ListMyOrdersViewModel.OrderItemViewModel(
                    order.getOrderId(),
                    order.getCustomerId(),
                    order.getCustomerName(),
                    order.getCustomerPhone(),
                    order.getShippingAddress(),
                    formatOrderStatus(order.getOrderStatus()),
                    VND_FORMAT.format(order.getTotalAmount()),
                    order.getTotalItems(),
                    order.getTotalQuantity(),
                    order.getOrderDate().format(DATE_FORMATTER),
                    getStatusColor(order.getOrderStatus())
            ));
        }

        viewModel.success = true;
        viewModel.hasError = false;
        viewModel.orders = formattedOrders;
        viewModel.message = "Lấy danh sách đơn hàng thành công";
        viewModel.errorCode = null;
        viewModel.errorMessage = null;
    }

    private void presentEmptyResult() {
        viewModel.success = true;
        viewModel.hasError = false;
        viewModel.orders = new ArrayList<>();
        viewModel.message = "Bạn chưa có đơn hàng nào";
        viewModel.errorCode = null;
        viewModel.errorMessage = null;
    }

    private void presentError(ListMyOrdersOutputData outputData) {
        viewModel.success = false;
        viewModel.hasError = true;
        viewModel.orders = new ArrayList<>();
        viewModel.message = "Lỗi khi lấy danh sách đơn hàng";
        viewModel.errorCode = outputData.getErrorCode();
        viewModel.errorMessage = outputData.getErrorMessage();
    }

    private String formatOrderStatus(String status) {
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

    private String getStatusColor(String status) {
        switch (status) {
            case "CHO_XAC_NHAN":
                return "ORANGE";
            case "DA_XAC_NHAN":
                return "BLUE";
            case "DANG_GIAO":
                return "PURPLE";
            case "DA_GIAO":
                return "GREEN";
            case "DA_HUY":
                return "RED";
            default:
                return "GRAY";
        }
    }
}
