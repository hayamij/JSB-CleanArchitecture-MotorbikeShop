package com.motorbike.adapters.util;

import com.motorbike.domain.entities.VaiTro;

/**
 * Utility class to convert between presentation layer string values
 * and domain enums. This prevents controllers from directly importing
 * domain entities while maintaining type safety in the business layer.
 */
public class RoleConverter {
    
    public static VaiTro fromString(String roleStr) {
        if (roleStr == null) {
            return VaiTro.CUSTOMER; // Default
        }
        
        switch (roleStr.toUpperCase()) {
            case "ADMIN":
                return VaiTro.ADMIN;
            case "CUSTOMER":
            case "KHACH_HANG":
                return VaiTro.CUSTOMER;
            default:
                return VaiTro.CUSTOMER;
        }
    }
}
