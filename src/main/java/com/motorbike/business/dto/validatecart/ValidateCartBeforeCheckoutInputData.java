package com.motorbike.business.dto.validatecart;

public class ValidateCartBeforeCheckoutInputData {
    private final Long cartId;

    public ValidateCartBeforeCheckoutInputData(Long cartId) {
        this.cartId = cartId;
    }

    public Long getCartId() {
        return cartId;
    }
}
