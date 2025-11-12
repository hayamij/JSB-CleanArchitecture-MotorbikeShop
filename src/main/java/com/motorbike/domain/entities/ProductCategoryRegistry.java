package com.motorbike.domain.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

/**
 * registry quản lý các loại sản phẩm
 * cho phép thêm loại mới mà ko cần sửa code cũ (open/close principle)
 */
public class ProductCategoryRegistry {
    
    private static final Map<String, ProductCategory> categories = new HashMap<>();
    
    // khởi tạo sẵn các loại cơ bản
    static {
        register(new MotorcycleCategory());
        register(new AccessoryCategory());
    }
    
    /**
     * đăng ký loại sản phẩm mới
     * cho phép mở rộng bằng cách thêm loại mới
     */
    public static void register(ProductCategory category) {
        if (category == null) {
            throw new IllegalArgumentException("category ko được null");
        }
        categories.put(category.getCategoryCode(), category);
    }
    
    /**
     * lấy loại sản phẩm theo code
     */
    public static ProductCategory getByCode(String code) {
        ProductCategory category = categories.get(code);
        if (category == null) {
            throw new IllegalArgumentException("ko tìm thấy category với code: " + code);
        }
        return category;
    }
    
    /**
     * kiểm tra xem có tồn tại category với code này ko
     */
    public static boolean exists(String code) {
        return categories.containsKey(code);
    }
    
    /**
     * lấy tất cả categories
     */
    public static Collection<ProductCategory> getAllCategories() {
        return categories.values();
    }
    
    /**
     * các method tiện ích để lấy nhanh categories thông dụng
     */
    public static ProductCategory motorcycle() {
        return getByCode("MOTORCYCLE");
    }
    
    public static ProductCategory accessory() {
        return getByCode("ACCESSORY");
    }
}
