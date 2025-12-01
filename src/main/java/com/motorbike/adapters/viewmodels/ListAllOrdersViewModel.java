package com.motorbike.adapters.viewmodels;

import java.util.List;

public class ListAllOrdersViewModel {
    public boolean success;
    public boolean hasError;
    public String message;
    public List<OrderItemViewModel> orders;
    public int totalOrders;
    public int totalPages;
    public int currentPage;
    public int pageSize;
    public String formattedTotalRevenue;
    public String errorCode;
    public String errorMessage;

    public ListAllOrdersViewModel() {
        this.success = false;
        this.hasError = false;
    }

    
    public static class OrderItemViewModel {
        public final Long orderId;
        public final Long customerId;
        public final String customerName;
        public final String customerPhone;
        public final String shippingAddress;
        public final String orderStatus;
        public final String formattedTotalAmount;
        public final int totalItems;
        public final int totalQuantity;
        public final String formattedOrderDate;
        public final String statusColor;

        public OrderItemViewModel(Long orderId, Long customerId, String customerName, String customerPhone,
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
    }
}
