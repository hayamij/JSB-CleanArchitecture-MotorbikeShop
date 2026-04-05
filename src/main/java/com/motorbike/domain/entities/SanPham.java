package com.motorbike.domain.entities;

import com.motorbike.domain.exceptions.*;
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
            throw ValidationException.emptyProductName();
        }
        if (tenSanPham.length() > 255) {
            throw ValidationException.productNameTooLong();
        }
    }

    public static void validateGia(BigDecimal gia) {
        if (gia == null) {
            throw ValidationException.nullPrice();
        }
        if (gia.compareTo(BigDecimal.ZERO) <= 0) {
            throw ValidationException.invalidPrice();
        }
    }

    public static void validateSoLuongTonKho(int soLuong) {
        if (soLuong < 0) {
            throw ValidationException.negativeStock();
        }
    }

    public static void checkInput(Long productId, int quantity) {
        if (productId == null) {
            throw ValidationException.nullProductId();
        }
        if (quantity <= 0) {
            throw ValidationException.invalidQuantity();
        }
    }

    public boolean coConHang() {return this.conHang && this.soLuongTonKho > 0;}
    public boolean duSoLuong(int soLuongYeuCau) {return this.conHang && this.soLuongTonKho >= soLuongYeuCau;}
    public void capNhatGia(BigDecimal giaMoi) {validateGia(giaMoi); this.gia = giaMoi; this.ngayCapNhat = LocalDateTime.now();}
    public void ngungKinhDoanh() {this.conHang = false; this.ngayCapNhat = LocalDateTime.now();}

    public void giamTonKho(int soLuong) {
        if (soLuong <= 0) {
            throw ValidationException.invalidQuantity();
        }
        if (this.soLuongTonKho < soLuong) {
            throw DomainException.insufficientStock(this.tenSanPham, this.soLuongTonKho);
        }
        this.soLuongTonKho -= soLuong;
        this.ngayCapNhat = LocalDateTime.now();
        
        if (this.soLuongTonKho == 0) {
            this.conHang = false;
        }
    }

    public void tangTonKho(int soLuong) {
        if (soLuong <= 0) {
            throw ValidationException.invalidQuantity();
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
            throw DomainException.noStockToRestore();
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
    
    // Alias methods for backward compatibility
    public BigDecimal getGiaBan() {return gia;}
    public String getLoaiSanPham() {return this.getClass().getSimpleName();}
    public BigDecimal getPhanTramGiamGia() {return BigDecimal.ZERO;} // Default no discount
    public void setMaSanPham(Long maSanPham) {this.maSanPham = maSanPham;}
    public void setTenSanPham(String tenSanPham) {validateTenSanPham(tenSanPham); this.tenSanPham = tenSanPham; this.ngayCapNhat = LocalDateTime.now();}
    public void setMoTa(String moTa) {this.moTa = moTa; this.ngayCapNhat = LocalDateTime.now();}
    public void setHinhAnh(String hinhAnh) {this.hinhAnh = hinhAnh; this.ngayCapNhat = LocalDateTime.now();}
    public void setSoLuongTonKho(int soLuongTonKho) {
        this.soLuongTonKho = soLuongTonKho;
    }
    
    public void setMaSP(long maSanPham) {
        this.maSanPham = maSanPham;
    }
    
    public Long getMaSP() {
        return maSanPham;
    }
    
    public String getTenSP() {
        return tenSanPham;
    }
    
    /**
     * Factory method for creating test mock objects.
     * Creates XeMay if isPhuKien=false, PhuKienXeMay if isPhuKien=true
     */
    public static SanPham createForTest(String maSP, String ten, String moTa, double gia, 
                                        int soLuong, boolean conHang, boolean isPhuKien) {
        BigDecimal giaDecimal = BigDecimal.valueOf(gia);
        SanPham product;
        
        if (isPhuKien) {
            product = new PhuKienXeMay(ten, moTa, giaDecimal, "default.jpg", soLuong,
                                       "Loại", "Thương hiệu", "Chất liệu", "Size");
        } else {
            product = new XeMay(ten, moTa, giaDecimal, "default.jpg", soLuong,
                               "Yamaha", "Exciter", "Đỏ", 2024, 150);
        }
        
        // Set additional fields
        if (maSP != null && !maSP.isEmpty()) {
            try {
                product.maSanPham = Long.parseLong(maSP.replaceAll("[^0-9]", ""));
            } catch (NumberFormatException e) {
                product.maSanPham = 1L;
            }
        }
        product.conHang = conHang;
        
        return product;
    }
}
