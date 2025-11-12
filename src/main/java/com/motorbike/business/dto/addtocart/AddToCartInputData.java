package com.motorbike.business.dto.addtocart;

/**
 * Input DTO for AddToCart Use Case
 * Carries data INTO the use case from the adapter layer
 * Plain data structure - no business logic
 */
public class AddToCartInputData {
    private final Long productId;
    private final int quantity;
    private final Long userId; // null for guest users
    private final Long guestCartId; // for guest sessions

    // Main constructor - all parameters
    public AddToCartInputData(Long productId, int quantity, Long userId, Long guestCartId) {
        this.productId = productId;
        this.quantity = quantity;
        this.userId = userId;
        this.guestCartId = guestCartId;
    }
    
    // Static factory method for logged-in users
    public static AddToCartInputData forLoggedInUser(Long productId, int quantity, Long userId) {
        return new AddToCartInputData(productId, quantity, userId, null);
    }
    
    // Static factory method for guest users
    public static AddToCartInputData forGuestUser(Long productId, int quantity, Long guestCartId) {
        return new AddToCartInputData(productId, quantity, null, guestCartId);
    }

    // Getters
    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getGuestCartId() {
        return guestCartId;
    }

    public boolean isGuestUser() {
        return userId == null;
    }
}
