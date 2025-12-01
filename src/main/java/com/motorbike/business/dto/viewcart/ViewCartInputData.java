package com.motorbike.business.dto.viewcart;

public class ViewCartInputData {
    private final Long userId;
    private final Long guestCartId;

    private ViewCartInputData(Long userId, Long guestCartId) {
        this.userId = userId;
        this.guestCartId = guestCartId;
    }

    public static ViewCartInputData forLoggedInUser(Long userId) {
        return new ViewCartInputData(userId, null);
    }

    public static ViewCartInputData forGuestUser(Long guestCartId) {
        return new ViewCartInputData(null, guestCartId);
    }

    public Long getUserId() {return userId;}

    public Long getGuestCartId() {return guestCartId;}

    public boolean isLoggedIn() {
        return userId != null;
    }

    public boolean isGuest() {
        return guestCartId != null;
    }
}
