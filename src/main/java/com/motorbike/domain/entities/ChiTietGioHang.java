package com.motorbike.domain.entities;

import com.motorbike.domain.exceptions.InvalidCartException;
import java.math.BigDecimal;

public class ChiTietGioHang {
    private Long maChiTiet;
    private Long maGioHang;
    private Long maSanPham;
    private String tenSanPham;
    private BigDecimal giaSanPham;
    private int soLuong;
    private BigDecimal tamTinh;

    public ChiTietGioHang(Long maSanPham, String tenSanPham, BigDecimal giaSanPham, int soLuong) {
        validateSoLuong(soLuong);
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.giaSanPham = giaSanPham;
        this.soLuong = soLuong;
        this.tamTinh = tinhTamTinh();
    }

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

    public static void validateSoLuong(int soLuong) {
        if (soLuong <= 0) {
            throw new InvalidCartException("INVALID_QUANTITY",
                "Số lượng phải > 0 (hiện tại: " + soLuong + ")");
        }
    }

    public BigDecimal tinhTamTinh() {
        return this.giaSanPham.multiply(BigDecimal.valueOf(this.soLuong));
    }

    public void tangSoLuong(int themSoLuong) {
        if (themSoLuong <= 0) {
            throw new InvalidCartException("INVALID_QUANTITY", "Số lượng thêm phải > 0");
        }
        this.soLuong += themSoLuong;
        this.tamTinh = tinhTamTinh();
    }

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

    public void datSoLuong(int soLuongMoi) {
        if (soLuongMoi < 0) {
            throw new InvalidCartException("INVALID_QUANTITY", "Số lượng không được âm");
        }
        this.soLuong = soLuongMoi;
        this.tamTinh = tinhTamTinh();
    }

    public void capNhatGia(BigDecimal giaMoi) {
        if (giaMoi == null || giaMoi.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidCartException("INVALID_PRICE", "Giá phải > 0");
        }
        this.giaSanPham = giaMoi;
        this.tamTinh = tinhTamTinh();
    }

    public Long getMaChiTiet() {return maChiTiet;}
    public Long getMaGioHang() {return maGioHang;}
    public Long getMaSanPham() {return maSanPham;}
    public String getTenSanPham() {return tenSanPham;}
    public BigDecimal getGiaSanPham() {return giaSanPham;}
    public int getSoLuong() {return soLuong;}
    public BigDecimal getTamTinh() {return tamTinh;}
    public void setMaChiTiet(Long maChiTiet) {this.maChiTiet = maChiTiet;}
    public void setMaGioHang(Long maGioHang) {this.maGioHang = maGioHang;}
    public void setTenSanPham(String tenSanPham) {this.tenSanPham = tenSanPham;}
}
