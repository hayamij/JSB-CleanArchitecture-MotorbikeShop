package com.motorbike.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "loai_san_pham", discriminatorType = DiscriminatorType.STRING)
@Table(name = "san_pham")
public abstract class SanPhamJpaEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_san_pham")
    private Long maSanPham;

    @Column(name = "ten_san_pham", nullable = false, length = 255)
    private String tenSanPham;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;

    @Column(name = "gia", nullable = false, precision = 15, scale = 2)
    private BigDecimal gia;

    @Column(name = "hinh_anh", length = 500)
    private String hinhAnh;

    @Column(name = "so_luong_ton_kho", nullable = false)
    private int soLuongTonKho;

    @Column(name = "con_hang", nullable = false)
    private boolean conHang;

    @Column(name = "ngay_tao", nullable = false)
    private LocalDateTime ngayTao;

    @Column(name = "ngay_cap_nhat", nullable = false)
    private LocalDateTime ngayCapNhat;

    @Column(name = "loai_san_pham", nullable = false, length = 50, insertable = false, updatable = false)
    private String loaiSanPham;

    protected SanPhamJpaEntity() {
        this.ngayTao = LocalDateTime.now();
        this.ngayCapNhat = LocalDateTime.now();
        this.conHang = true;
    }

    protected SanPhamJpaEntity(String tenSanPham, String moTa, BigDecimal gia,
                               String hinhAnh, int soLuongTonKho) {
        this();
        this.tenSanPham = tenSanPham;
        this.moTa = moTa;
        this.gia = gia;
        this.hinhAnh = hinhAnh;
        this.soLuongTonKho = soLuongTonKho;
    }

    public Long getMaSanPham() {return maSanPham;}

    public void setMaSanPham(Long maSanPham) {this.maSanPham = maSanPham;}

    public String getTenSanPham() {return tenSanPham;}

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
        this.ngayCapNhat = LocalDateTime.now();
    }

    public String getMoTa() {return moTa;}

    public void setMoTa(String moTa) {
        this.moTa = moTa;
        this.ngayCapNhat = LocalDateTime.now();
    }

    public BigDecimal getGia() {return gia;}

    public void setGia(BigDecimal gia) {
        this.gia = gia;
        this.ngayCapNhat = LocalDateTime.now();
    }

    public String getHinhAnh() {return hinhAnh;}

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
        this.ngayCapNhat = LocalDateTime.now();
    }

    public int getSoLuongTonKho() {return soLuongTonKho;}

    public void setSoLuongTonKho(int soLuongTonKho) {
        this.soLuongTonKho = soLuongTonKho;
        this.ngayCapNhat = LocalDateTime.now();
    }

    public boolean isConHang() {return conHang;}

    public void setConHang(boolean conHang) {
        this.conHang = conHang;
        this.ngayCapNhat = LocalDateTime.now();
    }

    public LocalDateTime getNgayTao() {return ngayTao;}

    public void setNgayTao(LocalDateTime ngayTao) {this.ngayTao = ngayTao;}

    public LocalDateTime getNgayCapNhat() {return ngayCapNhat;}

    public void setNgayCapNhat(LocalDateTime ngayCapNhat) {this.ngayCapNhat = ngayCapNhat;}

    public String getLoaiSanPham() {return loaiSanPham;}

    public void setLoaiSanPham(String loaiSanPham) {
        this.loaiSanPham = loaiSanPham;
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.ngayCapNhat = LocalDateTime.now();
    }
}
