package com.motorbike.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * JPA Entity for ChiTietGioHang (Cart Item)
 */
@Entity
@Table(name = "chi_tiet_gio_hang")
public class ChiTietGioHangJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_chi_tiet")
    private Long maChiTiet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_gio_hang", nullable = false)
    private GioHangJpaEntity gioHang;

    @Column(name = "ma_san_pham", nullable = false)
    private Long maSanPham;

    @Column(name = "ten_san_pham", length = 255)
    private String tenSanPham;

    @Column(name = "gia_san_pham", nullable = false, precision = 15, scale = 2)
    private BigDecimal giaSanPham;

    @Column(name = "so_luong", nullable = false)
    private int soLuong;

    @Column(name = "tam_tinh", nullable = false, precision = 15, scale = 2)
    private BigDecimal tamTinh;

    // Constructors
    public ChiTietGioHangJpaEntity() {
    }

    public ChiTietGioHangJpaEntity(Long maSanPham, String tenSanPham,
                                   BigDecimal giaSanPham, int soLuong) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.giaSanPham = giaSanPham;
        this.soLuong = soLuong;
        this.tamTinh = giaSanPham.multiply(BigDecimal.valueOf(soLuong));
    }

    // Getters and Setters
    public Long getMaChiTiet() {
        return maChiTiet;
    }

    public void setMaChiTiet(Long maChiTiet) {
        this.maChiTiet = maChiTiet;
    }

    public GioHangJpaEntity getGioHang() {
        return gioHang;
    }

    public void setGioHang(GioHangJpaEntity gioHang) {
        this.gioHang = gioHang;
    }

    public Long getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(Long maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public BigDecimal getGiaSanPham() {
        return giaSanPham;
    }

    public void setGiaSanPham(BigDecimal giaSanPham) {
        this.giaSanPham = giaSanPham;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
        this.tamTinh = this.giaSanPham.multiply(BigDecimal.valueOf(soLuong));
    }

    public BigDecimal getTamTinh() {
        return tamTinh;
    }

    public void setTamTinh(BigDecimal tamTinh) {
        this.tamTinh = tamTinh;
    }
}
