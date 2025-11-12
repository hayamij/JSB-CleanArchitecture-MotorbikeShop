package com.motorbike.domain.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

/**
 * registry quản lý vai trò người dùng
 * cho phép thêm vai trò mới mà ko cần sửa code cũ
 */
public class UserRoleRegistry {
    
    private static final Map<String, UserRole> roles = new HashMap<>();
    
    // khởi tạo sẵn các vai trò cơ bản
    static {
        // vai trò khách hàng
        UserRole customer = new UserRole("CUSTOMER", "Khách hàng");
        customer.addPermission("VIEW_PRODUCT");
        customer.addPermission("ADD_TO_CART");
        customer.addPermission("CHECKOUT");
        customer.addPermission("VIEW_ORDER");
        register(customer);
        
        // vai trò admin
        UserRole admin = new UserRole("ADMIN", "Quản trị viên");
        admin.addPermission("VIEW_PRODUCT");
        admin.addPermission("ADD_PRODUCT");
        admin.addPermission("EDIT_PRODUCT");
        admin.addPermission("DELETE_PRODUCT");
        admin.addPermission("VIEW_ORDER");
        admin.addPermission("MANAGE_ORDER");
        admin.addPermission("VIEW_USER");
        admin.addPermission("MANAGE_USER");
        register(admin);
    }
    
    /**
     * đăng ký vai trò mới
     * ví dụ: có thể thêm vai trò STAFF, WAREHOUSE, ACCOUNTANT,...
     */
    public static void register(UserRole role) {
        if (role == null) {
            throw new IllegalArgumentException("role ko được null");
        }
        roles.put(role.getRoleCode(), role);
    }
    
    /**
     * lấy vai trò theo code
     */
    public static UserRole getByCode(String code) {
        if (code == null) {
            throw new IllegalArgumentException("role code ko được null");
        }
        UserRole role = roles.get(code.toUpperCase());
        if (role == null) {
            throw new IllegalArgumentException("ko tìm thấy role với code: " + code);
        }
        return role;
    }
    
    /**
     * kiểm tra xem có tồn tại role với code này ko
     */
    public static boolean exists(String code) {
        return code != null && roles.containsKey(code.toUpperCase());
    }
    
    /**
     * lấy tất cả roles
     */
    public static Collection<UserRole> getAllRoles() {
        return roles.values();
    }
    
    /**
     * các method tiện ích để lấy nhanh roles thông dụng
     */
    public static UserRole customer() {
        return getByCode("CUSTOMER");
    }
    
    public static UserRole admin() {
        return getByCode("ADMIN");
    }
}
