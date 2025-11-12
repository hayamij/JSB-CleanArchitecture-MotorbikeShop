package com.motorbike.domain.entities;

import java.math.BigDecimal;

/**
 * abstract class cho các loại sản phẩm
 * áp dụng open/close principle - mở rộng bằng cách tạo class mới, ko sửa code cũ
 */
public abstract class ProductCategory {
    
    protected final String categoryCode;
    protected final String categoryName;
    
    protected ProductCategory(String categoryCode, String categoryName) {
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
    }
    
    // business logic có thể khác nhau giữa các loại sản phẩm
    public abstract boolean requiresSpecifications();
    
    public abstract boolean hasWarranty();
    
    // validate giá dựa trên loại sản phẩm
    public abstract void validatePrice(BigDecimal price);
    
    public String getCategoryCode() {
        return categoryCode;
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ProductCategory that = (ProductCategory) obj;
        return categoryCode.equals(that.categoryCode);
    }
    
    @Override
    public int hashCode() {
        return categoryCode.hashCode();
    }
    
    @Override
    public String toString() {
        return categoryName;
    }
}
