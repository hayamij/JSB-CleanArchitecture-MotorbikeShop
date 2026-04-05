package com.motorbike.business.dto.user;

import com.motorbike.domain.entities.VaiTro;

public class AddUserInputData {
    private final String hoTen;
    private final String email;
    private final String tenDangNhap;
    private final String matKhau;
    private final String soDienThoai;
    private final String diaChi;
    private final VaiTro vaiTro;
    private final boolean hoatDong;
    
    public AddUserInputData(String hoTen, String email, String tenDangNhap, String matKhau,
                           String soDienThoai, String diaChi, VaiTro vaiTro, boolean hoatDong) {
        this.hoTen = hoTen;
        this.email = email;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
        this.vaiTro = vaiTro;
        this.hoatDong = hoatDong;
    }
    
    // Constructor with 7 params (String vaiTro instead of VaiTro enum)
    public AddUserInputData(String hoTen, String email, String tenDangNhap, String matKhau,
                           String soDienThoai, String diaChi, String vaiTroStr) {
        this.hoTen = hoTen;
        this.email = email;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
        this.vaiTro = VaiTro.fromString(vaiTroStr);
        this.hoatDong = true;
    }
    
    public String getHoTen() { return hoTen; }
    public String getEmail() { return email; }
    public String getTenDangNhap() { return tenDangNhap; }
    public String getUsername() { return tenDangNhap; } // Alias for consistency
    public String getMatKhau() { return matKhau; }
    public String getPassword() { return matKhau; } // Alias for consistency
    public String getSoDienThoai() { return soDienThoai; }
    public String getDiaChi() { return diaChi; }
    public VaiTro getVaiTro() { return vaiTro; }
    public boolean isHoatDong() { return hoatDong; }
}
