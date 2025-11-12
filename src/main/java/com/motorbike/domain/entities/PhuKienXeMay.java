package com.motorbike.domain.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * PhuKienXeMay (Motorbike Accessory) - Concrete implementation of SanPham
 * Đại diện cho phụ kiện xe máy trong hệ thống
 */
public class PhuKienXeMay extends SanPham {
    private String loaiPhuKien; // accessory type (mũ bảo hiểm, găng tay, áo mưa, etc.)
    private String thuongHieu; // brand
    private String chatLieu; // material
    private String kichThuoc; // size

    // Constructor cho phụ kiện mới
    public PhuKienXeMay(String tenSanPham, String moTa, BigDecimal gia, 
                        String hinhAnh, int soLuongTonKho,
                        String loaiPhuKien, String thuongHieu, 
                        String chatLieu, String kichThuoc) {
        super(tenSanPham, moTa, gia, hinhAnh, soLuongTonKho);
        this.loaiPhuKien = loaiPhuKien;
        this.thuongHieu = thuongHieu;
        this.chatLieu = chatLieu;
        this.kichThuoc = kichThuoc;
    }

    // Constructor đầy đủ (reconstruct từ DB)
    public PhuKienXeMay(Long maSanPham, String tenSanPham, String moTa, BigDecimal gia,
                        String hinhAnh, int soLuongTonKho, boolean conHang,
                        LocalDateTime ngayTao, LocalDateTime ngayCapNhat,
                        String loaiPhuKien, String thuongHieu, 
                        String chatLieu, String kichThuoc) {
        super(maSanPham, tenSanPham, moTa, gia, hinhAnh, soLuongTonKho, 
              conHang, ngayTao, ngayCapNhat);
        this.loaiPhuKien = loaiPhuKien;
        this.thuongHieu = thuongHieu;
        this.chatLieu = chatLieu;
        this.kichThuoc = kichThuoc;
    }

    @Override
    public BigDecimal tinhGiaSauKhuyenMai() {
        // Business logic: Phụ kiện có khuyến mãi khi mua số lượng lớn
        // Giảm 10% cho phụ kiện có tồn kho > 100 (hàng ế)
        if (this.soLuongTonKho > 100) {
            return this.gia.multiply(BigDecimal.valueOf(0.90));
        }
        return this.gia;
    }

    @Override
    public String layThongTinChiTiet() {
        return String.format(
            "Phụ kiện: %s\n" +
            "Loại: %s\n" +
            "Thương hiệu: %s\n" +
            "Chất liệu: %s\n" +
            "Kích thước: %s\n" +
            "Giá: %s VND\n" +
            "Tồn kho: %d",
            tenSanPham, loaiPhuKien, thuongHieu, chatLieu, kichThuoc,
            gia.toString(), soLuongTonKho
        );
    }

    // Business logic: Kiểm tra phụ kiện an toàn (mũ bảo hiểm, găng tay bảo hộ)
    public boolean laPhuKienAnToan() {
        return this.loaiPhuKien != null && 
               (this.loaiPhuKien.toLowerCase().contains("mũ") || 
                this.loaiPhuKien.toLowerCase().contains("găng tay bảo hộ"));
    }

    // Getters
    public String getLoaiPhuKien() {
        return loaiPhuKien;
    }

    public String getThuongHieu() {
        return thuongHieu;
    }

    public String getChatLieu() {
        return chatLieu;
    }

    public String getKichThuoc() {
        return kichThuoc;
    }

    // Setters
    public void setLoaiPhuKien(String loaiPhuKien) {
        this.loaiPhuKien = loaiPhuKien;
        this.ngayCapNhat = LocalDateTime.now();
    }

    public void setThuongHieu(String thuongHieu) {
        this.thuongHieu = thuongHieu;
        this.ngayCapNhat = LocalDateTime.now();
    }

    public void setChatLieu(String chatLieu) {
        this.chatLieu = chatLieu;
        this.ngayCapNhat = LocalDateTime.now();
    }

    public void setKichThuoc(String kichThuoc) {
        this.kichThuoc = kichThuoc;
        this.ngayCapNhat = LocalDateTime.now();
    }
}
