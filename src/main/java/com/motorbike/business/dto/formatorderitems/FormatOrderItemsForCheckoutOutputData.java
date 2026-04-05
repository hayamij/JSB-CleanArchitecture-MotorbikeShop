package com.motorbike.business.dto.formatorderitems;

import java.math.BigDecimal;
import java.util.List;

/**
 * UC-82: Format Order Items For Checkout - Output Data
 * Contains formatted order items for checkout response
 */
public class FormatOrderItemsForCheckoutOutputData {
    
    private final boolean success;
    private final String errorCode;
    private final String errorMessage;
    private final List<OrderItemDisplay> formattedItems;
    
    private FormatOrderItemsForCheckoutOutputData(
            boolean success,
            String errorCode,
            String errorMessage,
            List<OrderItemDisplay> formattedItems) {
        this.success = success;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.formattedItems = formattedItems;
    }
    
    public static FormatOrderItemsForCheckoutOutputData forSuccess(List<OrderItemDisplay> formattedItems) {
        return new FormatOrderItemsForCheckoutOutputData(true, null, null, formattedItems);
    }
    
    public static FormatOrderItemsForCheckoutOutputData forError(String errorCode, String errorMessage) {
        return new FormatOrderItemsForCheckoutOutputData(false, errorCode, errorMessage, null);
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
    
    public List<OrderItemDisplay> getFormattedItems() {
        return formattedItems;
    }
    
    /**
     * Represents a single formatted order item
     */
    public static class OrderItemDisplay {
        private final String productId;
        private final String productName;
        private final BigDecimal price;
        private final int quantity;
        private final BigDecimal subtotal;
        
        public OrderItemDisplay(
                String productId,
                String productName,
                BigDecimal price,
                int quantity,
                BigDecimal subtotal) {
            this.productId = productId;
            this.productName = productName;
            this.price = price;
            this.quantity = quantity;
            this.subtotal = subtotal;
        }
        
        public String getProductId() {
            return productId;
        }
        
        public String getProductName() {
            return productName;
        }
        
        public BigDecimal getPrice() {
            return price;
        }
        
        public int getQuantity() {
            return quantity;
        }
        
        public BigDecimal getSubtotal() {
            return subtotal;
        }
    }
}
