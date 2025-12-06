package com.motorbike.adapters.presenters;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.motorbike.adapters.viewmodels.OrderDetailViewModel;
import com.motorbike.business.dto.orderdetail.OrderDetailOutputData;
import com.motorbike.business.dto.orderdetail.OrderDetailOutputData.OrderData;
import com.motorbike.business.dto.orderdetail.OrderDetailOutputData.OrderItemData;
import com.motorbike.business.usecase.output.OrderDetailOutputBoundary;

public class OrderDetailPresenter implements OrderDetailOutputBoundary {

    private static final NumberFormat VND_FORMAT = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final OrderDetailViewModel viewModel;

    public OrderDetailPresenter(OrderDetailViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(OrderDetailOutputData outputData) {
        if (outputData == null || !outputData.isSuccess()) {
            presentError(outputData);
            return;
        }

        OrderData order = outputData.getOrder();
        List<OrderDetailViewModel.OrderItemViewModel> itemViewModels = new ArrayList<>();

        if (order != null && order.getItems() != null) {
            for (OrderItemData item : order.getItems()) {
                itemViewModels.add(new OrderDetailViewModel.OrderItemViewModel(
                        item.getOrderItemId(),
                        item.getProductId(),
                        item.getProductName(),
                        formatCurrency(item.getUnitPrice()),
                        item.getQuantity(),
                        formatCurrency(item.getLineTotal())
                ));
            }
        }

        viewModel.success = true;
        viewModel.hasError = false;
        viewModel.message = "Lấy chi tiết đơn hàng thành công";
        viewModel.orderId = order != null ? order.getOrderId() : null;
        viewModel.customerId = order != null ? order.getCustomerId() : null;
        viewModel.receiverName = order != null ? order.getReceiverName() : null;
        viewModel.phoneNumber = order != null ? order.getPhoneNumber() : null;
        viewModel.shippingAddress = order != null ? order.getShippingAddress() : null;
        viewModel.orderStatus = order != null ? formatStatus(order.getOrderStatus()) : null;
        viewModel.statusColor = order != null ? statusColor(order.getOrderStatus()) : "GRAY";
        viewModel.formattedTotalAmount = order != null ? formatCurrency(order.getTotalAmount()) : null;
        viewModel.totalItems = order != null ? order.getTotalItems() : 0;
        viewModel.totalQuantity = order != null ? order.getTotalQuantity() : 0;
        viewModel.note = order != null ? order.getNote() : null;
        viewModel.formattedOrderDate = order != null ? formatDate(order.getOrderDate()) : null;
        viewModel.formattedUpdatedDate = order != null ? formatDate(order.getUpdatedDate()) : null;
        viewModel.items = itemViewModels;
        viewModel.errorCode = null;
        viewModel.errorMessage = null;
    }

    private void presentError(OrderDetailOutputData outputData) {
        viewModel.success = false;
        viewModel.hasError = true;
        viewModel.message = "Lỗi khi lấy chi tiết đơn hàng";
        viewModel.orderId = null;
        viewModel.customerId = null;
        viewModel.receiverName = null;
        viewModel.phoneNumber = null;
        viewModel.shippingAddress = null;
        viewModel.orderStatus = null;
        viewModel.statusColor = "GRAY";
        viewModel.formattedTotalAmount = null;
        viewModel.totalItems = 0;
        viewModel.totalQuantity = 0;
        viewModel.note = null;
        viewModel.formattedOrderDate = null;
        viewModel.formattedUpdatedDate = null;
        viewModel.items = new ArrayList<>();
        if (outputData != null) {
            viewModel.errorCode = outputData.getErrorCode();
            viewModel.errorMessage = outputData.getErrorMessage();
        } else {
            viewModel.errorCode = "SYSTEM_ERROR";
            viewModel.errorMessage = "Không có dữ liệu trả về";
        }
    }

    private String formatCurrency(java.math.BigDecimal value) {
        if (value == null) {
            return VND_FORMAT.format(0);
        }
        return VND_FORMAT.format(value);
    }

    private String formatDate(java.time.LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DATE_FORMATTER);
    }

    private String formatStatus(String status) {
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

    private String statusColor(String status) {
        if (status == null) {
            return "GRAY";
        }
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
