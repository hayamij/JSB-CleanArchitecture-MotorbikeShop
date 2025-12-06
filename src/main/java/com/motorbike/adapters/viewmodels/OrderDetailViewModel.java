package com.motorbike.adapters.viewmodels;

import java.util.List;

public class OrderDetailViewModel {
    public boolean success;
    public boolean hasError;
    public String message;

    public Long orderId;
    public Long customerId;
    public String receiverName;
    public String phoneNumber;
    public String shippingAddress;
    public String orderStatus;
    public String statusColor;
    public String formattedTotalAmount;
    public int totalItems;
    public int totalQuantity;
    public String note;
    public String formattedOrderDate;
    public String formattedUpdatedDate;
    public List<OrderItemViewModel> items;

    public String errorCode;
    public String errorMessage;

    public OrderDetailViewModel() {
        this.success = false;
        this.hasError = false;
    }

    public static class OrderItemViewModel {
        public final Long orderItemId;
        public final Long productId;
        public final String productName;
        public final String formattedUnitPrice;
        public final int quantity;
        public final String formattedLineTotal;

        public OrderItemViewModel(Long orderItemId, Long productId, String productName,
                                  String formattedUnitPrice, int quantity, String formattedLineTotal) {
            this.orderItemId = orderItemId;
            this.productId = productId;
            this.productName = productName;
            this.formattedUnitPrice = formattedUnitPrice;
            this.quantity = quantity;
            this.formattedLineTotal = formattedLineTotal;
        }
    }
}
