package com.motorbike.business.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Abstract base class cho tất cả các loại sản phẩm
 * Áp dụng Open/Closed Principle - mở cho mở rộng, đóng cho sửa đổi
 */
public abstract class SanPham {
    
    // Thuộc tính chung cho tất cả sản phẩm
    private Long id;
    private String maSanPham;        // Mã sản phẩm duy nhất
    private String tenSanPham;       // Tên sản phẩm
    private String moTa;             // Mô tả
    private BigDecimal rhoTa;        // Giá tiền
    private String hinhAnh;          // URL hình ảnh
    private Integer soLuongTonKho;   // Số lượng tồn kho
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructor
    protected SanPham() {}
    
    protected SanPham(Long id, String maSanPham, String tenSanPham, String moTa, 
                     BigDecimal rhoTa, String hinhAnh, Integer soLuongTonKho) {
        this.id = id;
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.moTa = moTa;
        this.rhoTa = rhoTa;
        this.hinhAnh = hinhAnh;
        this.soLuongTonKho = soLuongTonKho;
    }
    
    // Abstract methods - mỗi loại sản phẩm phải implement
    public abstract String getLoaiSanPham();
    public abstract String getThongTinChiTiet();
    public abstract boolean kiemTraTuongThich(String dongXe);
    
    // Business logic methods
    public boolean isAvailable() {
        return soLuongTonKho != null && soLuongTonKho > 0;
    }
    
    public boolean hasStock() {
        return isAvailable();
    }
    
    public boolean hasStock(int quantity) {
        return soLuongTonKho != null && soLuongTonKho >= quantity;
    }
    
    public String getFormattedPrice() {
        if (rhoTa == null) return "N/A";
        return String.format("%,.0f VND", rhoTa);
    }
    
    public void validate() {
        if (tenSanPham == null || tenSanPham.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên sản phẩm không được để trống");
        }
        if (rhoTa == null || rhoTa.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Giá sản phẩm phải lớn hơn 0");
        }
        if (soLuongTonKho == null || soLuongTonKho < 0) {
            throw new IllegalArgumentException("Số lượng tồn kho không được âm");
        }
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getMaSanPham() {
        return maSanPham;
    }
    
    public void setMaSanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }
    
    public String getTenSanPham() {
        return tenSanPham;
    }
    
    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }
    
    public String getMoTa() {
        return moTa;
    }
    
    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }
    
    public BigDecimal getRhoTa() {
        return rhoTa;
    }
    
    public void setRhoTa(BigDecimal rhoTa) {
        this.rhoTa = rhoTa;
    }
    
    public String getHinhAnh() {
        return hinhAnh;
    }
    
    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }
    
    public Integer getSoLuongTonKho() {
        return soLuongTonKho;
    }
    
    public void setSoLuongTonKho(Integer soLuongTonKho) {
        this.soLuongTonKho = soLuongTonKho;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return "SanPham{" +
                "id=" + id +
                ", maSanPham='" + maSanPham + '\'' +
                ", tenSanPham='" + tenSanPham + '\'' +
                ", rhoTa=" + rhoTa +
                ", soLuongTonKho=" + soLuongTonKho +
                ", loai='" + getLoaiSanPham() + '\'' +
                '}';
    }
}
