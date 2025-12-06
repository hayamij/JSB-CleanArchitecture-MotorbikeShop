package com.motorbike.adapters.viewmodels;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class SearchAdminOrderViewModel {
    
    public boolean success;
    public List<OrderItemViewModel> orders;
    public String message;
    public String errorCode;
    public String errorMessage;

    public static class OrderItemViewModel {
        public Long orderId;
        public Long customerId;
        public String customerName;
        public String customerPhone;
        public String shippingAddress;
        public String orderStatus;
        public String formattedTotalAmount;
        public int totalItems;
        public int totalQuantity;
        public String formattedOrderDate;
        public String note;
        public String statusColor;
    }
}
