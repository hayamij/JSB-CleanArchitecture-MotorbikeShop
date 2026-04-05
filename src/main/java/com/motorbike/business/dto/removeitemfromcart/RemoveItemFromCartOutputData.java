package com.motorbike.business.dto.removeitemfromcart;

import java.math.BigDecimal;

public class RemoveItemFromCartOutputData {
    private final boolean success;
    private final Long cartId;
    private final Long productId;
    private final String productName;
    private final int remainingItems;
    private final BigDecimal newTotal;
    private final String errorCode;
    private final String errorMessage;

    // Success constructor
    public RemoveItemFromCartOutputData(Long cartId, Long productId, String productName,
                                        int remainingItems, BigDecimal newTotal) {
        this.success = true;
        this.cartId = cartId;
        this.productId = productId;
        this.productName = productName;
        this.remainingItems = remainingItems;
        this.newTotal = newTotal;
        this.errorCode = null;
        this.errorMessage = null;
    }

    // Error constructor
    public RemoveItemFromCartOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.cartId = null;
        this.productId = null;
        this.productName = null;
        this.remainingItems = 0;
        this.newTotal = BigDecimal.ZERO;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public Long getCartId() {
        return cartId;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getRemainingItems() {
        return remainingItems;
    }

    public BigDecimal getNewTotal() {
        return newTotal;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public static RemoveItemFromCartOutputData forError(String errorCode, String errorMessage) {
        return new RemoveItemFromCartOutputData(errorCode, errorMessage);
    }
}
