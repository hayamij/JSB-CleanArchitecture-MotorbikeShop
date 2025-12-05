package com.motorbike.business.dto.user;

public class CreateUserInputData {
    private final String email;
    private final String tenDangNhap;
    private final String matKhau;
    private final String soDienThoai;
    private final String diaChi;
    private final String vaiTro;

    public CreateUserInputData(String email, String tenDangNhap, String matKhau,
                              String soDienThoai, String diaChi, String vaiTro) {
        this.email = email;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
        this.vaiTro = vaiTro;
    }

    public String getEmail() {return email;}
    public String getTenDangNhap() {return tenDangNhap;}
    public String getMatKhau() {return matKhau;}
    public String getSoDienThoai() {return soDienThoai;}
    public String getDiaChi() {return diaChi;}
    public String getVaiTro() {return vaiTro;}
}
