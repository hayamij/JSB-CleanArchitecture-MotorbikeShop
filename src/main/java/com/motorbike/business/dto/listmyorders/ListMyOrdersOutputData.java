package com.motorbike.business.dto.listmyorders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ListMyOrdersOutputData {
    private final boolean success;
    private final List<OrderItemData> orders;
    private final String errorCode;
    private final String errorMessage;

    public ListMyOrdersOutputData(List<OrderItemData> orders) {
        this.success = true;
        this.orders = orders;
        this.errorCode = null;
        this.errorMessage = null;
    }

    public ListMyOrdersOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.orders = null;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() { return success; }
    public List<OrderItemData> getOrders() { return orders; }
    public String getErrorCode() { return errorCode; }
    public String getErrorMessage() { return errorMessage; }
    public boolean isEmpty() { return orders == null || orders.isEmpty(); }
    
    public static ListMyOrdersOutputData forSuccess(List<OrderItemData> orders) {
        return new ListMyOrdersOutputData(orders);
    }

    public static ListMyOrdersOutputData forError(String errorCode, String errorMessage) {
        return new ListMyOrdersOutputData(errorCode, errorMessage);
    }

    
    public static class OrderItemData {
        private final Long orderId;
        private final Long customerId;
        private final String customerName;
        private final String customerPhone;
        private final String shippingAddress;
        private final String orderStatus;
        private final BigDecimal totalAmount;
        private final int totalItems;
        private final int totalQuantity;
        private final LocalDateTime orderDate;
        private final String note;

        public OrderItemData(Long orderId, Long customerId, String customerName, String customerPhone,
                           String shippingAddress, String orderStatus, BigDecimal totalAmount,
                           int totalItems, int totalQuantity, LocalDateTime orderDate, String note) {
            this.orderId = orderId;
            this.customerId = customerId;
            this.customerName = customerName;
            this.customerPhone = customerPhone;
            this.shippingAddress = shippingAddress;
            this.orderStatus = orderStatus;
            this.totalAmount = totalAmount;
            this.totalItems = totalItems;
            this.totalQuantity = totalQuantity;
            this.orderDate = orderDate;
            this.note = note;
        }

        public Long getOrderId() { return orderId; }
        public Long getCustomerId() { return customerId; }
        public String getCustomerName() { return customerName; }
        public String getCustomerPhone() { return customerPhone; }
        public String getShippingAddress() { return shippingAddress; }
        public String getOrderStatus() { return orderStatus; }
        public BigDecimal getTotalAmount() { return totalAmount; }
        public int getTotalItems() { return totalItems; }
        public int getTotalQuantity() { return totalQuantity; }
        public LocalDateTime getOrderDate() { return orderDate; }
        public String getNote() { return note; }
    }
}
