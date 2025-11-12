package com.motorbike.adapters.presenters;

import com.motorbike.adapters.viewmodels.CheckoutViewModel;
import com.motorbike.adapters.viewmodels.CheckoutViewModel.OrderItemViewModel;
import com.motorbike.business.dto.checkout.CheckoutOutputData;
import com.motorbike.business.usecase.CheckoutOutputBoundary;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Presenter: CheckoutPresenter
 * Converts CheckoutOutputData to CheckoutViewModel
 * Formats data for UI presentation
 */
public class CheckoutPresenter implements CheckoutOutputBoundary {
    private CheckoutViewModel viewModel;
    private static final NumberFormat VND_FORMAT = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public void present(CheckoutOutputData outputData) {
        if (!outputData.isSuccess()) {
            // Error case
            viewModel = new CheckoutViewModel(
                false,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                0,
                0,
                null,
                null,
                true,
                outputData.getErrorCode(),
                outputData.getErrorMessage(),
                getErrorColor(outputData.getErrorCode())
            );
            return;
        }

        // Success case - format all data
        String formattedTotalAmount = VND_FORMAT.format(outputData.getTotalAmount());
        String formattedOrderDate = outputData.getOrderDate().format(DATE_FORMATTER);
        String orderStatusDisplay = formatOrderStatus(outputData.getOrderStatus());
        
        // Format order items
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

        // Build success message
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

        viewModel = new CheckoutViewModel(
            true,
            outputData.getOrderId(),
            outputData.getCustomerId(),
            outputData.getCustomerName(),
            outputData.getCustomerEmail(),
            outputData.getCustomerPhone(),
            outputData.getShippingAddress(),
            orderStatusDisplay,
            formattedTotalAmount,
            outputData.getTotalItems(),
            outputData.getTotalQuantity(),
            itemViewModels,
            formattedOrderDate,
            false,
            null,
            successMessage,
            "#28a745" // Green color for success
        );
    }

    public CheckoutViewModel getViewModel() {
        return viewModel;
    }

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
            return "#28a745"; // Green for success
        }
        
        switch (errorCode) {
            case "USER_NOT_LOGGED_IN":
            case "USER_NOT_FOUND":
                return "#ffc107"; // Warning yellow
            case "CART_EMPTY":
                return "#17a2b8"; // Info blue
            case "PRODUCT_NOT_FOUND":
            case "PRODUCT_NOT_AVAILABLE":
            case "INSUFFICIENT_STOCK":
            case "SHIPPING_ADDRESS_REQUIRED":
                return "#dc3545"; // Danger red
            default:
                return "#dc3545"; // Default danger
        }
    }
}
