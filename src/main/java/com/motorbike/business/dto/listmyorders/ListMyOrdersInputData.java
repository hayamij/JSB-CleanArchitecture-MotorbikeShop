package com.motorbike.business.dto.listmyorders;

/**
 * Input data for listing user's own orders.
 */
public class ListMyOrdersInputData {

    private final Long userId;

    private ListMyOrdersInputData(Long userId) {
        this.userId = userId;
    }

    public static ListMyOrdersInputData forUser(Long userId) {
        return new ListMyOrdersInputData(userId);
    }

    public Long getUserId() {
        return userId;
    }
}
