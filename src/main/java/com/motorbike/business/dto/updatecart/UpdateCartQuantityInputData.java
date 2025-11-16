package com.motorbike.business.dto.updatecart;

/**
 * DTO: UpdateCartQuantityInputData
 * Input data for update cart quantity use case
 * Now requires cartId directly for clarity
 */
public class UpdateCartQuantityInputData {
    private final Long cartId;
    private final Long productId;
    private final int newQuantity;

    // Public constructor
    public UpdateCartQuantityInputData(Long cartId, Long productId, int newQuantity) {
        this.cartId = cartId;
        this.productId = productId;
        this.newQuantity = newQuantity;
    }

    public Long getCartId() {
        return cartId;
    }

    public Long getProductId() {
        return productId;
    }

    public int getNewQuantity() {
        return newQuantity;
    }
}
