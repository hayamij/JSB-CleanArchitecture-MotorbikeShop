package com.motorbike.adapters.dto.response;

import java.util.List;

public class ListAllOrdersResponse {
    private boolean success;
    private List<OrderItemResponse> orders;
    private String message;
    private String errorCode;
    private String errorMessage;
    public ListAllOrdersResponse(boolean success, List<OrderItemResponse> orders,
                                String message,
                                String errorCode, String errorMessage) {
        this.success = success;
        this.orders = orders;
        this.message = message;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() { return success; }
    public List<OrderItemResponse> getOrders() { return orders; }
    public String getMessage() { return message; }
    public String getErrorCode() { return errorCode; }
    public String getErrorMessage() { return errorMessage; }

    
    public static class OrderItemResponse {
        private Long orderId;
        private Long customerId;
        private String customerName;
        private String customerPhone;
        private String shippingAddress;
        private String orderStatus;
        private String formattedTotalAmount;
        private java.math.BigDecimal totalAmount;
        private int totalItems;
        private int totalQuantity;
        private String formattedOrderDate;
        private String orderDate;
        private String statusColor;
        private String paymentMethodText;

        public OrderItemResponse(Long orderId, Long customerId, String customerName, String customerPhone,
                               String shippingAddress, String orderStatus, String formattedTotalAmount,
                               java.math.BigDecimal totalAmount, int totalItems, int totalQuantity, 
                               String formattedOrderDate, String orderDate,
                               String statusColor, String paymentMethodText) {
            this.orderId = orderId;
            this.customerId = customerId;
            this.customerName = customerName;
            this.customerPhone = customerPhone;
            this.shippingAddress = shippingAddress;
            this.orderStatus = orderStatus;
            this.paymentMethodText = paymentMethodText;
            this.formattedTotalAmount = formattedTotalAmount;
            this.totalAmount = totalAmount;
            this.totalItems = totalItems;
            this.totalQuantity = totalQuantity;
            this.formattedOrderDate = formattedOrderDate;
            this.orderDate = orderDate;
            this.statusColor = statusColor;
        }

        public Long getOrderId() { return orderId; }
        public Long getCustomerId() { return customerId; }
        public String getCustomerName() { return customerName; }
        public String getCustomerPhone() { return customerPhone; }
        public String getShippingAddress() { return shippingAddress; }
        public String getOrderStatus() { return orderStatus; }
        public String getFormattedTotalAmount() { return formattedTotalAmount; }
        public java.math.BigDecimal getTotalAmount() { return totalAmount; }
        public int getTotalItems() { return totalItems; }
        public int getTotalQuantity() { return totalQuantity; }
        public String getFormattedOrderDate() { return formattedOrderDate; }
        public String getOrderDate() { return orderDate; }
        public String getStatusColor() { return statusColor; }
        public String getPaymentMethodText() { return paymentMethodText; }
    }
}
