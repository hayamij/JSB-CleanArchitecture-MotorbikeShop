package com.motorbike.adapters.viewmodels;

import java.util.List;

/**
 * ViewModel: ViewCartViewModel
 * UI-ready data for cart display
 * Mutable fields to be populated by Presenter
 */
public class ViewCartViewModel {
    public boolean success;
    public Long cartId;
    public Long userId;
    public int totalItems;
    public int totalQuantity;
    public String formattedTotalAmount;
    public List<CartItemViewModel> items;
    public boolean isEmpty;
    public boolean hasStockWarnings;
    public String message;
    public String errorMessage;

    public ViewCartViewModel() {
        this.success = false;
        this.isEmpty = false;
        this.hasStockWarnings = false;
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
