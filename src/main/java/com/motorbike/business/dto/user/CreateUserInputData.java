package com.motorbike.business.dto.user;

public class CreateUserInputData {
    private final String hoTen;
    private final String email;
    private final String tenDangNhap;
    private final String matKhau;
    private final String soDienThoai;
    private final String diaChi;
    private final String vaiTro;

    public CreateUserInputData(String hoTen, String email, String tenDangNhap, String matKhau,
                              String soDienThoai, String diaChi, String vaiTro) {
        this.hoTen = hoTen;
        this.email = email;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
        this.vaiTro = vaiTro;
    }

    public String getHoTen() {return hoTen;}
    public String getEmail() {return email;}
    public String getTenDangNhap() {return tenDangNhap;}
    public String getMatKhau() {return matKhau;}
    public String getSoDienThoai() {return soDienThoai;}
    public String getDiaChi() {return diaChi;}
    public String getVaiTro() {return vaiTro;}
}
