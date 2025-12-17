package com.motorbike.business.dto.formatcartitems;

import java.math.BigDecimal;
import java.util.List;

/**
 * UC-78: Format Cart Items For Display - Output Data
 * Contains formatted cart items with product info, stock warnings, etc.
 */
public class FormatCartItemsForDisplayOutputData {
    
    private final boolean success;
    private final String errorCode;
    private final String errorMessage;
    private final List<CartItemDisplayData> formattedItems;
    
    private FormatCartItemsForDisplayOutputData(
            boolean success,
            String errorCode,
            String errorMessage,
            List<CartItemDisplayData> formattedItems) {
        this.success = success;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.formattedItems = formattedItems;
    }
    
    public static FormatCartItemsForDisplayOutputData forSuccess(List<CartItemDisplayData> formattedItems) {
        return new FormatCartItemsForDisplayOutputData(true, null, null, formattedItems);
    }
    
    public static FormatCartItemsForDisplayOutputData forError(String errorCode, String errorMessage) {
        return new FormatCartItemsForDisplayOutputData(false, errorCode, errorMessage, null);
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
    
    public List<CartItemDisplayData> getFormattedItems() {
        return formattedItems;
    }
    
    /**
     * Represents a single formatted cart item with all display information
     */
    public static class CartItemDisplayData {
        private final String productId;
        private final String productName;
        private final String imageUrl;
        private final BigDecimal price;
        private final int quantity;
        private final BigDecimal subtotal;
        private final int availableStock;
        private final boolean hasStockWarning;
        private final String stockWarningMessage;
        
        public CartItemDisplayData(
                String productId,
                String productName,
                String imageUrl,
                BigDecimal price,
                int quantity,
                BigDecimal subtotal,
                int availableStock,
                boolean hasStockWarning,
                String stockWarningMessage) {
            this.productId = productId;
            this.productName = productName;
            this.imageUrl = imageUrl;
            this.price = price;
            this.quantity = quantity;
            this.subtotal = subtotal;
            this.availableStock = availableStock;
            this.hasStockWarning = hasStockWarning;
            this.stockWarningMessage = stockWarningMessage;
        }
        
        public String getProductId() {
            return productId;
        }
        
        public String getProductName() {
            return productName;
        }
        
        public String getImageUrl() {
            return imageUrl;
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
        
        public int getAvailableStock() {
            return availableStock;
        }
        
        public boolean isHasStockWarning() {
            return hasStockWarning;
        }
        
        public String getStockWarningMessage() {
            return stockWarningMessage;
        }
    }
}
