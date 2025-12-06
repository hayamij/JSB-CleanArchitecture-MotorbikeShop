package com.motorbike.business.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class GetOrderDetailOutputData {
    private final boolean success;
    private final OrderDetailInfo orderDetail;
    private final String errorCode;
    private final String errorMessage;

    private GetOrderDetailOutputData(boolean success, OrderDetailInfo orderDetail,
                                     String errorCode, String errorMessage) {
        this.success = success;
        this.orderDetail = orderDetail;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static GetOrderDetailOutputData forSuccess(OrderDetailInfo orderDetail) {
        return new GetOrderDetailOutputData(true, orderDetail, null, null);
    }

    public static GetOrderDetailOutputData forError(String errorCode, String errorMessage) {
        return new GetOrderDetailOutputData(false, null, errorCode, errorMessage);
    }

    public boolean isSuccess() {
        return success;
    }

    public OrderDetailInfo getOrderDetail() {
        return orderDetail;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public static class OrderDetailInfo {
        private final Long orderId;
        private final Long customerId;
        private final String customerName;
        private final String customerPhone;
        private final String shippingAddress;
        private final String orderStatus;
        private final String orderStatusCode;
        private final BigDecimal totalAmount;
        private final LocalDateTime orderDate;
        private final String note;
        private final String paymentMethod;
        private final List<OrderItemInfo> items;

        public OrderDetailInfo(Long orderId, Long customerId, String customerName,
                              String customerPhone, String shippingAddress, String orderStatus,
                              String orderStatusCode, BigDecimal totalAmount, LocalDateTime orderDate,
                              String note, String paymentMethod, List<OrderItemInfo> items) {
            this.orderId = orderId;
            this.customerId = customerId;
            this.customerName = customerName;
            this.customerPhone = customerPhone;
            this.shippingAddress = shippingAddress;
            this.orderStatus = orderStatus;
            this.orderStatusCode = orderStatusCode;
            this.totalAmount = totalAmount;
            this.orderDate = orderDate;
            this.note = note;
            this.paymentMethod = paymentMethod;
            this.items = items;
        }

        // Getters
        public Long getOrderId() { return orderId; }
        public Long getCustomerId() { return customerId; }
        public String getCustomerName() { return customerName; }
        public String getCustomerPhone() { return customerPhone; }
        public String getShippingAddress() { return shippingAddress; }
        public String getOrderStatus() { return orderStatus; }
        public String getOrderStatusCode() { return orderStatusCode; }
        public BigDecimal getTotalAmount() { return totalAmount; }
        public LocalDateTime getOrderDate() { return orderDate; }
        public String getNote() { return note; }
        public String getPaymentMethod() { return paymentMethod; }
        public List<OrderItemInfo> getItems() { return items; }
    }

    public static class OrderItemInfo {
        private final Long productId;
        private final String productName;
        private final int quantity;
        private final BigDecimal price;
        private final BigDecimal subtotal;

        public OrderItemInfo(Long productId, String productName, int quantity,
                            BigDecimal price, BigDecimal subtotal) {
            this.productId = productId;
            this.productName = productName;
            this.quantity = quantity;
            this.price = price;
            this.subtotal = subtotal;
        }

        // Getters
        public Long getProductId() { return productId; }
        public String getProductName() { return productName; }
        public int getQuantity() { return quantity; }
        public BigDecimal getPrice() { return price; }
        public BigDecimal getSubtotal() { return subtotal; }
    }
}
