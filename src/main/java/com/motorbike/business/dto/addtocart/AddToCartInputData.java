package com.motorbike.business.dto.addtocart;

public class AddToCartInputData {
    private final Long productId;
    private final int quantity;
    private final Long userId;
    private final Long guestCartId;

    public AddToCartInputData(Long productId, int quantity, Long userId, Long guestCartId) {
        this.productId = productId;
        this.quantity = quantity;
        this.userId = userId;
        this.guestCartId = guestCartId;
    }
    
    public static AddToCartInputData forLoggedInUser(Long productId, int quantity, Long userId) {
        return new AddToCartInputData(productId, quantity, userId, null);
    }
    
    public static AddToCartInputData forGuestUser(Long productId, int quantity, Long guestCartId) {
        return new AddToCartInputData(productId, quantity, null, guestCartId);
    }

    public Long getProductId() {return productId;}

    public int getQuantity() {return quantity;}

    public Long getUserId() {return userId;}

    public Long getGuestCartId() {return guestCartId;}

    public boolean isGuestUser() {
        return userId == null;
    }
}
