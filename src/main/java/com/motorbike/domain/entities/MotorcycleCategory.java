package com.motorbike.domain.entities;

import com.motorbike.domain.exceptions.InvalidProductException;
import java.math.BigDecimal;

/**
 * loại sản phẩm: xe máy
 * có yêu cầu đặc biệt như thông số kỹ thuật bắt buộc, bảo hành
 */
public class MotorcycleCategory extends ProductCategory {
    
    private static final BigDecimal MIN_PRICE = new BigDecimal("5000000"); // tối thiểu 5 triệu
    private static final BigDecimal MAX_PRICE = new BigDecimal("500000000"); // tối đa 500 triệu
    
    public MotorcycleCategory() {
        super("MOTORCYCLE", "Xe máy");
    }
    
    @Override
    public boolean requiresSpecifications() {
        return true; // xe máy bắt buộc phải có thông số kỹ thuật
    }
    
    @Override
    public boolean hasWarranty() {
        return true; // xe máy có bảo hành
    }
    
    @Override
    public void validatePrice(BigDecimal price) {
        if (price == null) {
            throw new InvalidProductException("NULL_PRICE", "giá xe máy ko được null");
        }
        if (price.compareTo(MIN_PRICE) < 0) {
            throw new InvalidProductException("PRICE_TOO_LOW", 
                "giá xe máy phải >= " + MIN_PRICE + " VND");
        }
        if (price.compareTo(MAX_PRICE) > 0) {
            throw new InvalidProductException("PRICE_TOO_HIGH", 
                "giá xe máy phải <= " + MAX_PRICE + " VND");
        }
    }
}
