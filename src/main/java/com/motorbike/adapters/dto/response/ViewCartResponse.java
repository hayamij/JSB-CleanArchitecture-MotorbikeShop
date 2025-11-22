package com.motorbike.adapters.dto.response;

import java.math.BigDecimal;
import java.util.List;

/**
 * Response DTO for view cart operation
 */
public class ViewCartResponse {
    private boolean success;
    private String message;
    private Long cartId;
    private Long userId;
    private int totalItems;
    private int totalQuantity;
    private BigDecimal totalAmount;
    private boolean isEmpty;
    private boolean hasStockWarnings;
    private List<CartItemResponse> items;
    private String errorCode;
    private String errorMessage;

    public ViewCartResponse(boolean success, String message, Long cartId, Long userId,
                           int totalItems, int totalQuantity, BigDecimal totalAmount,
                           boolean isEmpty, boolean hasStockWarnings, List<CartItemResponse> items,
                           String errorCode, String errorMessage) {
        this.success = success;
        this.message = message;
        this.cartId = cartId;
        this.userId = userId;
        this.totalItems = totalItems;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
        this.isEmpty = isEmpty;
        this.hasStockWarnings = hasStockWarnings;
        this.items = items;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Long getCartId() { return cartId; }
    public Long getUserId() { return userId; }
    public int getTotalItems() { return totalItems; }
    public int getTotalQuantity() { return totalQuantity; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public boolean isEmpty() { return isEmpty; }
    public boolean hasStockWarnings() { return hasStockWarnings; }
    public List<CartItemResponse> getItems() { return items; }
    public String getErrorCode() { return errorCode; }
    public String getErrorMessage() { return errorMessage; }

    public static class CartItemResponse {
        private Long productId;
        private String productName;
        private String imageUrl;
        private String description;
        private String category;
        private BigDecimal price;
        private int quantity;
        private int availableStock;
        private boolean hasStockIssue;
        private BigDecimal subtotal;

        public CartItemResponse(Long productId, String productName, BigDecimal price,
                               int quantity, int availableStock, boolean hasStockIssue,
                               BigDecimal subtotal) {
            this.productId = productId;
            this.productName = productName;
            this.price = price;
            this.quantity = quantity;
            this.availableStock = availableStock;
            this.hasStockIssue = hasStockIssue;
            this.subtotal = subtotal;
        }

        public CartItemResponse(Long productId, String productName, String imageUrl, 
                               String description, String category, BigDecimal price,
                               int quantity, int availableStock, boolean hasStockIssue,
                               BigDecimal subtotal) {
            this.productId = productId;
            this.productName = productName;
            this.imageUrl = imageUrl;
            this.description = description;
            this.category = category;
            this.price = price;
            this.quantity = quantity;
            this.availableStock = availableStock;
            this.hasStockIssue = hasStockIssue;
            this.subtotal = subtotal;
        }

        public Long getProductId() { return productId; }
        public String getProductName() { return productName; }
        public String getImageUrl() { return imageUrl; }
        public String getDescription() { return description; }
        public String getCategory() { return category; }
        public BigDecimal getPrice() { return price; }
        public int getQuantity() { return quantity; }
        public int getAvailableStock() { return availableStock; }
        public boolean hasStockIssue() { return hasStockIssue; }
        public BigDecimal getSubtotal() { return subtotal; }
    }
}
