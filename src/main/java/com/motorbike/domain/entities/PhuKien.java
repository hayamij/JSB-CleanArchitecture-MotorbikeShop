package com.motorbike.domain.entities;

import java.math.BigDecimal;

/**
 * Domain entity: PhuKien (Accessory)
 * Mở rộng từ SanPham - có thể thêm thuộc tính riêng nếu cần
 */
public class PhuKien extends SanPham {
    private String loai; // loại phụ kiện (ví dụ: mũ bảo hiểm, ốp lườn...)

    public PhuKien(String tenSanPham, String moTa, BigDecimal gia, String hinhAnh, int soLuongTonKho, String loai) {
        super(tenSanPham, moTa, gia, hinhAnh, soLuongTonKho);
        this.loai = loai;
    }

    public PhuKien(Long maSanPham, String tenSanPham, String moTa, BigDecimal gia, String hinhAnh,
            int soLuongTonKho, boolean conHang, java.time.LocalDateTime ngayTao,
            java.time.LocalDateTime ngayCapNhat, String loai) {
        super(maSanPham, tenSanPham, moTa, gia, hinhAnh, soLuongTonKho, conHang, ngayTao, ngayCapNhat);
        this.loai = loai;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
        this.ngayCapNhat = java.time.LocalDateTime.now();
    }

    @Override
    public java.math.BigDecimal tinhGiaSauKhuyenMai() {
        // Mặc định không có khuyến mãi riêng cho phụ kiện
        return this.gia;
    }

    @Override
    public String layThongTinChiTiet() {
        return "Phụ kiện: " + this.tenSanPham + " (Loại: " + this.loai + ") - " + this.moTa;
    }
}
