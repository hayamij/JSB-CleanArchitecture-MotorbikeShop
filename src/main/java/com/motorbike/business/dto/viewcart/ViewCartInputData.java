package com.motorbike.business.dto.viewcart;

/**
 * DTO: ViewCartInputData
 * Input data for view cart use case
 * Supports both logged-in users and guests
 */
public class ViewCartInputData {
    private final Long userId;
    private final Long guestCartId;

    // Private constructor
    private ViewCartInputData(Long userId, Long guestCartId) {
        this.userId = userId;
        this.guestCartId = guestCartId;
    }

    // Factory method for logged-in user
    public static ViewCartInputData forLoggedInUser(Long userId) {
        return new ViewCartInputData(userId, null);
    }

    // Factory method for guest user
    public static ViewCartInputData forGuestUser(Long guestCartId) {
        return new ViewCartInputData(null, guestCartId);
    }

    public Long getUserId() {
        return userId;
    }

    public Long getGuestCartId() {
        return guestCartId;
    }

    public boolean isLoggedIn() {
        return userId != null;
    }

    public boolean isGuest() {
        return guestCartId != null;
    }
}
