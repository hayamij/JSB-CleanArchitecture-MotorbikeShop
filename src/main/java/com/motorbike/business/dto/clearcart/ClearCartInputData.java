package com.motorbike.business.dto.clearcart;

public class ClearCartInputData {
    private final Long cartId;

    public ClearCartInputData(Long cartId) {
        this.cartId = cartId;
    }

    public Long getCartId() {
        return cartId;
    }
}
