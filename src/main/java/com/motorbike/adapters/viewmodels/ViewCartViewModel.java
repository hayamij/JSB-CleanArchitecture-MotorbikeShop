package com.motorbike.adapters.viewmodels;

import java.util.List;

/**
 * ViewModel: ViewCartViewModel
 * UI-ready data for cart display
 */
public class ViewCartViewModel {
    public final boolean success;
    public final Long cartId;
    public final Long userId;
    public final int totalItems;
    public final int totalQuantity;
    public final String formattedTotalAmount;
    public final List<CartItemViewModel> items;
    public final boolean isEmpty;
    public final boolean hasStockWarnings;
    public final String message;
    public final String errorMessage;

    public ViewCartViewModel(boolean success, Long cartId, Long userId,
                           int totalItems, int totalQuantity, String formattedTotalAmount,
                           List<CartItemViewModel> items, boolean isEmpty,
                           boolean hasStockWarnings, String message, String errorMessage) {
        this.success = success;
        this.cartId = cartId;
        this.userId = userId;
        this.totalItems = totalItems;
        this.totalQuantity = totalQuantity;
        this.formattedTotalAmount = formattedTotalAmount;
        this.items = items;
        this.isEmpty = isEmpty;
        this.hasStockWarnings = hasStockWarnings;
        this.message = message;
        this.errorMessage = errorMessage;
    }

    /**
     * Nested ViewModel for cart items
     */
    public static class CartItemViewModel {
        public final Long productId;
        public final String productName;
        public final String productImageUrl;
        public final String formattedUnitPrice;
        public final int quantity;
        public final String formattedSubtotal;
        public final int availableStock;
        public final String stockStatus;
        public final boolean hasStockWarning;
        public final String stockWarningMessage;
        public final String warningColor;

        public CartItemViewModel(Long productId, String productName, String productImageUrl,
                               String formattedUnitPrice, int quantity, String formattedSubtotal,
                               int availableStock, String stockStatus, boolean hasStockWarning,
                               String stockWarningMessage, String warningColor) {
            this.productId = productId;
            this.productName = productName;
            this.productImageUrl = productImageUrl;
            this.formattedUnitPrice = formattedUnitPrice;
            this.quantity = quantity;
            this.formattedSubtotal = formattedSubtotal;
            this.availableStock = availableStock;
            this.stockStatus = stockStatus;
            this.hasStockWarning = hasStockWarning;
            this.stockWarningMessage = stockWarningMessage;
            this.warningColor = warningColor;
        }
    }
}
