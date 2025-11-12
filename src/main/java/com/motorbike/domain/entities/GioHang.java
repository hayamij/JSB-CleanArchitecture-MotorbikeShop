package com.motorbike.domain.entities;

import com.motorbike.domain.exceptions.InvalidCartException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * GioHang (Shopping Cart)
 * Chứa logic nghiệp vụ về giỏ hàng
 */
public class GioHang {
    private Long maGioHang; // id
    private Long maTaiKhoan; // user id
    private List<ChiTietGioHang> danhSachSanPham; // cart items
    private BigDecimal tongTien; // total amount
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;

    // Constructor cho giỏ hàng mới
    public GioHang(Long maTaiKhoan) {
        this.maTaiKhoan = maTaiKhoan;
        this.danhSachSanPham = new ArrayList<>();
        this.tongTien = BigDecimal.ZERO;
        this.ngayTao = LocalDateTime.now();
        this.ngayCapNhat = LocalDateTime.now();
    }

    // Constructor đầy đủ (reconstruct từ DB)
    public GioHang(Long maGioHang, Long maTaiKhoan, List<ChiTietGioHang> danhSachSanPham,
                   BigDecimal tongTien, LocalDateTime ngayTao, LocalDateTime ngayCapNhat) {
        this.maGioHang = maGioHang;
        this.maTaiKhoan = maTaiKhoan;
        this.danhSachSanPham = danhSachSanPham != null ? danhSachSanPham : new ArrayList<>();
        this.tongTien = tongTien;
        this.ngayTao = ngayTao;
        this.ngayCapNhat = ngayCapNhat;
    }

    // Business logic: Thêm sản phẩm vào giỏ
    public void themSanPham(ChiTietGioHang chiTiet) {
        if (chiTiet == null) {
            throw new InvalidCartException("NULL_ITEM", "Chi tiết giỏ hàng không được null");
        }

        // Kiểm tra sản phẩm đã có trong giỏ chưa
        Optional<ChiTietGioHang> existing = timSanPhamTheoMa(chiTiet.getMaSanPham());
        if (existing.isPresent()) {
            // Cộng dồn số lượng
            existing.get().tangSoLuong(chiTiet.getSoLuong());
        } else {
            // Thêm mới
            this.danhSachSanPham.add(chiTiet);
        }

        tinhLaiTongTien();
        this.ngayCapNhat = LocalDateTime.now();
    }

    // Business logic: Xóa sản phẩm khỏi giỏ
    public void xoaSanPham(Long maSanPham) {
        if (maSanPham == null) {
            throw new InvalidCartException("NULL_PRODUCT_ID", "Mã sản phẩm không được null");
        }

        Optional<ChiTietGioHang> item = timSanPhamTheoMa(maSanPham);
        if (item.isPresent()) {
            this.danhSachSanPham.remove(item.get());
            tinhLaiTongTien();
            this.ngayCapNhat = LocalDateTime.now();
        }
    }

    // Business logic: Cập nhật số lượng sản phẩm
    public void capNhatSoLuong(Long maSanPham, int soLuongMoi) {
        if (maSanPham == null) {
            throw new InvalidCartException("NULL_PRODUCT_ID", "Mã sản phẩm không được null");
        }

        if (soLuongMoi < 0) {
            throw new InvalidCartException("INVALID_QUANTITY", "Số lượng không được âm");
        }

        // Nếu số lượng = 0, xóa sản phẩm
        if (soLuongMoi == 0) {
            xoaSanPham(maSanPham);
            return;
        }

        Optional<ChiTietGioHang> item = timSanPhamTheoMa(maSanPham);
        if (item.isPresent()) {
            item.get().datSoLuong(soLuongMoi);
            tinhLaiTongTien();
            this.ngayCapNhat = LocalDateTime.now();
        }
    }

    // Business logic: Xóa toàn bộ giỏ hàng
    public void xoaToanBoGioHang() {
        this.danhSachSanPham.clear();
        this.tongTien = BigDecimal.ZERO;
        this.ngayCapNhat = LocalDateTime.now();
    }

    // Business logic: Kiểm tra giỏ hàng có trống không
    public boolean coTrong() {
        return this.danhSachSanPham.isEmpty();
    }

    // Business logic: Đếm số sản phẩm trong giỏ
    public int demSoSanPham() {
        return this.danhSachSanPham.size();
    }

    // Business logic: Tính tổng số lượng tất cả sản phẩm
    public int tongSoLuong() {
        return this.danhSachSanPham.stream()
                .mapToInt(ChiTietGioHang::getSoLuong)
                .sum();
    }

    // Helper: Tìm sản phẩm theo mã
    private Optional<ChiTietGioHang> timSanPhamTheoMa(Long maSanPham) {
        return this.danhSachSanPham.stream()
                .filter(item -> item.getMaSanPham().equals(maSanPham))
                .findFirst();
    }

    // Helper: Tính lại tổng tiền
    private void tinhLaiTongTien() {
        this.tongTien = this.danhSachSanPham.stream()
                .map(ChiTietGioHang::tinhTamTinh)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Getters
    public Long getMaGioHang() {
        return maGioHang;
    }

    public Long getMaTaiKhoan() {
        return maTaiKhoan;
    }

    public List<ChiTietGioHang> getDanhSachSanPham() {
        return new ArrayList<>(danhSachSanPham); // defensive copy
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public LocalDateTime getNgayTao() {
        return ngayTao;
    }

    public LocalDateTime getNgayCapNhat() {
        return ngayCapNhat;
    }

    // Setters
    public void setMaGioHang(Long maGioHang) {
        this.maGioHang = maGioHang;
    }

    public void setMaTaiKhoan(Long maTaiKhoan) {
        this.maTaiKhoan = maTaiKhoan;
    }
}
