package com.motorbike.adapters.viewmodels;

import java.util.List;

/**
 * ViewModel: CheckoutViewModel
 * UI-ready data for checkout result display
 * Mutable fields to be populated by Presenter
 */
public class CheckoutViewModel {
    public boolean success;
    public Long orderId;
    public Long customerId;
    public String customerName;
    public String customerEmail;
    public String customerPhone;
    public String shippingAddress;
    public String orderStatus;
    public String formattedTotalAmount;
    public int totalItems;
    public int totalQuantity;
    public List<OrderItemViewModel> items;
    public String formattedOrderDate;
    public boolean hasError;
    public String errorCode;
    public String message;
    public String messageColor;

    public CheckoutViewModel() {
        this.success = false;
        this.hasError = false;
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
