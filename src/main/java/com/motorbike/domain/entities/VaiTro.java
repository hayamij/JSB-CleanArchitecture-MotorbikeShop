package com.motorbike.domain.entities;

/**
 * VaiTro (User Role)
 * Enum đơn giản cho vai trò người dùng
 */
public enum VaiTro {
    CUSTOMER("Khách hàng"),
    ADMIN("Quản trị viên");

    private final String moTa;

    VaiTro(String moTa) {
        this.moTa = moTa;
    }

    public String getMoTa() {
        return moTa;
    }
}
