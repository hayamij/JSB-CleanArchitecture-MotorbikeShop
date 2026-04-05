package com.motorbike.business.dto.removeitemfromcart;

public class RemoveItemFromCartInputData {
    private final Long cartId;
    private final Long productId;

    public RemoveItemFromCartInputData(Long cartId, Long productId) {
        this.cartId = cartId;
        this.productId = productId;
    }

    public Long getCartId() {
        return cartId;
    }

    public Long getProductId() {
        return productId;
    }
}
