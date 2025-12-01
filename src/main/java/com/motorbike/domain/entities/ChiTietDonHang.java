package com.motorbike.domain.entities;

import com.motorbike.domain.exceptions.InvalidOrderException;
import java.math.BigDecimal;

public class ChiTietDonHang {
    private Long maChiTiet;
    private Long maDonHang;
    private Long maSanPham;
    private String tenSanPham;
    private BigDecimal giaBan;
    private int soLuong;
    private BigDecimal thanhTien;

    public ChiTietDonHang(Long maSanPham, String tenSanPham, BigDecimal giaBan, int soLuong) {
        validateSoLuong(soLuong);
        validateGia(giaBan);
        
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.giaBan = giaBan;
        this.soLuong = soLuong;
        this.thanhTien = tinhThanhTien();
    }

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

    private void validateSoLuong(int soLuong) {
        if (soLuong <= 0) {
            throw new InvalidOrderException("INVALID_QUANTITY",
                "Số lượng phải lớn hơn 0 (hiện tại: " + soLuong + ")");
        }
    }

    private void validateGia(BigDecimal gia) {
        if (gia == null || gia.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidOrderException("INVALID_PRICE",
                "Giá bán phải lớn hơn 0");
        }
    }

    public BigDecimal tinhThanhTien() {
        return this.giaBan.multiply(BigDecimal.valueOf(this.soLuong));
    }

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

    public Long getMaChiTiet() {return maChiTiet;}
    public Long getMaDonHang() {return maDonHang;}
    public Long getMaSanPham() {return maSanPham;}
    public String getTenSanPham() {return tenSanPham;}
    public BigDecimal getGiaBan() {return giaBan;}
    public int getSoLuong() {return soLuong;}
    public BigDecimal getThanhTien() {return thanhTien;}
    public void setMaChiTiet(Long maChiTiet) {this.maChiTiet = maChiTiet;}
    public void setMaDonHang(Long maDonHang) {this.maDonHang = maDonHang;}

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
