package com.motorbike.domain.entities;

import com.motorbike.domain.exceptions.InvalidProductException;
import java.math.BigDecimal;

/**
 * loại sản phẩm: phụ kiện
 * yêu cầu đơn giản hơn xe máy
 */
public class AccessoryCategory extends ProductCategory {
    
    private static final BigDecimal MIN_PRICE = new BigDecimal("10000"); // tối thiểu 10k
    private static final BigDecimal MAX_PRICE = new BigDecimal("50000000"); // tối đa 50 triệu
    
    public AccessoryCategory() {
        super("ACCESSORY", "Phụ kiện");
    }
    
    @Override
    public boolean requiresSpecifications() {
        return false; // phụ kiện ko bắt buộc có thông số kỹ thuật
    }
    
    @Override
    public boolean hasWarranty() {
        return false; // phụ kiện thường ko có bảo hành
    }
    
    @Override
    public void validatePrice(BigDecimal price) {
        if (price == null) {
            throw new InvalidProductException("NULL_PRICE", "giá phụ kiện ko được null");
        }
        if (price.compareTo(MIN_PRICE) < 0) {
            throw new InvalidProductException("PRICE_TOO_LOW", 
                "giá phụ kiện phải >= " + MIN_PRICE + " VND");
        }
        if (price.compareTo(MAX_PRICE) > 0) {
            throw new InvalidProductException("PRICE_TOO_HIGH", 
                "giá phụ kiện phải <= " + MAX_PRICE + " VND");
        }
    }
}
