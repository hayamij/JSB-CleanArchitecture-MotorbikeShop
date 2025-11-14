package com.motorbike.business.dto.updatecart;

/**
 * DTO: UpdateCartQuantityInputData
 * Input data for update cart quantity use case
 * Supports both logged-in users and guests
 */
public class UpdateCartQuantityInputData {
    private final Long userId;
    private final Long guestCartId;
    private final Long productId;
    private final int newQuantity;

    // Private constructor
    private UpdateCartQuantityInputData(Long userId, Long guestCartId, Long productId, int newQuantity) {
        this.userId = userId;
        this.guestCartId = guestCartId;
        this.productId = productId;
        this.newQuantity = newQuantity;
    }

    // Factory method for logged-in user
    public static UpdateCartQuantityInputData forLoggedInUser(Long userId, Long productId, int newQuantity) {
        return new UpdateCartQuantityInputData(userId, null, productId, newQuantity);
    }

    // Factory method for guest user
    public static UpdateCartQuantityInputData forGuestUser(Long guestCartId, Long productId, int newQuantity) {
        return new UpdateCartQuantityInputData(null, guestCartId, productId, newQuantity);
    }

    public Long getUserId() {
        return userId;
    }

    public Long getGuestCartId() {
        return guestCartId;
    }

    public Long getProductId() {
        return productId;
    }

    public int getNewQuantity() {
        return newQuantity;
    }

    public boolean isLoggedIn() {
        return userId != null;
    }

    public boolean isGuest() {
        return guestCartId != null;
    }
}
