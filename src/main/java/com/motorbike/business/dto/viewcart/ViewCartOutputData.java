package com.motorbike.business.dto.viewcart;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO: ViewCartOutputData
 * Output data from view cart use case
 */
public class ViewCartOutputData {
    private final boolean success;
    private final Long cartId;
    private final Long userId;
    private final int totalItems;
    private final int totalQuantity;
    private final BigDecimal totalAmount;
    private final List<CartItemData> items;
    private final boolean isEmpty;
    private final boolean hasStockWarnings;
    private final String errorCode;
    private final String errorMessage;

    // Success constructor
    public ViewCartOutputData(Long cartId, Long userId, int totalItems, int totalQuantity,
                             BigDecimal totalAmount, List<CartItemData> items,
                             boolean isEmpty, boolean hasStockWarnings) {
        this.success = true;
        this.cartId = cartId;
        this.userId = userId;
        this.totalItems = totalItems;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
        this.items = items;
        this.isEmpty = isEmpty;
        this.hasStockWarnings = hasStockWarnings;
        this.errorCode = null;
        this.errorMessage = null;
    }

    // Error constructor
    public ViewCartOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.cartId = null;
        this.userId = null;
        this.totalItems = 0;
        this.totalQuantity = 0;
        this.totalAmount = BigDecimal.ZERO;
        this.items = null;
        this.isEmpty = true;
        this.hasStockWarnings = false;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public Long getCartId() {
        return cartId;
    }

    public Long getUserId() {
        return userId;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public List<CartItemData> getItems() {
        return items;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public boolean hasStockWarnings() {
        return hasStockWarnings;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Nested class for cart item data
     */
    public static class CartItemData {
        private final Long productId;
        private final String productName;
        private final String productImageUrl;
        private final BigDecimal unitPrice;
        private final int quantity;
        private final BigDecimal subtotal;
        private final int availableStock;
        private final boolean hasStockWarning;
        private final String stockWarningMessage;

        public CartItemData(Long productId, String productName, String productImageUrl,
                          BigDecimal unitPrice, int quantity, BigDecimal subtotal,
                          int availableStock, boolean hasStockWarning, String stockWarningMessage) {
            this.productId = productId;
            this.productName = productName;
            this.productImageUrl = productImageUrl;
            this.unitPrice = unitPrice;
            this.quantity = quantity;
            this.subtotal = subtotal;
            this.availableStock = availableStock;
            this.hasStockWarning = hasStockWarning;
            this.stockWarningMessage = stockWarningMessage;
        }

        public Long getProductId() {
            return productId;
        }

        public String getProductName() {
            return productName;
        }

        public String getProductImageUrl() {
            return productImageUrl;
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

        public int getAvailableStock() {
            return availableStock;
        }

        public boolean hasStockWarning() {
            return hasStockWarning;
        }

        public String getStockWarningMessage() {
            return stockWarningMessage;
        }
    }
    
    // Simplified static factory methods for use cases
    public static ViewCartOutputData forSuccess(Long cartId, List<CartItemData> items, BigDecimal totalAmount) {
        int totalItems = (items == null) ? 0 : items.size();
        int totalQuantity = 0;
        if (items != null) {
            for (CartItemData item : items) {
                totalQuantity += (item == null) ? 0 : item.getQuantity();
            }
        }
        return new ViewCartOutputData(cartId, null, totalItems, totalQuantity,
                totalAmount, items, totalItems == 0, false);
    }
    
    public static ViewCartOutputData forEmptyCart() {
        return new ViewCartOutputData(null, null, 0, 0, BigDecimal.ZERO, null, true, false);
    }
    
    public static ViewCartOutputData forError(String errorCode, String errorMessage) {
        return new ViewCartOutputData(errorCode, errorMessage);
    }
}
