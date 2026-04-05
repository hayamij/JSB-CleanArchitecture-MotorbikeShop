package com.motorbike.domain.entities;

public enum TrangThaiDonHang {
    CHO_XAC_NHAN("Chờ xác nhận"),
    DA_XAC_NHAN("Đã xác nhận"),
    DANG_GIAO("Đang giao hàng"),
    DANG_GIAO_HANG("Đang giao hàng"), // Alias for DANG_GIAO
    DA_GIAO("Đã giao hàng"),
    DA_GIAO_HANG("Đã giao hàng"), // Alias for DA_GIAO
    DA_HUY("Đã hủy");

    private final String moTa;

    @Override
    public String toString() {return this.moTa;}
    TrangThaiDonHang(String moTa) {this.moTa = moTa;}
    public String getMoTa() {return moTa;}

    public boolean coTheChuyenSang(TrangThaiDonHang trangThaiMoi) {
        switch (this) {
            case CHO_XAC_NHAN: return trangThaiMoi == DA_XAC_NHAN || trangThaiMoi == DA_HUY;
            case DA_XAC_NHAN: return trangThaiMoi == DANG_GIAO || trangThaiMoi == DA_HUY;
            case DANG_GIAO: return trangThaiMoi == DA_GIAO || trangThaiMoi == DA_HUY;
            case DA_GIAO: return false;
            case DA_HUY: return false;
            default: return false;
        }
    }
}
