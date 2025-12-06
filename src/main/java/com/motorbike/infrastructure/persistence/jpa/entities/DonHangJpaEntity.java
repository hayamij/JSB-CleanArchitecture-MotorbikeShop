package com.motorbike.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "don_hang")
public class DonHangJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_don_hang")
    private Long maDonHang;

    @Column(name = "ma_tai_khoan", nullable = false)
    private Long maTaiKhoan;

    @OneToMany(mappedBy = "donHang", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChiTietDonHangJpaEntity> danhSachSanPham = new ArrayList<>();

    @Column(name = "tong_tien", precision = 15, scale = 2, nullable = false)
    private BigDecimal tongTien;

    @Column(name = "trang_thai", nullable = false, length = 20)
    private String trangThai;

    @Column(name = "ten_nguoi_nhan", nullable = false, length = 255)
    private String tenNguoiNhan;

    @Column(name = "so_dien_thoai", nullable = false, length = 20)
    private String soDienThoai;

    @Column(name = "dia_chi_giao_hang", nullable = false, columnDefinition = "TEXT")
    private String diaChiGiaoHang;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;

    @Column(name = "phuong_thuc_thanh_toan", length = 50)
    private String phuongThucThanhToan;

    @Column(name = "ngay_dat", nullable = false)
    private LocalDateTime ngayDat;

    @Column(name = "ngay_cap_nhat", nullable = false)
    private LocalDateTime ngayCapNhat;

    public DonHangJpaEntity() {
        this.tongTien = BigDecimal.ZERO;
        this.ngayDat = LocalDateTime.now();
        this.ngayCapNhat = LocalDateTime.now();
    }

    public void addItem(ChiTietDonHangJpaEntity item) {
        danhSachSanPham.add(item);
        item.setDonHang(this);
    }

    public void removeItem(ChiTietDonHangJpaEntity item) {
        danhSachSanPham.remove(item);
        item.setDonHang(null);
    }

    public Long getMaDonHang() {return maDonHang;}

    public void setMaDonHang(Long maDonHang) {this.maDonHang = maDonHang;}

    public Long getMaTaiKhoan() {return maTaiKhoan;}

    public void setMaTaiKhoan(Long maTaiKhoan) {this.maTaiKhoan = maTaiKhoan;}

    public List<ChiTietDonHangJpaEntity> getDanhSachSanPham() {return danhSachSanPham;}

    public void setDanhSachSanPham(List<ChiTietDonHangJpaEntity> danhSachSanPham) {this.danhSachSanPham = danhSachSanPham;}

    public BigDecimal getTongTien() {return tongTien;}

    public void setTongTien(BigDecimal tongTien) {this.tongTien = tongTien;}

    public String getTrangThai() {return trangThai;}

    public void setTrangThai(String trangThai) {this.trangThai = trangThai;}

    public String getTenNguoiNhan() {return tenNguoiNhan;}

    public void setTenNguoiNhan(String tenNguoiNhan) {this.tenNguoiNhan = tenNguoiNhan;}

    public String getSoDienThoai() {return soDienThoai;}

    public void setSoDienThoai(String soDienThoai) {this.soDienThoai = soDienThoai;}

    public String getDiaChiGiaoHang() {return diaChiGiaoHang;}

    public void setDiaChiGiaoHang(String diaChiGiaoHang) {this.diaChiGiaoHang = diaChiGiaoHang;}

    public String getGhiChu() {return ghiChu;}

    public void setGhiChu(String ghiChu) {this.ghiChu = ghiChu;}

    public LocalDateTime getNgayDat() {return ngayDat;}

    public void setNgayDat(LocalDateTime ngayDat) {this.ngayDat = ngayDat;}

    public LocalDateTime getNgayCapNhat() {return ngayCapNhat;}

    public void setNgayCapNhat(LocalDateTime ngayCapNhat) {this.ngayCapNhat = ngayCapNhat;}

    public String getPhuongThucThanhToan() {return phuongThucThanhToan;}

    public void setPhuongThucThanhToan(String phuongThucThanhToan) {this.phuongThucThanhToan = phuongThucThanhToan;}
}
