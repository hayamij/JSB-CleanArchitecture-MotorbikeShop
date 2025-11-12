package com.motorbike.domain.entities;

import com.motorbike.domain.exceptions.InvalidUserException;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

/**
 * entity người dùng
 * chứa logic nghiệp vụ về user
 */
public class User {
    private Long id;
    private String email;
    private String username;
    private String password; // sẽ dc hash
    private String phoneNumber;
    private UserRole role;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLoginAt;

    // pattern để validate email
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    // pattern số đt vn
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^(0|\\+84)[0-9]{9,10}$");

    // constructor
    public User(String email, String username, String password, String phoneNumber) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = UserRoleRegistry.customer(); // vai trò mặc định: khách hàng
        this.active = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // constructor đầy đủ
    public User(Long id, String email, String username, String password, 
                String phoneNumber, UserRole role, boolean active,
                LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime lastLoginAt) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lastLoginAt = lastLoginAt;
    }

    // validate format email
    public static void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidUserException("EMPTY_EMAIL", "email ko được rỗng");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidUserException("INVALID_EMAIL_FORMAT", "format email ko đúng");
        }
    }

    // validate tên đăng nhập
    public static void validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new InvalidUserException("EMPTY_USERNAME", "username ko được rỗng");
        }
        if (username.length() < 3) {
            throw new InvalidUserException("USERNAME_TOO_SHORT", "username phải >= 3 ký tự");
        }
        if (username.length() > 50) {
            throw new InvalidUserException("USERNAME_TOO_LONG", "username phải <= 50 ký tự");
        }
    }

    // validate mật khẩu
    public static void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new InvalidUserException("EMPTY_PASSWORD", "password ko được rỗng");
        }
        if (password.length() < 6) {
            throw new InvalidUserException("PASSWORD_TOO_SHORT", "password phải >= 6 ký tự");
        }
    }

    // validate số điện thoại
    public static void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
            if (!PHONE_PATTERN.matcher(phoneNumber).matches()) {
                throw new InvalidUserException("INVALID_PHONE_FORMAT", "format số đt ko đúng");
            }
        }
    }

    // kiểm tra user có thể đăng nhập ko
    public boolean canLogin() {
        return this.active;
    }

    // kiểm tra xem user có phải admin ko
    public boolean isAdmin() {
        return this.role != null && this.role.equals(UserRoleRegistry.admin());
    }

    // kiểm tra xem user có phải khách hàng ko
    public boolean isCustomer() {
        return this.role != null && this.role.equals(UserRoleRegistry.customer());
    }

    // cập nhật thời gian đăng nhập cuối
    public void recordLogin() {
        this.lastLoginAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // vô hiệu hóa user
    public void deactivate() {
        this.active = false;
        this.updatedAt = LocalDateTime.now();
    }

    // kích hoạt user
    public void activate() {
        this.active = true;
        this.updatedAt = LocalDateTime.now();
    }

    // getters
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UserRole getRole() {
        return role;
    }

    public boolean isActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    // setters (hạn chế để giữ tính bất biến khi có thể)
    public void setId(Long id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
        this.updatedAt = LocalDateTime.now();
    }

    public void setPhoneNumber(String phoneNumber) {
        validatePhoneNumber(phoneNumber);
        this.phoneNumber = phoneNumber;
        this.updatedAt = LocalDateTime.now();
    }

    public void setRole(UserRole role) {
        this.role = role;
        this.updatedAt = LocalDateTime.now();
    }
}
