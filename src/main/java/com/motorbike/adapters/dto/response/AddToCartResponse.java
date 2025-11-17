package com.motorbike.adapters.dto.response;

import java.math.BigDecimal;

/**
 * Response DTO for add to cart operation
 */
public class AddToCartResponse {
    private boolean success;
    private String message;
    private Long cartId;
    private int totalItems;
    private int totalQuantity;
    private BigDecimal totalAmount;
    private Long productId;
    private String productName;
    private int addedQuantity;
    private int newItemQuantity;
    private boolean itemAlreadyInCart;
    private BigDecimal productPrice;
    private int productStock;
    private String errorCode;
    private String errorMessage;

    public AddToCartResponse(boolean success, String message, Long cartId, int totalItems, 
                            int totalQuantity, BigDecimal totalAmount, Long productId, 
                            String productName, int addedQuantity, int newItemQuantity,
                            boolean itemAlreadyInCart, BigDecimal productPrice, int productStock,
                            String errorCode, String errorMessage) {
        this.success = success;
        this.message = message;
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
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Long getCartId() { return cartId; }
    public int getTotalItems() { return totalItems; }
    public int getTotalQuantity() { return totalQuantity; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public Long getProductId() { return productId; }
    public String getProductName() { return productName; }
    public int getAddedQuantity() { return addedQuantity; }
    public int getNewItemQuantity() { return newItemQuantity; }
    public boolean isItemAlreadyInCart() { return itemAlreadyInCart; }
    public BigDecimal getProductPrice() { return productPrice; }
    public int getProductStock() { return productStock; }
    public String getErrorCode() { return errorCode; }
    public String getErrorMessage() { return errorMessage; }
}
