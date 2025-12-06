package com.motorbike.adapters.viewmodels;

import java.util.List;

public class GetOrderDetailViewModel {
    public boolean success;
    public OrderDetailDisplay orderDetail;
    public String errorCode;
    public String errorMessage;

    public static class OrderDetailDisplay {
        public Long orderId;
        public Long customerId;
        public String customerName;
        public String customerPhone;
        public String shippingAddress;
        public String orderStatus;
        public String orderStatusCode;
        public String formattedTotalAmount;
        public String formattedOrderDate;
        public String note;
        public String paymentMethod;
        public String paymentMethodText;
        public List<OrderItemDisplay> items;
        public int totalItems;
    }

    public static class OrderItemDisplay {
        public Long productId;
        public String productName;
        public int quantity;
        public String formattedPrice;
        public String formattedSubtotal;
    }
}
