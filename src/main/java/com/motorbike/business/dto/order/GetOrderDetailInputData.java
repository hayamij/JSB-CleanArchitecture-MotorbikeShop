package com.motorbike.business.dto.order;

public class GetOrderDetailInputData {
    private final Long orderId;

    public GetOrderDetailInputData(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }
}
