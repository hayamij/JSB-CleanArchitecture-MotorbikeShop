package com.motorbike.adapters.dto.response;

import java.util.List;

public class ListMyOrdersResponse {
    private boolean success;
    private List<OrderItemResponse> orders;
    private String message;
    private String errorCode;
    private String errorMessage;

    public ListMyOrdersResponse(boolean success, List<OrderItemResponse> orders,
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
        private int totalItems;
        private int totalQuantity;
        private String formattedOrderDate;
        private String statusColor;

        public OrderItemResponse(Long orderId, Long customerId, String customerName, String customerPhone,
                               String shippingAddress, String orderStatus, String formattedTotalAmount,
                               int totalItems, int totalQuantity, String formattedOrderDate,
                               String statusColor) {
            this.orderId = orderId;
            this.customerId = customerId;
            this.customerName = customerName;
            this.customerPhone = customerPhone;
            this.shippingAddress = shippingAddress;
            this.orderStatus = orderStatus;
            this.formattedTotalAmount = formattedTotalAmount;
            this.totalItems = totalItems;
            this.totalQuantity = totalQuantity;
            this.formattedOrderDate = formattedOrderDate;
            this.statusColor = statusColor;
        }

        public Long getOrderId() { return orderId; }
        public Long getCustomerId() { return customerId; }
        public String getCustomerName() { return customerName; }
        public String getCustomerPhone() { return customerPhone; }
        public String getShippingAddress() { return shippingAddress; }
        public String getOrderStatus() { return orderStatus; }
        public String getFormattedTotalAmount() { return formattedTotalAmount; }
        public int getTotalItems() { return totalItems; }
        public int getTotalQuantity() { return totalQuantity; }
        public String getFormattedOrderDate() { return formattedOrderDate; }
        public String getStatusColor() { return statusColor; }
    }
}
