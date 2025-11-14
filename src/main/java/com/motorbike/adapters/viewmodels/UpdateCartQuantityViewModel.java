package com.motorbike.adapters.viewmodels;

import java.util.List;

/**
 * View Model for Update Cart Quantity use case.
 * Contains formatted data ready for UI display.
 */
public class UpdateCartQuantityViewModel {
    
    private final boolean success;
    private final String message;
    private final String errorCode;
    private final String messageColor;
    
    // Cart and product details
    private final Long cartId;
    private final Long userId;
    private final Long productId;
    private final String productName;
    private final int oldQuantity;
    private final int newQuantity;
    private final boolean itemRemoved;
    
    // Cart totals
    private final int totalItems;
    private final int totalQuantity;
    private final String totalAmount;
    private final String itemSubtotal;
    
    // All cart items
    private final List<CartItemViewModel> allItems;
    
    // Cart summary
    private final String cartSummary;

    // Full constructor
    public UpdateCartQuantityViewModel(
            boolean success,
            String message,
            String errorCode,
            String messageColor,
            Long cartId,
            Long userId,
            Long productId,
            String productName,
            int oldQuantity,
            int newQuantity,
            boolean itemRemoved,
            int totalItems,
            int totalQuantity,
            String totalAmount,
            String itemSubtotal,
            List<CartItemViewModel> allItems,
            String cartSummary) {
        this.success = success;
        this.message = message;
        this.errorCode = errorCode;
        this.messageColor = messageColor;
        this.cartId = cartId;
        this.userId = userId;
        this.productId = productId;
        this.productName = productName;
        this.oldQuantity = oldQuantity;
        this.newQuantity = newQuantity;
        this.itemRemoved = itemRemoved;
        this.totalItems = totalItems;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
        this.itemSubtotal = itemSubtotal;
        this.allItems = allItems;
        this.cartSummary = cartSummary;
    }

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessageColor() {
        return messageColor;
    }

    public Long getCartId() {
        return cartId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getOldQuantity() {
        return oldQuantity;
    }

    public int getNewQuantity() {
        return newQuantity;
    }

    public boolean isItemRemoved() {
        return itemRemoved;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getItemSubtotal() {
        return itemSubtotal;
    }

    public List<CartItemViewModel> getAllItems() {
        return allItems;
    }

    public String getCartSummary() {
        return cartSummary;
    }

    /**
     * Nested ViewModel for individual cart items
     */
    public static class CartItemViewModel {
        private final Long productId;
        private final String productName;
        private final String unitPrice;
        private final int quantity;
        private final String subtotal;

        public CartItemViewModel(
                Long productId,
                String productName,
                String unitPrice,
                int quantity,
                String subtotal) {
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

        public String getUnitPrice() {
            return unitPrice;
        }

        public int getQuantity() {
            return quantity;
        }

        public String getSubtotal() {
            return subtotal;
        }
    }
}
