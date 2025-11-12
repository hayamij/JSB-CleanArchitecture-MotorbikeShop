package com.motorbike.business.dto.addtocart;

import java.math.BigDecimal;

/**
 * Output DTO for AddToCart Use Case
 * Carries data OUT OF the use case to the presenter
 * Contains raw data before formatting
 */
public class AddToCartOutputData {
    private final boolean success;
    private final String errorCode;
    private final String errorMessage;
    
    // Cart information (only if success)
    private final Long cartId;
    private final int totalItems; // Total number of different products
    private final int totalQuantity; // Total quantity of all items
    private final BigDecimal totalAmount; // Total cart value
    
    // Added item information
    private final Long productId;
    private final String productName;
    private final int addedQuantity;
    private final int newItemQuantity; // Quantity after adding (if item existed)
    private final boolean itemAlreadyInCart; // True if we merged with existing item
    
    // Product information
    private final BigDecimal productPrice;
    private final int productStock;

    // Constructor for success case
    public AddToCartOutputData(Long cartId, int totalItems, int totalQuantity, 
                              BigDecimal totalAmount, Long productId, String productName,
                              int addedQuantity, int newItemQuantity, boolean itemAlreadyInCart,
                              BigDecimal productPrice, int productStock) {
        this.success = true;
        this.errorCode = null;
        this.errorMessage = null;
        this.cartId = cartId;
        this.totalItems = totalItems;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
        this.productId = productId;
        this.productName = productName;
        this.addedQuantity = addedQuantity;
        this.newItemQuantity = newItemQuantity;
        this.itemAlreadyInCart = itemAlreadyInCart;
        this.productPrice = productPrice;
        this.productStock = productStock;
    }

    // Constructor for error case
    public AddToCartOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.cartId = null;
        this.totalItems = 0;
        this.totalQuantity = 0;
        this.totalAmount = BigDecimal.ZERO;
        this.productId = null;
        this.productName = null;
        this.addedQuantity = 0;
        this.newItemQuantity = 0;
        this.itemAlreadyInCart = false;
        this.productPrice = BigDecimal.ZERO;
        this.productStock = 0;
    }

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Long getCartId() {
        return cartId;
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

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getAddedQuantity() {
        return addedQuantity;
    }

    public int getNewItemQuantity() {
        return newItemQuantity;
    }

    public boolean isItemAlreadyInCart() {
        return itemAlreadyInCart;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public int getProductStock() {
        return productStock;
    }
}
