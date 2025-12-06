package com.motorbike.business.dto.orderdetail;

/**
 * Input data for viewing a single order detail.
 */
public class OrderDetailInputData {

    private final Long orderId;
    private final Long userId;
    private final boolean admin;

    private OrderDetailInputData(Long orderId, Long userId, boolean admin) {
        this.orderId = orderId;
        this.userId = userId;
        this.admin = admin;
    }

    public static OrderDetailInputData forAdmin(Long orderId) {
        return new OrderDetailInputData(orderId, null, true);
    }

    public static OrderDetailInputData forUser(Long orderId, Long userId) {
        return new OrderDetailInputData(orderId, userId, false);
    }

    public Long getOrderId() { return orderId; }
    public Long getUserId() { return userId; }
    public boolean isAdmin() { return admin; }
}
