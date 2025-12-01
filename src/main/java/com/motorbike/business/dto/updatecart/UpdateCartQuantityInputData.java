package com.motorbike.business.dto.updatecart;

public class UpdateCartQuantityInputData {
    private final Long cartId;
    private final Long productId;
    private final int newQuantity;

    public UpdateCartQuantityInputData(Long cartId, Long productId, int newQuantity) {
        this.cartId = cartId;
        this.productId = productId;
        this.newQuantity = newQuantity;
    }

    public Long getCartId() {return cartId;}

    public Long getProductId() {return productId;}

    public int getNewQuantity() {return newQuantity;}
}
