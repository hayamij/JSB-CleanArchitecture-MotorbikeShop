package com.motorbike.business.dto.searchadminorder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class SearchAdminOrderOutputData {
    
    private final boolean success;
    private final List<OrderItemData> orders;
    private final String errorCode;
    private final String errorMessage;
    private final String message;

    private SearchAdminOrderOutputData(boolean success, List<OrderItemData> orders, 
                                      String errorCode, String errorMessage, String message) {
        this.success = success;
        this.orders = orders;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.message = message;
    }

    public static SearchAdminOrderOutputData forSuccess(List<OrderItemData> orders) {
        String message = orders.isEmpty() 
            ? "Không tìm thấy đơn hàng phù hợp" 
            : "Tìm thấy " + orders.size() + " đơn hàng";
        return new SearchAdminOrderOutputData(true, orders, null, null, message);
    }

    public static SearchAdminOrderOutputData forError(String errorCode, String errorMessage) {
        return new SearchAdminOrderOutputData(false, null, errorCode, errorMessage, "Tìm kiếm thất bại");
    }

    public boolean isSuccess() {
        return success;
    }

    public List<OrderItemData> getOrders() {
        return orders;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getMessage() {
        return message;
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

        public OrderItemData(Long orderId, Long customerId, String customerName, 
                           String customerPhone, String shippingAddress, String orderStatus,
                           BigDecimal totalAmount, int totalItems, int totalQuantity, 
                           LocalDateTime orderDate, String note) {
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

        public Long getOrderId() {
            return orderId;
        }

        public Long getCustomerId() {
            return customerId;
        }

        public String getCustomerName() {
            return customerName;
        }

        public String getCustomerPhone() {
            return customerPhone;
        }

        public String getShippingAddress() {
            return shippingAddress;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public BigDecimal getTotalAmount() {
            return totalAmount;
        }

        public int getTotalItems() {
            return totalItems;
        }

        public int getTotalQuantity() {
            return totalQuantity;
        }

        public LocalDateTime getOrderDate() {
            return orderDate;
        }

        public String getNote() {
            return note;
        }
    }
}
