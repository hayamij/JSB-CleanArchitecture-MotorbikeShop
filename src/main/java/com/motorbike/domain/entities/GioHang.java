package com.motorbike.domain.entities;

import com.motorbike.domain.exceptions.InvalidCartException;
import com.motorbike.domain.exceptions.ProductNotInCartException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GioHang {
    private Long maGioHang;
    private Long maTaiKhoan;
    private List<ChiTietGioHang> danhSachSanPham;
    private BigDecimal tongTien;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;

    public GioHang(Long maTaiKhoan) {
        this.maTaiKhoan = maTaiKhoan;
        this.danhSachSanPham = new ArrayList<>();
        this.tongTien = BigDecimal.ZERO;
        this.ngayTao = LocalDateTime.now();
        this.ngayCapNhat = LocalDateTime.now();
    }

    public GioHang(Long maGioHang, Long maTaiKhoan, List<ChiTietGioHang> danhSachSanPham,
                   BigDecimal tongTien, LocalDateTime ngayTao, LocalDateTime ngayCapNhat) {
        this.maGioHang = maGioHang;
        this.maTaiKhoan = maTaiKhoan;
        this.danhSachSanPham = danhSachSanPham != null ? danhSachSanPham : new ArrayList<>();
        this.tongTien = tongTien;
        this.ngayTao = ngayTao;
        this.ngayCapNhat = ngayCapNhat;
    }

    public void themSanPham(ChiTietGioHang chiTiet) {
        if (chiTiet == null) {
            throw new InvalidCartException("NULL_ITEM", "Chi tiết giỏ hàng không được null");
        }
        Optional<ChiTietGioHang> existing = timSanPhamTheoMa(chiTiet.getMaSanPham());
        if (existing.isPresent()) {
            existing.get().tangSoLuong(chiTiet.getSoLuong());
        } else {
            this.danhSachSanPham.add(chiTiet);
        }
        tinhLaiTongTien();
        this.ngayCapNhat = LocalDateTime.now();
    }

    public void xoaSanPham(Long maSanPham) {
        if (maSanPham == null) {
            throw new InvalidCartException("NULL_PRODUCT_ID", "Mã sản phẩm không được null");
        }
        Optional<ChiTietGioHang> item = timSanPhamTheoMa(maSanPham);
        if (item.isPresent()) {
            this.danhSachSanPham.remove(item.get());
            tinhLaiTongTien();
            this.ngayCapNhat = LocalDateTime.now();
        } else {
            throw new ProductNotInCartException("Không thể xóa sản phẩm: Sản phẩm không có trong giỏ hàng");
        }
    }

    public void capNhatSoLuong(Long maSanPham, int soLuongMoi) {
        if (maSanPham == null) {
            throw new InvalidCartException("NULL_PRODUCT_ID", "Mã sản phẩm không được null");
        }
        if (soLuongMoi < 0) {
            throw new InvalidCartException("INVALID_QUANTITY", "Số lượng không được âm");
        }
        if (soLuongMoi == 0) {
            xoaSanPham(maSanPham);
            return;
        }
        Optional<ChiTietGioHang> item = timSanPhamTheoMa(maSanPham);
        if (item.isPresent()) {
            item.get().datSoLuong(soLuongMoi);
            tinhLaiTongTien();
            this.ngayCapNhat = LocalDateTime.now();
        } else {
            throw new ProductNotInCartException("Không thể cập nhật số lượng: Sản phẩm không có trong giỏ hàng");
        }
    }

    public void xoaToanBoGioHang() {this.danhSachSanPham.clear(); this.tongTien = BigDecimal.ZERO; this.ngayCapNhat = LocalDateTime.now();}
    public boolean coTrong() {return this.danhSachSanPham.isEmpty();}
    public int demSoSanPham() {return this.danhSachSanPham.size();}
    public int tongSoLuong() {return this.danhSachSanPham.stream().mapToInt(ChiTietGioHang::getSoLuong).sum();}
    private Optional<ChiTietGioHang> timSanPhamTheoMa(Long maSanPham) {return this.danhSachSanPham.stream().filter(item -> item.getMaSanPham().equals(maSanPham)).findFirst();}
    private void tinhLaiTongTien() {this.tongTien = this.danhSachSanPham.stream().map(ChiTietGioHang::tinhTamTinh).reduce(BigDecimal.ZERO, BigDecimal::add);}

    public Long getMaGioHang() {return maGioHang;}
    public Long getMaTaiKhoan() {return maTaiKhoan;}
    public List<ChiTietGioHang> getDanhSachSanPham() {return new ArrayList<>(danhSachSanPham);}
    public BigDecimal getTongTien() {return tongTien;}
    public LocalDateTime getNgayTao() {return ngayTao;}
    public LocalDateTime getNgayCapNhat() {return ngayCapNhat;}
    public void setMaGioHang(Long maGioHang) {this.maGioHang = maGioHang;}
    public void setMaTaiKhoan(Long maTaiKhoan) {this.maTaiKhoan = maTaiKhoan;}
}
