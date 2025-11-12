package com.motorbike.business.dto.checkout;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO: CheckoutOutputData
 * Output data from checkout use case
 */
public class CheckoutOutputData {
    private final boolean success;
    private final Long orderId;
    private final Long customerId;
    private final String customerName;
    private final String customerEmail;
    private final String customerPhone;
    private final String shippingAddress;
    private final String orderStatus;
    private final BigDecimal totalAmount;
    private final int totalItems;
    private final int totalQuantity;
    private final List<OrderItemData> items;
    private final LocalDateTime orderDate;
    private final String errorCode;
    private final String errorMessage;

    // Success constructor
    public CheckoutOutputData(Long orderId, Long customerId, String customerName, 
                             String customerEmail, String customerPhone, String shippingAddress,
                             String orderStatus, BigDecimal totalAmount, int totalItems, 
                             int totalQuantity, List<OrderItemData> items, LocalDateTime orderDate) {
        this.success = true;
        this.orderId = orderId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.shippingAddress = shippingAddress;
        this.orderStatus = orderStatus;
        this.totalAmount = totalAmount;
        this.totalItems = totalItems;
        this.totalQuantity = totalQuantity;
        this.items = items;
        this.orderDate = orderDate;
        this.errorCode = null;
        this.errorMessage = null;
    }

    // Error constructor
    public CheckoutOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.orderId = null;
        this.customerId = null;
        this.customerName = null;
        this.customerEmail = null;
        this.customerPhone = null;
        this.shippingAddress = null;
        this.orderStatus = null;
        this.totalAmount = null;
        this.totalItems = 0;
        this.totalQuantity = 0;
        this.items = null;
        this.orderDate = null;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    // Getters
    public boolean isSuccess() {
        return success;
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

    public String getCustomerEmail() {
        return customerEmail;
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

    public List<OrderItemData> getItems() {
        return items;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Nested class for order item data
     */
    public static class OrderItemData {
        private final Long productId;
        private final String productName;
        private final BigDecimal unitPrice;
        private final int quantity;
        private final BigDecimal subtotal;

        public OrderItemData(Long productId, String productName, BigDecimal unitPrice, 
                           int quantity, BigDecimal subtotal) {
            this.productId = productId;
            this.productName = productName;
            this.unitPrice = unitPrice;
            this.quantity = quantity;
            this.subtotal = subtotal;
        }

        public Long getProductId() {
            return productId;
        }

        public String getProductName() {
            return productName;
        }

        public BigDecimal getUnitPrice() {
            return unitPrice;
        }

        public int getQuantity() {
            return quantity;
        }

        public BigDecimal getSubtotal() {
            return subtotal;
        }
    }
    
    // Simplified static factory methods for use cases
    public static CheckoutOutputData forSuccess(Long orderId, Long customerId, String customerName,
                                               String customerPhone, String shippingAddress,
                                               String orderStatus, BigDecimal totalAmount, 
                                               int totalItems, List<OrderItemData> items) {
        return new CheckoutOutputData(orderId, customerId, customerName, null, customerPhone, 
                shippingAddress, orderStatus, totalAmount, totalItems, 
                items.stream().mapToInt(OrderItemData::getQuantity).sum(), 
                items, LocalDateTime.now());
    }
    
    public static CheckoutOutputData forError(String errorCode, String errorMessage) {
        return new CheckoutOutputData(errorCode, errorMessage);
    }
}
