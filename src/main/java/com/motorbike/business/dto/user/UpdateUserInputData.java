package com.motorbike.business.dto.user;

import com.motorbike.domain.entities.VaiTro;

public class UpdateUserInputData {
    private final Long maTaiKhoan;
    private final String tenDangNhap;
    private final String hoTen;
    private final String email;
    private final String soDienThoai;
    private final String diaChi;
    private final VaiTro vaiTro;
    private final boolean hoatDong;
    
    public UpdateUserInputData(Long maTaiKhoan, String tenDangNhap, String hoTen, String email, String soDienThoai,
                              String diaChi, VaiTro vaiTro, boolean hoatDong) {
        this.maTaiKhoan = maTaiKhoan;
        this.tenDangNhap = tenDangNhap;
        this.hoTen = hoTen;
        this.email = email;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
        this.vaiTro = vaiTro;
        this.hoatDong = hoatDong;
    }
    
    public Long getMaTaiKhoan() { return maTaiKhoan; }
    public String getTenDangNhap() { return tenDangNhap; }
    public String getHoTen() { return hoTen; }
    public String getEmail() { return email; }
    public String getSoDienThoai() { return soDienThoai; }
    public String getDiaChi() { return diaChi; }
    public VaiTro getVaiTro() { return vaiTro; }
    public boolean isHoatDong() { return hoatDong; }
}
