package com.motorbike.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * JPA Entity for ChiTietDonHang (Order Item)
 */
@Entity
@Table(name = "chi_tiet_don_hang")
public class ChiTietDonHangJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_chi_tiet")
    private Long maChiTiet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_don_hang", nullable = false)
    private DonHangJpaEntity donHang;

    @Column(name = "ma_san_pham", nullable = false)
    private Long maSanPham;

    @Column(name = "ten_san_pham", length = 255)
    private String tenSanPham;

    @Column(name = "gia_ban", precision = 15, scale = 2, nullable = false)
    private BigDecimal giaBan;

    @Column(name = "so_luong", nullable = false)
    private int soLuong;

    @Column(name = "thanh_tien", precision = 15, scale = 2, nullable = false)
    private BigDecimal thanhTien;

    // Constructors
    public ChiTietDonHangJpaEntity() {
    }

    public ChiTietDonHangJpaEntity(Long maSanPham, String tenSanPham, BigDecimal giaBan, int soLuong) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.giaBan = giaBan;
        this.soLuong = soLuong;
        this.thanhTien = giaBan.multiply(BigDecimal.valueOf(soLuong));
    }

    // Getters and Setters
    public Long getMaChiTiet() {
        return maChiTiet;
    }

    public void setMaChiTiet(Long maChiTiet) {
        this.maChiTiet = maChiTiet;
    }

    public DonHangJpaEntity getDonHang() {
        return donHang;
    }

    public void setDonHang(DonHangJpaEntity donHang) {
        this.donHang = donHang;
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

    public BigDecimal getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(BigDecimal giaBan) {
        this.giaBan = giaBan;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public BigDecimal getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(BigDecimal thanhTien) {
        this.thanhTien = thanhTien;
    }
}
