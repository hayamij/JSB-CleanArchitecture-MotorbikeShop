package com.motorbike.adapters.viewmodels;

import java.math.BigDecimal;

public class RemoveItemFromCartViewModel {
    public boolean success;
    public Long cartId;
    public Long productId;
    public String productName;
    public int remainingItems;
    public BigDecimal rawNewTotal;
    public String formattedNewTotal;
    public String message;
    public boolean hasError;
    public String errorCode;
    public String errorMessage;

    public RemoveItemFromCartViewModel() {
        this.success = false;
        this.cartId = null;
        this.productId = null;
        this.productName = null;
        this.remainingItems = 0;
        this.rawNewTotal = BigDecimal.ZERO;
        this.formattedNewTotal = null;
        this.message = null;
        this.hasError = false;
        this.errorCode = null;
        this.errorMessage = null;
    }
}
