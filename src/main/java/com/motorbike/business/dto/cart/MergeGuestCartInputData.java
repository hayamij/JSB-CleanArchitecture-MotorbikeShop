package com.motorbike.business.dto.cart;

public class MergeGuestCartInputData {
    private final Long guestCartId;
    private final Long userCartId;
    
    public MergeGuestCartInputData(Long guestCartId, Long userCartId) {
        this.guestCartId = guestCartId;
        this.userCartId = userCartId;
    }
    
    public Long getGuestCartId() {
        return guestCartId;
    }
    
    public Long getUserCartId() {
        return userCartId;
    }
}
