package com.motorbike.business.dto.formatordersforlist;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * UC-81: Format Orders For List - Output Data
 * Contains formatted order items for list display
 */
public class FormatOrdersForListOutputData {
    
    private final boolean success;
    private final String errorCode;
    private final String errorMessage;
    private final List<OrderListItem> orderItems;
    
    private FormatOrdersForListOutputData(
            boolean success,
            String errorCode,
            String errorMessage,
            List<OrderListItem> orderItems) {
        this.success = success;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.orderItems = orderItems;
    }
    
    public static FormatOrdersForListOutputData forSuccess(List<OrderListItem> orderItems) {
        return new FormatOrdersForListOutputData(true, null, null, orderItems);
    }
    
    public static FormatOrdersForListOutputData forError(String errorCode, String errorMessage) {
        return new FormatOrdersForListOutputData(false, errorCode, errorMessage, null);
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public List<OrderListItem> getOrderItems() {
        return orderItems;
    }
    
    /**
     * Represents a single order item in a list format
     */
    public static class OrderListItem {
        private final Long orderId;
        private final String userId;
        private final String receiverName;
        private final String phoneNumber;
        private final String shippingAddress;
        private final String status;
        private final BigDecimal totalAmount;
        private final int productCount;
        private final int totalQuantity;
        private final LocalDateTime orderDate;
        private final String note;
        private final String paymentMethod;
        
        public OrderListItem(
                Long orderId,
                String userId,
                String receiverName,
                String phoneNumber,
                String shippingAddress,
                String status,
                BigDecimal totalAmount,
                int productCount,
                int totalQuantity,
                LocalDateTime orderDate,
                String note,
                String paymentMethod) {
            this.orderId = orderId;
            this.userId = userId;
            this.receiverName = receiverName;
            this.phoneNumber = phoneNumber;
            this.shippingAddress = shippingAddress;
            this.status = status;
            this.totalAmount = totalAmount;
            this.productCount = productCount;
            this.totalQuantity = totalQuantity;
            this.orderDate = orderDate;
            this.note = note;
            this.paymentMethod = paymentMethod;
        }
        
        public Long getOrderId() {
            return orderId;
        }
        
        public String getUserId() {
            return userId;
        }
        
        public String getReceiverName() {
            return receiverName;
        }
        
        public String getPhoneNumber() {
            return phoneNumber;
        }
        
        public String getShippingAddress() {
            return shippingAddress;
        }
        
        public String getStatus() {
            return status;
        }
        
        public BigDecimal getTotalAmount() {
            return totalAmount;
        }
        
        public int getProductCount() {
            return productCount;
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
        
        public String getPaymentMethod() {
            return paymentMethod;
        }
    }
}
