package com.motorbike.domain.entities;

import java.util.HashSet;
import java.util.Set;

/**
 * vai trò người dùng
 * ko dùng enum để có thể thêm vai trò mới linh hoạt
 */
public class UserRole {
    
    private final String roleCode;
    private final String roleName;
    private final Set<String> permissions;
    
    public UserRole(String roleCode, String roleName) {
        if (roleCode == null || roleCode.trim().isEmpty()) {
            throw new IllegalArgumentException("role code ko được rỗng");
        }
        if (roleName == null || roleName.trim().isEmpty()) {
            throw new IllegalArgumentException("role name ko được rỗng");
        }
        this.roleCode = roleCode.toUpperCase();
        this.roleName = roleName;
        this.permissions = new HashSet<>();
    }
    
    /**
     * thêm quyền cho vai trò này
     */
    public void addPermission(String permission) {
        if (permission != null && !permission.trim().isEmpty()) {
            permissions.add(permission.toUpperCase());
        }
    }
    
    /**
     * xóa quyền
     */
    public void removePermission(String permission) {
        permissions.remove(permission.toUpperCase());
    }
    
    /**
     * kiểm tra có quyền này ko
     */
    public boolean hasPermission(String permission) {
        return permissions.contains(permission.toUpperCase());
    }
    
    public String getRoleCode() {
        return roleCode;
    }
    
    public String getRoleName() {
        return roleName;
    }
    
    public Set<String> getPermissions() {
        return new HashSet<>(permissions);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UserRole that = (UserRole) obj;
        return roleCode.equals(that.roleCode);
    }
    
    @Override
    public int hashCode() {
        return roleCode.hashCode();
    }
    
    @Override
    public String toString() {
        return roleName + " (" + roleCode + ")";
    }
}
