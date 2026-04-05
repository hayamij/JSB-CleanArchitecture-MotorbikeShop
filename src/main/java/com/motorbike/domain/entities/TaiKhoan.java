package com.motorbike.domain.entities;

import com.motorbike.domain.exceptions.*;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class TaiKhoan {
    private Long maTaiKhoan;
    private String hoTen;
    private String email;
    private String tenDangNhap;
    private String matKhau;
    private String soDienThoai;
    private String diaChi;
    private VaiTro vaiTro;
    private boolean hoatDong;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;
    private LocalDateTime lanDangNhapCuoi;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(0|\\+84)[0-9]{9,10}$");

    public TaiKhoan(String hoTen, String email, String tenDangNhap, String matKhau,
                    String soDienThoai, String diaChi) {
        validateHoTen(hoTen);
        validateEmail(email);
        validateTenDangNhap(tenDangNhap);
        validateMatKhau(matKhau);
        validateSoDienThoai(soDienThoai);
        
        this.hoTen = hoTen;
        this.email = email;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
        this.vaiTro = VaiTro.CUSTOMER;
        this.hoatDong = true;
        this.ngayTao = LocalDateTime.now();
        this.ngayCapNhat = LocalDateTime.now();
    }

    public TaiKhoan(Long maTaiKhoan, String hoTen, String email, String tenDangNhap, String matKhau,
                    String soDienThoai, String diaChi, VaiTro vaiTro, boolean hoatDong,
                    LocalDateTime ngayTao, LocalDateTime ngayCapNhat, LocalDateTime lanDangNhapCuoi) {
        this.maTaiKhoan = maTaiKhoan;
        this.hoTen = hoTen;
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
    
    // Constructor with 5 parameters (no phone)
    public TaiKhoan(String hoTen, String email, String tenDangNhap, String matKhau, String diaChi) {
        this(hoTen, email, tenDangNhap, matKhau, null, diaChi);
    }

    public static void validateHoTen(String hoTen) {
        if (hoTen == null || hoTen.trim().isEmpty()) {
            throw ValidationException.emptyFullName();
        }
        if (hoTen.trim().length() < 2) {
            throw ValidationException.fullNameTooShort();
        }
    }

    public static void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw ValidationException.emptyEmail();
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw ValidationException.invalidEmail();
        }
    }

    public static void validateTenDangNhap(String tenDangNhap) {
        if (tenDangNhap == null || tenDangNhap.trim().isEmpty()) {
            throw ValidationException.emptyUsername();
        }
        if (tenDangNhap.length() < 3) {
            throw ValidationException.usernameTooShort();
        }
        if (tenDangNhap.length() > 50) {
            throw ValidationException.usernameTooLong();
        }
    }

    public static void validateMatKhau(String matKhau) {
        if (matKhau == null || matKhau.isEmpty()) {
            throw ValidationException.emptyPassword();
        }
        if (matKhau.length() < 6) {
            throw ValidationException.passwordTooShort();
        }
    }

    public static void validateSoDienThoai(String soDienThoai) {
        if (soDienThoai == null || soDienThoai.trim().isEmpty()) {
            throw ValidationException.emptyPhone();
        }
        if (!PHONE_PATTERN.matcher(soDienThoai).matches()) {
            throw ValidationException.invalidPhoneFormat();
        }
    }

    public static void checkInput(Long userId) {
        if (userId == null) {
            throw ValidationException.invalidUserId();
        }
    }

    public static void checkInputForLogin(String emailOrUsernameOrPhone, String password) {
        // Kiểm tra input không được rỗng
        if (emailOrUsernameOrPhone == null || emailOrUsernameOrPhone.trim().isEmpty()) {
            throw ValidationException.emptyLoginIdentifier();
        }
        
        // Kiểm tra password
        if (password == null || password.isEmpty()) {
            throw ValidationException.emptyPassword();
        }
        
        // Không validate format ở đây vì có thể là email hoặc username
        // Repository sẽ tìm kiếm theo email hoặc username
    }

    public static void checkInputForRegister(String hoTen, String email, String tenDangNhap, String matKhau, String soDienThoai) {
        validateHoTen(hoTen);
        validateEmail(email);
        validateTenDangNhap(tenDangNhap);
        validateMatKhau(matKhau);
        validateSoDienThoai(soDienThoai);
    }

    public void dangNhapThanhCong() {
        if (!this.hoatDong) {
            throw ValidationException.accountInactive();
        }
        this.lanDangNhapCuoi = LocalDateTime.now();
    }

    public void khoaTaiKhoan() {this.hoatDong = false; this.ngayCapNhat = LocalDateTime.now();}
    public void moKhoaTaiKhoan() {this.hoatDong = true; this.ngayCapNhat = LocalDateTime.now();}
    public void thangCapAdmin() {this.vaiTro = VaiTro.ADMIN; this.ngayCapNhat = LocalDateTime.now();}
    public void haCapCustomer() {this.vaiTro = VaiTro.CUSTOMER; this.ngayCapNhat = LocalDateTime.now();}
    public void capNhatMatKhau(String matKhauMoi) {validateMatKhau(matKhauMoi); this.matKhau = matKhauMoi; this.ngayCapNhat = LocalDateTime.now();}
    public boolean laAdmin() {return this.vaiTro == VaiTro.ADMIN;}
    public boolean laCustomer() {return this.vaiTro == VaiTro.CUSTOMER;}
    public boolean kiemTraMatKhau(String matKhauNhap) {return this.matKhau.equals(matKhauNhap);}

    public Long getMaTaiKhoan() {return maTaiKhoan;}
    public String getHoTen() {return hoTen;}
    public String getEmail() {return email;}
    public String getTenDangNhap() {return tenDangNhap;}
    public String getMatKhau() {return matKhau;}
    public String getSoDienThoai() {return soDienThoai;}
    public String getDiaChi() {return diaChi;}
    public VaiTro getVaiTro() {return vaiTro;}
    public boolean isHoatDong() {return hoatDong;}
    public LocalDateTime getNgayTao() {return ngayTao;}
    public LocalDateTime getNgayCapNhat() {return ngayCapNhat;}
    public LocalDateTime getLanDangNhapCuoi() {return lanDangNhapCuoi;}
    public void setMaTaiKhoan(Long maTaiKhoan) {this.maTaiKhoan = maTaiKhoan;}
    public void setHoTen(String hoTen) {validateHoTen(hoTen); this.hoTen = hoTen; this.ngayCapNhat = LocalDateTime.now();}
    public void setEmail(String email) {validateEmail(email); this.email = email; this.ngayCapNhat = LocalDateTime.now();}
    public void setTenDangNhap(String tenDangNhap) {validateTenDangNhap(tenDangNhap); this.tenDangNhap = tenDangNhap; this.ngayCapNhat = LocalDateTime.now();}
    public void setSoDienThoai(String soDienThoai) {validateSoDienThoai(soDienThoai); this.soDienThoai = soDienThoai; this.ngayCapNhat = LocalDateTime.now();}
    public void setDiaChi(String diaChi) {this.diaChi = diaChi; this.ngayCapNhat = LocalDateTime.now();}
    public void setVaiTro(VaiTro vaiTro) {this.vaiTro = vaiTro; this.ngayCapNhat = LocalDateTime.now();}
    public void setMaTK(Long maTaiKhoan) {this.maTaiKhoan = maTaiKhoan;}
    public Long getMaNguoiDung() {return maTaiKhoan;} // Alias for compatibility
}
