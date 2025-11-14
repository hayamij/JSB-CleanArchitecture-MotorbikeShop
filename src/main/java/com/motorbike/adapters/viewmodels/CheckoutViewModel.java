package com.motorbike.adapters.viewmodels;

import java.util.List;

/**
 * ViewModel: CheckoutViewModel
 * UI-ready data for checkout result display
 */
public class CheckoutViewModel {
    public final boolean success;
    public final Long orderId;
    public final Long customerId;
    public final String customerName;
    public final String customerEmail;
    public final String customerPhone;
    public final String shippingAddress;
    public final String orderStatus;
    public final String formattedTotalAmount;
    public final int totalItems;
    public final int totalQuantity;
    public final List<OrderItemViewModel> items;
    public final String formattedOrderDate;
    public final boolean hasError;
    public final String errorCode;
    public final String message;
    public final String messageColor;

    public CheckoutViewModel(boolean success, Long orderId, Long customerId,
                           String customerName, String customerEmail, String customerPhone,
                           String shippingAddress, String orderStatus, String formattedTotalAmount,
                           int totalItems, int totalQuantity, List<OrderItemViewModel> items,
                           String formattedOrderDate, boolean hasError, String errorCode,
                           String message, String messageColor) {
        this.success = success;
        this.orderId = orderId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.shippingAddress = shippingAddress;
        this.orderStatus = orderStatus;
        this.formattedTotalAmount = formattedTotalAmount;
        this.totalItems = totalItems;
        this.totalQuantity = totalQuantity;
        this.items = items;
        this.formattedOrderDate = formattedOrderDate;
        this.hasError = hasError;
        this.errorCode = errorCode;
        this.message = message;
        this.messageColor = messageColor;
    }

    /**
     * Nested ViewModel for order items
     */
    public static class OrderItemViewModel {
        public final Long productId;
        public final String productName;
        public final String formattedUnitPrice;
        public final int quantity;
        public final String formattedSubtotal;

        public OrderItemViewModel(Long productId, String productName,
                                String formattedUnitPrice, int quantity,
                                String formattedSubtotal) {
            this.productId = productId;
            this.productName = productName;
            this.formattedUnitPrice = formattedUnitPrice;
            this.quantity = quantity;
            this.formattedSubtotal = formattedSubtotal;
        }
    }
}
