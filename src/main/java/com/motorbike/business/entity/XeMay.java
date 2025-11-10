package com.motorbike.business.entity;

import java.math.BigDecimal;

/**
 * Concrete class đại diện cho Xe Máy
 * Kế thừa từ SanPham và thêm các thuộc tính đặc thù
 */
public class XeMay extends SanPham {
    
    private String thongSoKyThuat;  // Thông số kỹ thuật (Động cơ, công suất, v.v.)
    private String dongXe;          // Dòng xe (Honda Wave, Yamaha Exciter, etc.)
    
    // Constructor
    public XeMay() {
        super();
    }
    
    public XeMay(Long id, String maSanPham, String tenSanPham, String moTa, 
                 BigDecimal rhoTa, String hinhAnh, Integer soLuongTonKho,
                 String thongSoKyThuat, String dongXe) {
        super(id, maSanPham, tenSanPham, moTa, rhoTa, hinhAnh, soLuongTonKho);
        this.thongSoKyThuat = thongSoKyThuat;
        this.dongXe = dongXe;
    }
    
    @Override
    public String getLoaiSanPham() {
        return "XE_MAY";
    }
    
    @Override
    public String getThongTinChiTiet() {
        return String.format("Xe Máy: %s\nDòng xe: %s\nThông số kỹ thuật: %s",
                getTenSanPham(), dongXe, thongSoKyThuat);
    }
    
    @Override
    public boolean kiemTraTuongThich(String dongXe) {
        // Xe máy tương thích với chính nó
        return this.dongXe != null && this.dongXe.equalsIgnoreCase(dongXe);
    }
    
    @Override
    public void validate() {
        super.validate();
        if (thongSoKyThuat == null || thongSoKyThuat.trim().isEmpty()) {
            throw new IllegalArgumentException("Thông số kỹ thuật không được để trống");
        }
        if (dongXe == null || dongXe.trim().isEmpty()) {
            throw new IllegalArgumentException("Dòng xe không được để trống");
        }
    }
    
    // Getters and Setters
    public String getThongSoKyThuat() {
        return thongSoKyThuat;
    }
    
    public void setThongSoKyThuat(String thongSoKyThuat) {
        this.thongSoKyThuat = thongSoKyThuat;
    }
    
    public String getDongXe() {
        return dongXe;
    }
    
    public void setDongXe(String dongXe) {
        this.dongXe = dongXe;
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
        private String thongSoKyThuat;
        private String dongXe;
        
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
        
        public Builder thongSoKyThuat(String thongSoKyThuat) {
            this.thongSoKyThuat = thongSoKyThuat;
            return this;
        }
        
        public Builder dongXe(String dongXe) {
            this.dongXe = dongXe;
            return this;
        }
        
        public XeMay build() {
            return new XeMay(id, maSanPham, tenSanPham, moTa, rhoTa, hinhAnh, 
                           soLuongTonKho, thongSoKyThuat, dongXe);
        }
    }
    
    @Override
    public String toString() {
        return "XeMay{" +
                "id=" + getId() +
                ", tenSanPham='" + getTenSanPham() + '\'' +
                ", dongXe='" + dongXe + '\'' +
                ", rhoTa=" + getRhoTa() +
                ", soLuongTonKho=" + getSoLuongTonKho() +
                '}';
    }
}
