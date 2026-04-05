package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.CheckoutViewModel;
import com.motorbike.adapters.viewmodels.CheckoutViewModel.OrderItemViewModel;
import com.motorbike.business.dto.checkout.CheckoutOutputData;
import com.motorbike.business.usecase.output.CheckoutOutputBoundary;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CheckoutPresenter implements CheckoutOutputBoundary {
    private final CheckoutViewModel viewModel;
    private static final NumberFormat VND_FORMAT = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public CheckoutPresenter(CheckoutViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(CheckoutOutputData outputData) {
        if (!outputData.isSuccess()) {
            viewModel.success = false;
            viewModel.orderId = null;
            viewModel.customerId = null;
            viewModel.customerName = null;
            viewModel.customerEmail = null;
            viewModel.customerPhone = null;
            viewModel.shippingAddress = null;
            viewModel.orderStatus = null;
            viewModel.formattedTotalAmount = null;
            viewModel.totalItems = 0;
            viewModel.totalQuantity = 0;
            viewModel.items = null;
            viewModel.formattedOrderDate = null;
            viewModel.paymentMethod = null;
            viewModel.paymentMethodDisplay = null;
            viewModel.hasError = true;
            viewModel.errorCode = outputData.getErrorCode();
            viewModel.errorMessage = outputData.getErrorMessage();
            viewModel.message = outputData.getErrorMessage();
            viewModel.messageColor = getErrorColor(outputData.getErrorCode());
            return;
        }

        String formattedTotalAmount = VND_FORMAT.format(outputData.getTotalAmount());
        String formattedOrderDate = outputData.getOrderDate().format(DATE_FORMATTER);
        String orderStatusDisplay = formatOrderStatus(outputData.getOrderStatus());
        
        List<OrderItemViewModel> itemViewModels = new ArrayList<>();
        if (outputData.getItems() != null) {
            for (CheckoutOutputData.OrderItemData item : outputData.getItems()) {
                itemViewModels.add(new OrderItemViewModel(
                    item.getProductId(),
                    item.getProductName(),
                    VND_FORMAT.format(item.getUnitPrice()),
                    item.getQuantity(),
                    VND_FORMAT.format(item.getSubtotal())
                ));
            }
        }

        String successMessage = String.format(
            "Đặt hàng thành công! Mã đơn hàng: #%d. " +
            "Tổng giá trị: %s. " +
            "Gồm %d sản phẩm (%d món). " +
            "Đơn hàng đang được xử lý.",
            outputData.getOrderId(),
            formattedTotalAmount,
            outputData.getTotalItems(),
            outputData.getTotalQuantity()
        );

        viewModel.success = true;
        viewModel.orderId = outputData.getOrderId();
        viewModel.customerId = outputData.getCustomerId();
        viewModel.customerName = outputData.getCustomerName();
        viewModel.customerEmail = outputData.getCustomerEmail();
        viewModel.customerPhone = outputData.getCustomerPhone();
        viewModel.shippingAddress = outputData.getShippingAddress();
        viewModel.orderStatus = orderStatusDisplay;
        viewModel.formattedTotalAmount = formattedTotalAmount;
        viewModel.totalItems = outputData.getTotalItems();
        viewModel.totalQuantity = outputData.getTotalQuantity();
        viewModel.items = itemViewModels;
        viewModel.formattedOrderDate = formattedOrderDate;
        viewModel.paymentMethod = outputData.getPaymentMethod();
        viewModel.paymentMethodDisplay = outputData.getPaymentMethodDisplay();
        viewModel.hasError = false;
        viewModel.errorCode = null;
        viewModel.errorMessage = null;
        viewModel.message = successMessage;
        viewModel.messageColor = "#28a745";
    }

    public CheckoutViewModel getViewModel() {return viewModel;}

    private String formatOrderStatus(String status) {
        switch (status) {
            case "PENDING":
                return "Chờ xử lý";
            case "CONFIRMED":
                return "Đã xác nhận";
            case "PROCESSING":
                return "Đang xử lý";
            case "SHIPPING":
                return "Đang giao hàng";
            case "DELIVERED":
                return "Đã giao hàng";
            case "CANCELLED":
                return "Đã hủy";
            default:
                return status;
        }
    }

    private String getErrorColor(String errorCode) {
        if (errorCode == null) {
            return "#28a745";
        }
        
        switch (errorCode) {
            case "USER_NOT_LOGGED_IN":
            case "USER_NOT_FOUND":
                return "#ffc107";
            case "CART_EMPTY":
                return "#17a2b8";
            case "PRODUCT_NOT_FOUND":
            case "PRODUCT_NOT_AVAILABLE":
            case "INSUFFICIENT_STOCK":
            case "SHIPPING_ADDRESS_REQUIRED":
                return "#dc3545";
            default:
                return "#dc3545";
        }
    }
}
