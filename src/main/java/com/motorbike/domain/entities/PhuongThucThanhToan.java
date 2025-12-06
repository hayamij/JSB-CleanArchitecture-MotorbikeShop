package com.motorbike.domain.entities;

public enum PhuongThucThanhToan {
    CHUYEN_KHOAN("Chuyển khoản ngân hàng"),
    THANH_TOAN_TRUC_TIEP("Thanh toán khi nhận hàng (COD)");

    private final String moTa;

    PhuongThucThanhToan(String moTa) {
        this.moTa = moTa;
    }

    public String getMoTa() {
        return moTa;
    }
}
