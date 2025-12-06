package com.motorbike.domain.entities;

/**
 * Domain Value Object - Thống kê bán hàng của sản phẩm
 * Sử dụng cho tính năng best-selling products
 */
public class ProductSalesStats implements Comparable<ProductSalesStats> {
    private final Long maSanPham;
    private final String tenSanPham;
    private final int tongSoLuongBan;
    
    public ProductSalesStats(Long maSanPham, String tenSanPham, int tongSoLuongBan) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.tongSoLuongBan = tongSoLuongBan;
    }
    
    /**
     * Business rule: So sánh sản phẩm theo số lượng bán
     * Sản phẩm bán nhiều hơn sẽ được xếp cao hơn
     */
    @Override
    public int compareTo(ProductSalesStats other) {
        // Sắp xếp giảm dần theo số lượng bán
        return Integer.compare(other.tongSoLuongBan, this.tongSoLuongBan);
    }
    
    /**
     * Business rule: Kiểm tra sản phẩm có đủ điều kiện là "best-seller"
     * Sản phẩm phải bán được ít nhất 1 đơn vị
     */
    public boolean isDangBanChay() {
        return tongSoLuongBan > 0;
    }
    
    // Getters
    public Long getMaSanPham() {
        return maSanPham;
    }
    
    public String getTenSanPham() {
        return tenSanPham;
    }
    
    public int getTongSoLuongBan() {
        return tongSoLuongBan;
    }
}
