package com.motorbike.adapters.viewmodels;

import java.util.List;

/**
 * View Model for Update Cart Quantity use case.
 * Contains formatted data ready for UI display.
 * Mutable fields to be populated by Presenter
 */
public class UpdateCartQuantityViewModel {
    
    public boolean success;
    public String message;
    public String errorCode;
    public String errorMessage;
    public String messageColor;
    
    // Cart and product details
    public Long cartId;
    public Long userId;
    public Long productId;
    public String productName;
    public int oldQuantity;
    public int newQuantity;
    public boolean itemRemoved;
    
    // Cart totals
    public int totalItems;
    public int totalQuantity;
    public String totalAmount;
    public String itemSubtotal;
    
    // All cart items
    public List<CartItemViewModel> allItems;
    
    // Cart summary
    public String cartSummary;

    public UpdateCartQuantityViewModel() {
        this.success = false;
        this.itemRemoved = false;
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
