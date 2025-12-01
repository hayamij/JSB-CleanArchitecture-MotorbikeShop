package com.motorbike.business.dto.listallorders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ListAllOrdersOutputData {
    private final boolean success;
    private final List<OrderItemData> orders;
    private final int totalOrders;
    private final int totalPages;
    private final int currentPage;
    private final int pageSize;
    private final BigDecimal totalRevenue;
    private final String errorCode;
    private final String errorMessage;

    public ListAllOrdersOutputData(List<OrderItemData> orders, int totalOrders, int totalPages,
                                   int currentPage, int pageSize, BigDecimal totalRevenue) {
        this.success = true;
        this.orders = orders;
        this.totalOrders = totalOrders;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalRevenue = totalRevenue;
        this.errorCode = null;
        this.errorMessage = null;
    }

    public ListAllOrdersOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.orders = null;
        this.totalOrders = 0;
        this.totalPages = 0;
        this.currentPage = 0;
        this.pageSize = 0;
        this.totalRevenue = BigDecimal.ZERO;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() { return success; }
    public List<OrderItemData> getOrders() { return orders; }
    public int getTotalOrders() { return totalOrders; }
    public int getTotalPages() { return totalPages; }
    public int getCurrentPage() { return currentPage; }
    public int getPageSize() { return pageSize; }
    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public String getErrorCode() { return errorCode; }
    public String getErrorMessage() { return errorMessage; }
    public boolean isEmpty() { return orders == null || orders.isEmpty(); }

    public static ListAllOrdersOutputData forSuccess(List<OrderItemData> orders, int totalOrders,
                                                      int currentPage, int pageSize, BigDecimal totalRevenue) {
        int totalPages = (totalOrders + pageSize - 1) / pageSize;
        return new ListAllOrdersOutputData(orders, totalOrders, totalPages, currentPage, pageSize, totalRevenue);
    }

    public static ListAllOrdersOutputData forError(String errorCode, String errorMessage) {
        return new ListAllOrdersOutputData(errorCode, errorMessage);
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
