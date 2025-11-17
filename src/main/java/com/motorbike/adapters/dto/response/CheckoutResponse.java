package com.motorbike.adapters.dto.response;

import java.math.BigDecimal;
import java.util.List;

/**
 * Response DTO for checkout operation
 */
public class CheckoutResponse {
    private boolean success;
    private String message;
    private Long orderId;
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String shippingAddress;
    private String orderStatus;
    private BigDecimal totalAmount;
    private int totalItems;
    private int totalQuantity;
    private String orderDateDisplay;
    private List<OrderItemResponse> items;
    private String errorCode;
    private String errorMessage;

    public CheckoutResponse(boolean success, String message, Long orderId, Long customerId,
                           String customerName, String customerEmail, String customerPhone,
                           String shippingAddress, String orderStatus, BigDecimal totalAmount,
                           int totalItems, int totalQuantity, String orderDateDisplay,
                           List<OrderItemResponse> items, String errorCode, String errorMessage) {
        this.success = success;
        this.message = message;
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
        this.orderDateDisplay = orderDateDisplay;
        this.items = items;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Long getOrderId() { return orderId; }
    public Long getCustomerId() { return customerId; }
    public String getCustomerName() { return customerName; }
    public String getCustomerEmail() { return customerEmail; }
    public String getCustomerPhone() { return customerPhone; }
    public String getShippingAddress() { return shippingAddress; }
    public String getOrderStatus() { return orderStatus; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public int getTotalItems() { return totalItems; }
    public int getTotalQuantity() { return totalQuantity; }
    public String getOrderDateDisplay() { return orderDateDisplay; }
    public List<OrderItemResponse> getItems() { return items; }
    public String getErrorCode() { return errorCode; }
    public String getErrorMessage() { return errorMessage; }

    public static class OrderItemResponse {
        private Long productId;
        private String productName;
        private BigDecimal price;
        private int quantity;
        private BigDecimal subtotal;

        public OrderItemResponse(Long productId, String productName, BigDecimal price, 
                                int quantity, BigDecimal subtotal) {
            this.productId = productId;
            this.productName = productName;
            this.price = price;
            this.quantity = quantity;
            this.subtotal = subtotal;
        }

        public Long getProductId() { return productId; }
        public String getProductName() { return productName; }
        public BigDecimal getPrice() { return price; }
        public int getQuantity() { return quantity; }
        public BigDecimal getSubtotal() { return subtotal; }
    }
}
