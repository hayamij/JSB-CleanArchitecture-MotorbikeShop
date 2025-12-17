package com.motorbike.business.dto.updateorderstatus;

import com.motorbike.domain.entities.TrangThaiDonHang;

public class UpdateOrderStatusInputData {
    private final Long orderId;
    private final TrangThaiDonHang newStatus;
    
    public UpdateOrderStatusInputData(Long orderId, TrangThaiDonHang newStatus) {
        this.orderId = orderId;
        this.newStatus = newStatus;
    }
    
    public Long getOrderId() {
        return orderId;
    }
    
    public TrangThaiDonHang getNewStatus() {
        return newStatus;
    }
}
