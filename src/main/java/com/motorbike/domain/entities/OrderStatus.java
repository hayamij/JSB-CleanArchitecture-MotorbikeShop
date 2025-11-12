package com.motorbike.domain.entities;

/**
 * Enum: OrderStatus
 * Represents the status of an order
 */
public enum OrderStatus {
    PENDING,      // Đơn hàng chờ xử lý
    CONFIRMED,    // Đã xác nhận
    PROCESSING,   // Đang xử lý
    SHIPPING,     // Đang giao hàng
    DELIVERED,    // Đã giao hàng
    CANCELLED     // Đã hủy
}
