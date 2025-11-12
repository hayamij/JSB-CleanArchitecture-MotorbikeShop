package com.motorbike.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA Entity for GioHang (Shopping Cart)
 */
@Entity
@Table(name = "gio_hang")
public class GioHangJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_gio_hang")
    private Long maGioHang;

    @Column(name = "ma_tai_khoan")
    private Long maTaiKhoan;

    @OneToMany(mappedBy = "gioHang", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChiTietGioHangJpaEntity> danhSachSanPham = new ArrayList<>();

    @Column(name = "tong_tien", precision = 15, scale = 2)
    private BigDecimal tongTien;

    @Column(name = "ngay_tao", nullable = false)
    private LocalDateTime ngayTao;

    @Column(name = "ngay_cap_nhat", nullable = false)
    private LocalDateTime ngayCapNhat;

    // Constructors
    public GioHangJpaEntity() {
        this.tongTien = BigDecimal.ZERO;
        this.ngayTao = LocalDateTime.now();
        this.ngayCapNhat = LocalDateTime.now();
    }

    public GioHangJpaEntity(Long maTaiKhoan) {
        this();
        this.maTaiKhoan = maTaiKhoan;
    }

    // Helper method to add item
    public void addItem(ChiTietGioHangJpaEntity item) {
        danhSachSanPham.add(item);
        item.setGioHang(this);
    }

    // Helper method to remove item
    public void removeItem(ChiTietGioHangJpaEntity item) {
        danhSachSanPham.remove(item);
        item.setGioHang(null);
    }

    // Getters and Setters
    public Long getMaGioHang() {
        return maGioHang;
    }

    public void setMaGioHang(Long maGioHang) {
        this.maGioHang = maGioHang;
    }

    public Long getMaTaiKhoan() {
        return maTaiKhoan;
    }

    public void setMaTaiKhoan(Long maTaiKhoan) {
        this.maTaiKhoan = maTaiKhoan;
    }

    public List<ChiTietGioHangJpaEntity> getDanhSachSanPham() {
        return danhSachSanPham;
    }

    public void setDanhSachSanPham(List<ChiTietGioHangJpaEntity> danhSachSanPham) {
        this.danhSachSanPham = danhSachSanPham;
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public void setTongTien(BigDecimal tongTien) {
        this.tongTien = tongTien;
        this.ngayCapNhat = LocalDateTime.now();
    }

    public LocalDateTime getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDateTime ngayTao) {
        this.ngayTao = ngayTao;
    }

    public LocalDateTime getNgayCapNhat() {
        return ngayCapNhat;
    }

    public void setNgayCapNhat(LocalDateTime ngayCapNhat) {
        this.ngayCapNhat = ngayCapNhat;
    }

    @PreUpdate
    protected void onUpdate() {
        this.ngayCapNhat = LocalDateTime.now();
    }
}
