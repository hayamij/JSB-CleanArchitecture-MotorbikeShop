package com.motorbike.business.dto.orderdetail;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDetailOutputData {
    private final boolean success;
    private final OrderData order;
    private final String errorCode;
    private final String errorMessage;

    private OrderDetailOutputData(OrderData order) {
        this.success = true;
        this.order = order;
        this.errorCode = null;
        this.errorMessage = null;
    }

    private OrderDetailOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.order = null;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static OrderDetailOutputData forSuccess(OrderData order) {
        return new OrderDetailOutputData(order);
    }

    public static OrderDetailOutputData forError(String errorCode, String errorMessage) {
        return new OrderDetailOutputData(errorCode, errorMessage);
    }

    public boolean isSuccess() { return success; }
    public OrderData getOrder() { return order; }
    public String getErrorCode() { return errorCode; }
    public String getErrorMessage() { return errorMessage; }

    public static class OrderData {
        private final Long orderId;
        private final Long customerId;
        private final String receiverName;
        private final String phoneNumber;
        private final String shippingAddress;
        private final String orderStatus;
        private final BigDecimal totalAmount;
        private final int totalItems;
        private final int totalQuantity;
        private final String note;
        private final LocalDateTime orderDate;
        private final LocalDateTime updatedDate;
        private final List<OrderItemData> items;

        public OrderData(Long orderId, Long customerId, String receiverName, String phoneNumber,
                         String shippingAddress, String orderStatus, BigDecimal totalAmount,
                         int totalItems, int totalQuantity, String note, LocalDateTime orderDate,
                         LocalDateTime updatedDate, List<OrderItemData> items) {
            this.orderId = orderId;
            this.customerId = customerId;
            this.receiverName = receiverName;
            this.phoneNumber = phoneNumber;
            this.shippingAddress = shippingAddress;
            this.orderStatus = orderStatus;
            this.totalAmount = totalAmount;
            this.totalItems = totalItems;
            this.totalQuantity = totalQuantity;
            this.note = note;
            this.orderDate = orderDate;
            this.updatedDate = updatedDate;
            this.items = items;
        }

        public Long getOrderId() { return orderId; }
        public Long getCustomerId() { return customerId; }
        public String getReceiverName() { return receiverName; }
        public String getPhoneNumber() { return phoneNumber; }
        public String getShippingAddress() { return shippingAddress; }
        public String getOrderStatus() { return orderStatus; }
        public BigDecimal getTotalAmount() { return totalAmount; }
        public int getTotalItems() { return totalItems; }
        public int getTotalQuantity() { return totalQuantity; }
        public String getNote() { return note; }
        public LocalDateTime getOrderDate() { return orderDate; }
        public LocalDateTime getUpdatedDate() { return updatedDate; }
        public List<OrderItemData> getItems() { return items; }
    }

    public static class OrderItemData {
        private final Long orderItemId;
        private final Long productId;
        private final String productName;
        private final BigDecimal unitPrice;
        private final int quantity;
        private final BigDecimal lineTotal;

        public OrderItemData(Long orderItemId, Long productId, String productName,
                             BigDecimal unitPrice, int quantity, BigDecimal lineTotal) {
            this.orderItemId = orderItemId;
            this.productId = productId;
            this.productName = productName;
            this.unitPrice = unitPrice;
            this.quantity = quantity;
            this.lineTotal = lineTotal;
        }

        public Long getOrderItemId() { return orderItemId; }
        public Long getProductId() { return productId; }
        public String getProductName() { return productName; }
        public BigDecimal getUnitPrice() { return unitPrice; }
        public int getQuantity() { return quantity; }
        public BigDecimal getLineTotal() { return lineTotal; }
    }
}
