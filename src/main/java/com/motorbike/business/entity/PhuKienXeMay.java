package com.motorbike.business.entity;

import java.math.BigDecimal;

/**
 * Concrete class đại diện cho Phụ Kiện Xe Máy
 * Kế thừa từ SanPham và thêm các thuộc tính đặc thù
 */
public class PhuKienXeMay extends SanPham {
    
    private String tinhTuongThich;  // Tính tương thích với dòng xe (ví dụ: "Honda Wave, SH Mode")
    private String loaiPhuKien;     // Loại phụ kiện (Mũ bảo hiểm, Gương, Đèn, v.v.)
    
    // Constructor
    public PhuKienXeMay() {
        super();
    }
    
    public PhuKienXeMay(Long id, String maSanPham, String tenSanPham, String moTa, 
                        BigDecimal rhoTa, String hinhAnh, Integer soLuongTonKho,
                        String tinhTuongThich, String loaiPhuKien) {
        super(id, maSanPham, tenSanPham, moTa, rhoTa, hinhAnh, soLuongTonKho);
        this.tinhTuongThich = tinhTuongThich;
        this.loaiPhuKien = loaiPhuKien;
    }
    
    @Override
    public String getLoaiSanPham() {
        return "PHU_KIEN";
    }
    
    @Override
    public String getThongTinChiTiet() {
        return String.format("Phụ Kiện: %s\nLoại: %s\nTương thích với: %s",
                getTenSanPham(), loaiPhuKien, tinhTuongThich);
    }
    
    @Override
    public boolean kiemTraTuongThich(String dongXe) {
        // Kiểm tra xem phụ kiện có tương thích với dòng xe không
        if (tinhTuongThich == null || dongXe == null) {
            return false;
        }
        // Tương thích với tất cả dòng xe
        if (tinhTuongThich.equalsIgnoreCase("Tất cả") || 
            tinhTuongThich.equalsIgnoreCase("Universal")) {
            return true;
        }
        // Kiểm tra tương thích cụ thể
        return tinhTuongThich.toLowerCase().contains(dongXe.toLowerCase());
    }
    
    @Override
    public void validate() {
        super.validate();
        if (tinhTuongThich == null || tinhTuongThich.trim().isEmpty()) {
            throw new IllegalArgumentException("Tính tương thích không được để trống");
        }
        if (loaiPhuKien == null || loaiPhuKien.trim().isEmpty()) {
            throw new IllegalArgumentException("Loại phụ kiện không được để trống");
        }
    }
    
    // Getters and Setters
    public String getTinhTuongThich() {
        return tinhTuongThich;
    }
    
    public void setTinhTuongThich(String tinhTuongThich) {
        this.tinhTuongThich = tinhTuongThich;
    }
    
    public String getLoaiPhuKien() {
        return loaiPhuKien;
    }
    
    public void setLoaiPhuKien(String loaiPhuKien) {
        this.loaiPhuKien = loaiPhuKien;
    }
    
    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private Long id;
        private String maSanPham;
        private String tenSanPham;
        private String moTa;
        private BigDecimal rhoTa;
        private String hinhAnh;
        private Integer soLuongTonKho;
        private String tinhTuongThich;
        private String loaiPhuKien;
        
        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        
        public Builder maSanPham(String maSanPham) {
            this.maSanPham = maSanPham;
            return this;
        }
        
        public Builder tenSanPham(String tenSanPham) {
            this.tenSanPham = tenSanPham;
            return this;
        }
        
        public Builder moTa(String moTa) {
            this.moTa = moTa;
            return this;
        }
        
        public Builder rhoTa(BigDecimal rhoTa) {
            this.rhoTa = rhoTa;
            return this;
        }
        
        public Builder hinhAnh(String hinhAnh) {
            this.hinhAnh = hinhAnh;
            return this;
        }
        
        public Builder soLuongTonKho(Integer soLuongTonKho) {
            this.soLuongTonKho = soLuongTonKho;
            return this;
        }
        
        public Builder tinhTuongThich(String tinhTuongThich) {
            this.tinhTuongThich = tinhTuongThich;
            return this;
        }
        
        public Builder loaiPhuKien(String loaiPhuKien) {
            this.loaiPhuKien = loaiPhuKien;
            return this;
        }
        
        public PhuKienXeMay build() {
            return new PhuKienXeMay(id, maSanPham, tenSanPham, moTa, rhoTa, hinhAnh, 
                                   soLuongTonKho, tinhTuongThich, loaiPhuKien);
        }
    }
    
    @Override
    public String toString() {
        return "PhuKienXeMay{" +
                "id=" + getId() +
                ", tenSanPham='" + getTenSanPham() + '\'' +
                ", loaiPhuKien='" + loaiPhuKien + '\'' +
                ", tinhTuongThich='" + tinhTuongThich + '\'' +
                ", rhoTa=" + getRhoTa() +
                ", soLuongTonKho=" + getSoLuongTonKho() +
                '}';
    }
}
