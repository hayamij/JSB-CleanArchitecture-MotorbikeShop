package com.motorbike.domain.entities;

import com.motorbike.domain.exceptions.InvalidCartException;
import java.math.BigDecimal;

/**
 * ChiTietGioHang (Cart Item)
 * Đại diện cho từng sản phẩm trong giỏ hàng
 */
public class ChiTietGioHang {
    private Long maChiTiet; // id
    private Long maGioHang; // cart id
    private Long maSanPham; // product id
    private String tenSanPham; // product name (denormalized for display)
    private BigDecimal giaSanPham; // product price at time of adding
    private int soLuong; // quantity
    private BigDecimal tamTinh; // subtotal (price * quantity)

    // Constructor cho chi tiết mới
    public ChiTietGioHang(Long maSanPham, String tenSanPham, BigDecimal giaSanPham, int soLuong) {
        validateSoLuong(soLuong);
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.giaSanPham = giaSanPham;
        this.soLuong = soLuong;
        this.tamTinh = tinhTamTinh();
    }

    // Constructor đầy đủ (reconstruct từ DB)
    public ChiTietGioHang(Long maChiTiet, Long maGioHang, Long maSanPham, 
                          String tenSanPham, BigDecimal giaSanPham, 
                          int soLuong, BigDecimal tamTinh) {
        this.maChiTiet = maChiTiet;
        this.maGioHang = maGioHang;
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.giaSanPham = giaSanPham;
        this.soLuong = soLuong;
        this.tamTinh = tamTinh;
    }

    // Validation
    public static void validateSoLuong(int soLuong) {
        if (soLuong <= 0) {
            throw new InvalidCartException("INVALID_QUANTITY", 
                "Số lượng phải > 0 (hiện tại: " + soLuong + ")");
        }
    }

    // Business logic: Tính tạm tính (subtotal)
    public BigDecimal tinhTamTinh() {
        return this.giaSanPham.multiply(BigDecimal.valueOf(this.soLuong));
    }

    // Business logic: Tăng số lượng
    public void tangSoLuong(int themSoLuong) {
        if (themSoLuong <= 0) {
            throw new InvalidCartException("INVALID_QUANTITY", "Số lượng thêm phải > 0");
        }
        this.soLuong += themSoLuong;
        this.tamTinh = tinhTamTinh();
    }

    // Business logic: Giảm số lượng
    public void giamSoLuong(int botSoLuong) {
        if (botSoLuong <= 0) {
            throw new InvalidCartException("INVALID_QUANTITY", "Số lượng bớt phải > 0");
        }
        if (this.soLuong - botSoLuong <= 0) {
            throw new InvalidCartException("QUANTITY_TOO_LOW", 
                "Không thể giảm số lượng xuống <= 0. Hãy xóa sản phẩm thay vì giảm số lượng.");
        }
        this.soLuong -= botSoLuong;
        this.tamTinh = tinhTamTinh();
    }

    // Business logic: Đặt số lượng mới
    public void datSoLuong(int soLuongMoi) {
        // Cho phép số lượng = 0 vì logic xóa sẽ được xử lý ở tầng GioHang
        if (soLuongMoi < 0) {
            throw new InvalidCartException("INVALID_QUANTITY", "Số lượng không được âm");
        }
        this.soLuong = soLuongMoi;
        this.tamTinh = tinhTamTinh();
    }

    // Business logic: Cập nhật giá (khi giá sản phẩm thay đổi)
    public void capNhatGia(BigDecimal giaMoi) {
        if (giaMoi == null || giaMoi.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidCartException("INVALID_PRICE", "Giá phải > 0");
        }
        this.giaSanPham = giaMoi;
        this.tamTinh = tinhTamTinh();
    }

    // Getters
    public Long getMaChiTiet() {
        return maChiTiet;
    }

    public Long getMaGioHang() {
        return maGioHang;
    }

    public Long getMaSanPham() {
        return maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public BigDecimal getGiaSanPham() {
        return giaSanPham;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public BigDecimal getTamTinh() {
        return tamTinh;
    }

    // Setters
    public void setMaChiTiet(Long maChiTiet) {
        this.maChiTiet = maChiTiet;
    }

    public void setMaGioHang(Long maGioHang) {
        this.maGioHang = maGioHang;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }
}
