package com.motorbike.business.dto.addtocart;

import java.math.BigDecimal;

public class AddToCartOutputData {
    private final boolean success;
    private final String errorCode;
    private final String errorMessage;
    
    private final Long cartId;
    private final int totalItems;
    private final int totalQuantity;
    private final BigDecimal totalAmount;
    
    private final Long productId;
    private final String productName;
    private final int addedQuantity;
    private final int newItemQuantity;
    private final boolean itemAlreadyInCart;
    
    private final BigDecimal productPrice;
    private final int productStock;

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

    public boolean isSuccess() {
        return success;
    }

    public String getErrorCode() {return errorCode;}

    public String getErrorMessage() {return errorMessage;}

    public Long getCartId() {return cartId;}

    public int getTotalItems() {return totalItems;}

    public int getTotalQuantity() {return totalQuantity;}

    public BigDecimal getTotalAmount() {return totalAmount;}

    public Long getProductId() {return productId;}

    public String getProductName() {return productName;}

    public int getAddedQuantity() {return addedQuantity;}

    public int getNewItemQuantity() {return newItemQuantity;}

    public boolean isItemAlreadyInCart() {
        return itemAlreadyInCart;
    }

    public BigDecimal getProductPrice() {return productPrice;}

    public int getProductStock() {return productStock;}
    
    public static AddToCartOutputData forSuccess(Long cartId, int totalItems, BigDecimal totalAmount) {
        return new AddToCartOutputData(cartId, totalItems, totalItems, totalAmount,
                null, null, 0, 0, false, BigDecimal.ZERO, 0);
    }
    
    public static AddToCartOutputData forError(String errorCode, String errorMessage) {
        return new AddToCartOutputData(errorCode, errorMessage);
    }
}
