package com.motorbike.business.dto.user;

public class ValidateUserRegistrationInputData {
    private final String hoTen;
    private final String email;
    private final String tenDangNhap;
    private final String matKhau;
    private final String soDienThoai;
    
    public ValidateUserRegistrationInputData(String hoTen, String email, String tenDangNhap, 
                                            String matKhau, String soDienThoai) {
        this.hoTen = hoTen;
        this.email = email;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.soDienThoai = soDienThoai;
    }
    
    public String getHoTen() { return hoTen; }
    public String getEmail() { return email; }
    public String getTenDangNhap() { return tenDangNhap; }
    public String getMatKhau() { return matKhau; }
    public String getSoDienThoai() { return soDienThoai; }
}
