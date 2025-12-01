package com.motorbike.domain.entities;

import com.motorbike.domain.exceptions.InvalidProductException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public abstract class SanPham {
    protected Long maSanPham;
    protected String tenSanPham;
    protected String moTa;
    protected BigDecimal gia;
    protected String hinhAnh;
    protected int soLuongTonKho;
    protected boolean conHang;
    protected LocalDateTime ngayTao;
    protected LocalDateTime ngayCapNhat;

    protected SanPham(String tenSanPham, String moTa, BigDecimal gia,
                      String hinhAnh, int soLuongTonKho) {
        validateTenSanPham(tenSanPham);
        validateGia(gia);
        validateSoLuongTonKho(soLuongTonKho);
        
        this.tenSanPham = tenSanPham;
        this.moTa = moTa;
        this.gia = gia;
        this.hinhAnh = hinhAnh;
        this.soLuongTonKho = soLuongTonKho;
        this.conHang = true;
        this.ngayTao = LocalDateTime.now();
        this.ngayCapNhat = LocalDateTime.now();
    }

    protected SanPham(Long maSanPham, String tenSanPham, String moTa, BigDecimal gia,
                      String hinhAnh, int soLuongTonKho, boolean conHang,
                      LocalDateTime ngayTao, LocalDateTime ngayCapNhat) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.moTa = moTa;
        this.gia = gia;
        this.hinhAnh = hinhAnh;
        this.soLuongTonKho = soLuongTonKho;
        this.conHang = conHang;
        this.ngayTao = ngayTao;
        this.ngayCapNhat = ngayCapNhat;
    }

    public static void validateTenSanPham(String tenSanPham) {
        if (tenSanPham == null || tenSanPham.trim().isEmpty()) {
            throw new InvalidProductException("EMPTY_NAME", "Tên sản phẩm không được rỗng");
        }
        if (tenSanPham.length() > 255) {
            throw new InvalidProductException("NAME_TOO_LONG", "Tên sản phẩm phải <= 255 ký tự");
        }
    }

    public static void validateGia(BigDecimal gia) {
        if (gia == null) {
            throw new InvalidProductException("NULL_PRICE", "Giá không được null");
        }
        if (gia.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidProductException("INVALID_PRICE", "Giá phải > 0");
        }
    }

    public static void validateSoLuongTonKho(int soLuong) {
        if (soLuong < 0) {
            throw new InvalidProductException("NEGATIVE_STOCK", "Số lượng tồn kho không được âm");
        }
    }

    public boolean coConHang() {return this.conHang && this.soLuongTonKho > 0;}
    public boolean duSoLuong(int soLuongYeuCau) {return this.conHang && this.soLuongTonKho >= soLuongYeuCau;}
    public void capNhatGia(BigDecimal giaMoi) {validateGia(giaMoi); this.gia = giaMoi; this.ngayCapNhat = LocalDateTime.now();}
    public void ngungKinhDoanh() {this.conHang = false; this.ngayCapNhat = LocalDateTime.now();}

    public void giamTonKho(int soLuong) {
        if (soLuong <= 0) {
            throw new InvalidProductException("INVALID_QUANTITY", "Số lượng phải > 0");
        }
        if (this.soLuongTonKho < soLuong) {
            throw new InvalidProductException("INSUFFICIENT_STOCK",
                "Không đủ hàng trong kho (còn: " + this.soLuongTonKho + ", yêu cầu: " + soLuong + ")");
        }
        this.soLuongTonKho -= soLuong;
        this.ngayCapNhat = LocalDateTime.now();
        
        if (this.soLuongTonKho == 0) {
            this.conHang = false;
        }
    }

    public void tangTonKho(int soLuong) {
        if (soLuong <= 0) {
            throw new InvalidProductException("INVALID_QUANTITY", "Số lượng phải > 0");
        }
        this.soLuongTonKho += soLuong;
        this.ngayCapNhat = LocalDateTime.now();
        
        if (this.soLuongTonKho > 0 && !this.conHang) {
            this.conHang = true;
        }
    }

    public void khoiPhucKinhDoanh() {
        if (this.soLuongTonKho > 0) {
            this.conHang = true;
            this.ngayCapNhat = LocalDateTime.now();
        } else {
            throw new InvalidProductException("NO_STOCK_TO_RESTORE",
                "Không thể khôi phục kinh doanh khi không có hàng trong kho");
        }
    }

    public abstract BigDecimal tinhGiaSauKhuyenMai();
    public abstract String layThongTinChiTiet();

    public Long getMaSanPham() {return maSanPham;}
    public String getTenSanPham() {return tenSanPham;}
    public String getMoTa() {return moTa;}
    public BigDecimal getGia() {return gia;}
    public String getHinhAnh() {return hinhAnh;}
    public int getSoLuongTonKho() {return soLuongTonKho;}
    public boolean isConHang() {return conHang;}
    public LocalDateTime getNgayTao() {return ngayTao;}
    public LocalDateTime getNgayCapNhat() {return ngayCapNhat;}
    public void setMaSanPham(Long maSanPham) {this.maSanPham = maSanPham;}
    public void setTenSanPham(String tenSanPham) {validateTenSanPham(tenSanPham); this.tenSanPham = tenSanPham; this.ngayCapNhat = LocalDateTime.now();}
    public void setMoTa(String moTa) {this.moTa = moTa; this.ngayCapNhat = LocalDateTime.now();}
    public void setHinhAnh(String hinhAnh) {this.hinhAnh = hinhAnh; this.ngayCapNhat = LocalDateTime.now();}
}
