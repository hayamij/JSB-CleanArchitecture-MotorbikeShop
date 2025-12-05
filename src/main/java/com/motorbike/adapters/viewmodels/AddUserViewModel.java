package com.motorbike.adapters.viewmodels;

import com.motorbike.domain.entities.VaiTro;

public class AddUserViewModel {
    public boolean success = false;
    public boolean hasError = false;
    public String errorCode;
    public String errorMessage;
    public String successMessage;
    
    public Long maTaiKhoan;
    public String email;
    public String tenDangNhap;
    public String soDienThoai;
    public VaiTro vaiTro;
    public boolean hoatDong;
    public String ngayTao;
}
