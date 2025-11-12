package com.motorbike.domain.entities;

import com.motorbike.domain.exceptions.InvalidOrderException;
import java.math.BigDecimal;

/**
 * ChiTietDonHang (Order Item)
 * Đại diện cho từng sản phẩm trong đơn hàng
 * Business Rule: Lưu snapshot của sản phẩm tại thời điểm đặt hàng
 */
public class ChiTietDonHang {
    private Long maChiTiet; // id
    private Long maDonHang; // order id
    private Long maSanPham; // product id
    private String tenSanPham; // product name (snapshot)
    private BigDecimal giaBan; // selling price (snapshot)
    private int soLuong; // quantity
    private BigDecimal thanhTien; // subtotal (price * quantity)

    // Constructor cho chi tiết đơn hàng mới
    public ChiTietDonHang(Long maSanPham, String tenSanPham, BigDecimal giaBan, int soLuong) {
        validateSoLuong(soLuong);
        validateGia(giaBan);
        
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.giaBan = giaBan;
        this.soLuong = soLuong;
        this.thanhTien = tinhThanhTien();
    }

    // Constructor đầy đủ (reconstruct từ DB)
    public ChiTietDonHang(Long maChiTiet, Long maDonHang, Long maSanPham, 
                          String tenSanPham, BigDecimal giaBan, 
                          int soLuong, BigDecimal thanhTien) {
        this.maChiTiet = maChiTiet;
        this.maDonHang = maDonHang;
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.giaBan = giaBan;
        this.soLuong = soLuong;
        this.thanhTien = thanhTien;
    }

    // Business Rule: Validate số lượng
    private void validateSoLuong(int soLuong) {
        if (soLuong <= 0) {
            throw new InvalidOrderException("INVALID_QUANTITY", 
                "Số lượng phải lớn hơn 0 (hiện tại: " + soLuong + ")");
        }
    }

    // Business Rule: Validate giá
    private void validateGia(BigDecimal gia) {
        if (gia == null || gia.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidOrderException("INVALID_PRICE", 
                "Giá bán phải lớn hơn 0");
        }
    }

    // Business Rule: Tính thành tiền (quantity × price)
    public BigDecimal tinhThanhTien() {
        return this.giaBan.multiply(BigDecimal.valueOf(this.soLuong));
    }

    // Factory method: Tạo từ ChiTietGioHang
    public static ChiTietDonHang fromChiTietGioHang(ChiTietGioHang chiTietGioHang) {
        if (chiTietGioHang == null) {
            throw new InvalidOrderException("NULL_CART_ITEM", 
                "Chi tiết giỏ hàng không được null");
        }
        
        return new ChiTietDonHang(
            chiTietGioHang.getMaSanPham(),
            chiTietGioHang.getTenSanPham(),
            chiTietGioHang.getGiaSanPham(),
            chiTietGioHang.getSoLuong()
        );
    }

    // Getters
    public Long getMaChiTiet() {
        return maChiTiet;
    }

    public Long getMaDonHang() {
        return maDonHang;
    }

    public Long getMaSanPham() {
        return maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public BigDecimal getGiaBan() {
        return giaBan;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public BigDecimal getThanhTien() {
        return thanhTien;
    }

    // Setter cho reconstruct từ DB
    public void setMaChiTiet(Long maChiTiet) {
        this.maChiTiet = maChiTiet;
    }

    public void setMaDonHang(Long maDonHang) {
        this.maDonHang = maDonHang;
    }

    @Override
    public String toString() {
        return "ChiTietDonHang{" +
                "maChiTiet=" + maChiTiet +
                ", maDonHang=" + maDonHang +
                ", maSanPham=" + maSanPham +
                ", tenSanPham='" + tenSanPham + '\'' +
                ", giaBan=" + giaBan +
                ", soLuong=" + soLuong +
                ", thanhTien=" + thanhTien +
                '}';
    }
}
