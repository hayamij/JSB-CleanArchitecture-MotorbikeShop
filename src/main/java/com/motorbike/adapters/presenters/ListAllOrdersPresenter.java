package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.ListAllOrdersViewModel;
import com.motorbike.business.dto.listallorders.ListAllOrdersOutputData;
import com.motorbike.business.usecase.output.ListAllOrdersOutputBoundary;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Presenter for List All Orders Use Case
 */
public class ListAllOrdersPresenter implements ListAllOrdersOutputBoundary {
    
    private final ListAllOrdersViewModel viewModel;
    private static final NumberFormat VND_FORMAT = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public ListAllOrdersPresenter(ListAllOrdersViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(ListAllOrdersOutputData outputData) {
        if (!outputData.isSuccess()) {
            presentError(outputData);
        } else if (outputData.isEmpty()) {
            presentEmptyResult(outputData);
        } else {
            presentSuccess(outputData);
        }
    }

    private void presentSuccess(ListAllOrdersOutputData outputData) {
        List<ListAllOrdersViewModel.OrderItemViewModel> formattedOrders = new ArrayList<>();

        for (ListAllOrdersOutputData.OrderItemData order : outputData.getOrders()) {
            formattedOrders.add(new ListAllOrdersViewModel.OrderItemViewModel(
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
        viewModel.totalOrders = outputData.getTotalOrders();
        viewModel.totalPages = outputData.getTotalPages();
        viewModel.currentPage = outputData.getCurrentPage();
        viewModel.pageSize = outputData.getPageSize();
        viewModel.formattedTotalRevenue = VND_FORMAT.format(outputData.getTotalRevenue());
        viewModel.message = String.format(
                "Tổng %d đơn hàng | Doanh thu: %s | Trang %d/%d",
                outputData.getTotalOrders(),
                VND_FORMAT.format(outputData.getTotalRevenue()),
                outputData.getCurrentPage() + 1,
                outputData.getTotalPages()
        );
        viewModel.errorCode = null;
        viewModel.errorMessage = null;
    }

    private void presentEmptyResult(ListAllOrdersOutputData outputData) {
        viewModel.success = true;
        viewModel.hasError = false;
        viewModel.orders = new ArrayList<>();
        viewModel.totalOrders = 0;
        viewModel.totalPages = 0;
        viewModel.currentPage = outputData.getCurrentPage();
        viewModel.pageSize = outputData.getPageSize();
        viewModel.formattedTotalRevenue = "0 ₫";
        viewModel.message = "Không có đơn hàng nào";
        viewModel.errorCode = null;
        viewModel.errorMessage = null;
    }

    private void presentError(ListAllOrdersOutputData outputData) {
        viewModel.success = false;
        viewModel.hasError = true;
        viewModel.orders = new ArrayList<>();
        viewModel.totalOrders = 0;
        viewModel.totalPages = 0;
        viewModel.currentPage = 0;
        viewModel.pageSize = 0;
        viewModel.formattedTotalRevenue = "0 ₫";
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