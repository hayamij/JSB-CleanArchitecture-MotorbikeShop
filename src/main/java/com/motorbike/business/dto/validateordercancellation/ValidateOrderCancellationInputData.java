package com.motorbike.business.dto.validateordercancellation;

import com.motorbike.domain.entities.TrangThaiDonHang;

public class ValidateOrderCancellationInputData {
    private final Long orderId;
    private final TrangThaiDonHang currentStatus;
    
    public ValidateOrderCancellationInputData(Long orderId, TrangThaiDonHang currentStatus) {
        this.orderId = orderId;
        this.currentStatus = currentStatus;
    }

    // Constructor with DonHang (for backward compatibility)
    public ValidateOrderCancellationInputData(com.motorbike.domain.entities.DonHang donHang) {
        this.orderId = donHang != null ? donHang.getMaDonHang() : null;
        this.currentStatus = donHang != null ? donHang.getTrangThai() : null;
    }
    
    public Long getOrderId() {
        return orderId;
    }
    
    public TrangThaiDonHang getCurrentStatus() {
        return currentStatus;
    }
}
