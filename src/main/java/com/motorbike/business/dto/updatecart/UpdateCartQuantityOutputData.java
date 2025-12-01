package com.motorbike.business.dto.updatecart;

import java.math.BigDecimal;
import java.util.List;

public class UpdateCartQuantityOutputData {
    private final boolean success;
    private final Long cartId;
    private final Long userId;
    private final Long productId;
    private final String productName;
    private final int oldQuantity;
    private final int newQuantity;
    private final boolean itemRemoved;
    private final int totalItems;
    private final int totalQuantity;
    private final BigDecimal totalAmount;
    private final BigDecimal itemSubtotal;
    private final List<CartItemData> allItems;
    private final String errorCode;
    private final String errorMessage;

    public UpdateCartQuantityOutputData(Long cartId, Long userId, Long productId, String productName,
                                       int oldQuantity, int newQuantity, boolean itemRemoved,
                                       int totalItems, int totalQuantity, BigDecimal totalAmount,
                                       BigDecimal itemSubtotal, List<CartItemData> allItems) {
        this.success = true;
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
        this.errorCode = null;
        this.errorMessage = null;
    }

    public UpdateCartQuantityOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.cartId = null;
        this.userId = null;
        this.productId = null;
        this.productName = null;
        this.oldQuantity = 0;
        this.newQuantity = 0;
        this.itemRemoved = false;
        this.totalItems = 0;
        this.totalQuantity = 0;
        this.totalAmount = BigDecimal.ZERO;
        this.itemSubtotal = BigDecimal.ZERO;
        this.allItems = null;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public Long getCartId() {return cartId;}

    public Long getUserId() {return userId;}

    public Long getProductId() {return productId;}

    public String getProductName() {return productName;}

    public int getOldQuantity() {return oldQuantity;}

    public int getNewQuantity() {return newQuantity;}

    public boolean isItemRemoved() {
        return itemRemoved;
    }

    public int getTotalItems() {return totalItems;}

    public int getTotalQuantity() {return totalQuantity;}

    public BigDecimal getTotalAmount() {return totalAmount;}

    public BigDecimal getItemSubtotal() {return itemSubtotal;}

    public List<CartItemData> getAllItems() {return allItems;}

    public String getErrorCode() {return errorCode;}

    public String getErrorMessage() {return errorMessage;}

    
    public static class CartItemData {
        private final Long productId;
        private final String productName;
        private final BigDecimal unitPrice;
        private final int quantity;
        private final BigDecimal subtotal;

        public CartItemData(Long productId, String productName, BigDecimal unitPrice,
                          int quantity, BigDecimal subtotal) {
            this.productId = productId;
            this.productName = productName;
            this.unitPrice = unitPrice;
            this.quantity = quantity;
            this.subtotal = subtotal;
        }

        public Long getProductId() {return productId;}

        public String getProductName() {return productName;}

        public BigDecimal getUnitPrice() {return unitPrice;}

        public int getQuantity() {return quantity;}

        public BigDecimal getSubtotal() {return subtotal;}
    }
    
    public static UpdateCartQuantityOutputData forSuccess(Long cartId, int totalItems, BigDecimal totalAmount) {
        return new UpdateCartQuantityOutputData(cartId, null, null, null, 0, 0, false,
                totalItems, totalItems, totalAmount, BigDecimal.ZERO, null);
    }
    
    public static UpdateCartQuantityOutputData forError(String errorCode, String errorMessage) {
        return new UpdateCartQuantityOutputData(errorCode, errorMessage);
    }
}
