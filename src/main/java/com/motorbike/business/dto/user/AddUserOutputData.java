package com.motorbike.business.dto.user;

import com.motorbike.domain.entities.VaiTro;
import java.time.LocalDateTime;

public class AddUserOutputData {
    private final boolean success;
    private final String errorCode;
    private final String errorMessage;
    
    private final Long maTaiKhoan;
    private final String email;
    private final String tenDangNhap;
    private final String soDienThoai;
    private final VaiTro vaiTro;
    private final boolean hoatDong;
    private final LocalDateTime ngayTao;
    
    private AddUserOutputData(boolean success, String errorCode, String errorMessage,
                             Long maTaiKhoan, String email, String tenDangNhap,
                             String soDienThoai, VaiTro vaiTro, boolean hoatDong,
                             LocalDateTime ngayTao) {
        this.success = success;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.maTaiKhoan = maTaiKhoan;
        this.email = email;
        this.tenDangNhap = tenDangNhap;
        this.soDienThoai = soDienThoai;
        this.vaiTro = vaiTro;
        this.hoatDong = hoatDong;
        this.ngayTao = ngayTao;
    }
    
    public static AddUserOutputData forSuccess(Long maTaiKhoan, String email,
                                               String tenDangNhap, String soDienThoai,
                                               VaiTro vaiTro, boolean hoatDong,
                                               LocalDateTime ngayTao) {
        return new AddUserOutputData(true, null, null, maTaiKhoan, email,
                                     tenDangNhap, soDienThoai, vaiTro, hoatDong, ngayTao);
    }
    
    public static AddUserOutputData forError(String errorCode, String errorMessage) {
        return new AddUserOutputData(false, errorCode, errorMessage, null, null,
                                     null, null, null, false, null);
    }
    
    public boolean isSuccess() { return success; }
    public String getErrorCode() { return errorCode; }
    public String getErrorMessage() { return errorMessage; }
    public Long getMaTaiKhoan() { return maTaiKhoan; }
    public String getEmail() { return email; }
    public String getTenDangNhap() { return tenDangNhap; }
    public String getSoDienThoai() { return soDienThoai; }
    public VaiTro getVaiTro() { return vaiTro; }
    public boolean isHoatDong() { return hoatDong; }
    public LocalDateTime getNgayTao() { return ngayTao; }
}
