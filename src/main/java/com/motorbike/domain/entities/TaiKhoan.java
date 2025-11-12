package com.motorbike.domain.entities;

import com.motorbike.domain.exceptions.InvalidUserException;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

/**
 * TaiKhoan (User Account)
 * Đại diện cho tài khoản người dùng trong hệ thống
 */
public class TaiKhoan {
    private Long maTaiKhoan; // id
    private String email;
    private String tenDangNhap; // username
    private String matKhau; // password (hashed)
    private String soDienThoai; // phone number
    private String diaChi; // address
    private VaiTro vaiTro; // role (CUSTOMER, ADMIN)
    private boolean hoatDong; // active status
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;
    private LocalDateTime lanDangNhapCuoi; // last login

    // Patterns cho validation
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^(0|\\+84)[0-9]{9,10}$");

    // Constructor cho tài khoản mới
    public TaiKhoan(String email, String tenDangNhap, String matKhau, 
                    String soDienThoai, String diaChi) {
        validateEmail(email);
        validateTenDangNhap(tenDangNhap);
        validateMatKhau(matKhau);
        validateSoDienThoai(soDienThoai);
        
        this.email = email;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
        this.vaiTro = VaiTro.CUSTOMER; // mặc định là khách hàng
        this.hoatDong = true;
        this.ngayTao = LocalDateTime.now();
        this.ngayCapNhat = LocalDateTime.now();
    }

    // Constructor đầy đủ (reconstruct từ DB)
    public TaiKhoan(Long maTaiKhoan, String email, String tenDangNhap, String matKhau,
                    String soDienThoai, String diaChi, VaiTro vaiTro, boolean hoatDong,
                    LocalDateTime ngayTao, LocalDateTime ngayCapNhat, LocalDateTime lanDangNhapCuoi) {
        this.maTaiKhoan = maTaiKhoan;
        this.email = email;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
        this.vaiTro = vaiTro;
        this.hoatDong = hoatDong;
        this.ngayTao = ngayTao;
        this.ngayCapNhat = ngayCapNhat;
        this.lanDangNhapCuoi = lanDangNhapCuoi;
    }

    // Validation methods
    public static void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidUserException("EMPTY_EMAIL", "Email không được rỗng");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidUserException("INVALID_EMAIL_FORMAT", "Format email không đúng");
        }
    }

    public static void validateTenDangNhap(String tenDangNhap) {
        if (tenDangNhap == null || tenDangNhap.trim().isEmpty()) {
            throw new InvalidUserException("EMPTY_USERNAME", "Tên đăng nhập không được rỗng");
        }
        if (tenDangNhap.length() < 3) {
            throw new InvalidUserException("USERNAME_TOO_SHORT", "Tên đăng nhập phải >= 3 ký tự");
        }
        if (tenDangNhap.length() > 50) {
            throw new InvalidUserException("USERNAME_TOO_LONG", "Tên đăng nhập phải <= 50 ký tự");
        }
    }

    public static void validateMatKhau(String matKhau) {
        if (matKhau == null || matKhau.isEmpty()) {
            throw new InvalidUserException("EMPTY_PASSWORD", "Mật khẩu không được rỗng");
        }
        if (matKhau.length() < 6) {
            throw new InvalidUserException("PASSWORD_TOO_SHORT", "Mật khẩu phải >= 6 ký tự");
        }
    }

    public static void validateSoDienThoai(String soDienThoai) {
        if (soDienThoai == null || soDienThoai.trim().isEmpty()) {
            throw new InvalidUserException("EMPTY_PHONE", "Số điện thoại không được rỗng");
        }
        if (!PHONE_PATTERN.matcher(soDienThoai).matches()) {
            throw new InvalidUserException("INVALID_PHONE_FORMAT", 
                "Số điện thoại không đúng định dạng (VD: 0912345678 hoặc +84912345678)");
        }
    }

    // Business logic: Kiểm tra mật khẩu
    public boolean kiemTraMatKhau(String matKhauNhap) {
        return this.matKhau.equals(matKhauNhap);
    }

    // Business logic: Cập nhật mật khẩu
    public void capNhatMatKhau(String matKhauMoi) {
        validateMatKhau(matKhauMoi);
        this.matKhau = matKhauMoi;
        this.ngayCapNhat = LocalDateTime.now();
    }

    // Business logic: Đăng nhập thành công
    public void dangNhapThanhCong() {
        if (!this.hoatDong) {
            throw new InvalidUserException("ACCOUNT_INACTIVE", "Tài khoản đã bị khóa");
        }
        this.lanDangNhapCuoi = LocalDateTime.now();
    }

    // Business logic: Khóa tài khoản
    public void khoaTaiKhoan() {
        this.hoatDong = false;
        this.ngayCapNhat = LocalDateTime.now();
    }

    // Business logic: Mở khóa tài khoản
    public void moKhoaTaiKhoan() {
        this.hoatDong = true;
        this.ngayCapNhat = LocalDateTime.now();
    }

    // Business logic: Thăng cấp lên admin
    public void thangCapAdmin() {
        this.vaiTro = VaiTro.ADMIN;
        this.ngayCapNhat = LocalDateTime.now();
    }

    // Business logic: Hạ cấp xuống customer
    public void haCapCustomer() {
        this.vaiTro = VaiTro.CUSTOMER;
        this.ngayCapNhat = LocalDateTime.now();
    }

    // Business logic: Kiểm tra là admin
    public boolean laAdmin() {
        return this.vaiTro == VaiTro.ADMIN;
    }

    // Business logic: Kiểm tra là customer
    public boolean laCustomer() {
        return this.vaiTro == VaiTro.CUSTOMER;
    }

    // Getters
    public Long getMaTaiKhoan() {
        return maTaiKhoan;
    }

    public String getEmail() {
        return email;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public VaiTro getVaiTro() {
        return vaiTro;
    }

    public boolean isHoatDong() {
        return hoatDong;
    }

    public LocalDateTime getNgayTao() {
        return ngayTao;
    }

    public LocalDateTime getNgayCapNhat() {
        return ngayCapNhat;
    }

    public LocalDateTime getLanDangNhapCuoi() {
        return lanDangNhapCuoi;
    }

    // Setters (limited)
    public void setMaTaiKhoan(Long maTaiKhoan) {
        this.maTaiKhoan = maTaiKhoan;
    }

    public void setEmail(String email) {
        validateEmail(email);
        this.email = email;
        this.ngayCapNhat = LocalDateTime.now();
    }

    public void setSoDienThoai(String soDienThoai) {
        validateSoDienThoai(soDienThoai);
        this.soDienThoai = soDienThoai;
        this.ngayCapNhat = LocalDateTime.now();
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
        this.ngayCapNhat = LocalDateTime.now();
    }
}
