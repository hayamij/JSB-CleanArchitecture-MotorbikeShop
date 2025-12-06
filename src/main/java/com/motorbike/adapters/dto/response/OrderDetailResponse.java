package com.motorbike.adapters.dto.response;

import java.util.List;

public class OrderDetailResponse {
    private boolean success;
    private String message;

    private Long orderId;
    private Long customerId;
    private String receiverName;
    private String phoneNumber;
    private String shippingAddress;
    private String orderStatus;
    private String statusColor;
    private String formattedTotalAmount;
    private int totalItems;
    private int totalQuantity;
    private String note;
    private String formattedOrderDate;
    private String formattedUpdatedDate;
    private List<OrderItemResponse> items;

    private String errorCode;
    private String errorMessage;

    public OrderDetailResponse(boolean success, String message,
                               Long orderId, Long customerId, String receiverName,
                               String phoneNumber, String shippingAddress, String orderStatus,
                               String statusColor, String formattedTotalAmount, int totalItems,
                               int totalQuantity, String note, String formattedOrderDate,
                               String formattedUpdatedDate, List<OrderItemResponse> items,
                               String errorCode, String errorMessage) {
        this.success = success;
        this.message = message;
        this.orderId = orderId;
        this.customerId = customerId;
        this.receiverName = receiverName;
        this.phoneNumber = phoneNumber;
        this.shippingAddress = shippingAddress;
        this.orderStatus = orderStatus;
        this.statusColor = statusColor;
        this.formattedTotalAmount = formattedTotalAmount;
        this.totalItems = totalItems;
        this.totalQuantity = totalQuantity;
        this.note = note;
        this.formattedOrderDate = formattedOrderDate;
        this.formattedUpdatedDate = formattedUpdatedDate;
        this.items = items;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Long getOrderId() { return orderId; }
    public Long getCustomerId() { return customerId; }
    public String getReceiverName() { return receiverName; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getShippingAddress() { return shippingAddress; }
    public String getOrderStatus() { return orderStatus; }
    public String getStatusColor() { return statusColor; }
    public String getFormattedTotalAmount() { return formattedTotalAmount; }
    public int getTotalItems() { return totalItems; }
    public int getTotalQuantity() { return totalQuantity; }
    public String getNote() { return note; }
    public String getFormattedOrderDate() { return formattedOrderDate; }
    public String getFormattedUpdatedDate() { return formattedUpdatedDate; }
    public List<OrderItemResponse> getItems() { return items; }
    public String getErrorCode() { return errorCode; }
    public String getErrorMessage() { return errorMessage; }

    public static class OrderItemResponse {
        private Long orderItemId;
        private Long productId;
        private String productName;
        private String formattedUnitPrice;
        private int quantity;
        private String formattedLineTotal;

        public OrderItemResponse(Long orderItemId, Long productId, String productName,
                                 String formattedUnitPrice, int quantity, String formattedLineTotal) {
            this.orderItemId = orderItemId;
            this.productId = productId;
            this.productName = productName;
            this.formattedUnitPrice = formattedUnitPrice;
            this.quantity = quantity;
            this.formattedLineTotal = formattedLineTotal;
        }

        public Long getOrderItemId() { return orderItemId; }
        public Long getProductId() { return productId; }
        public String getProductName() { return productName; }
        public String getFormattedUnitPrice() { return formattedUnitPrice; }
        public int getQuantity() { return quantity; }
        public String getFormattedLineTotal() { return formattedLineTotal; }
    }
}
