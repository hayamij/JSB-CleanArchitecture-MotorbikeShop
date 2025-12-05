package com.motorbike.business.dto.user;

public class DeleteUserOutputData {
    private final boolean success;
    private final String errorCode;
    private final String errorMessage;
    
    private final Long maTaiKhoan;
    private final String tenDangNhap;
    
    private DeleteUserOutputData(boolean success, String errorCode, String errorMessage,
                                Long maTaiKhoan, String tenDangNhap) {
        this.success = success;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.maTaiKhoan = maTaiKhoan;
        this.tenDangNhap = tenDangNhap;
    }
    
    public static DeleteUserOutputData forSuccess(Long maTaiKhoan, String tenDangNhap) {
        return new DeleteUserOutputData(true, null, null, maTaiKhoan, tenDangNhap);
    }
    
    public static DeleteUserOutputData forError(String errorCode, String errorMessage) {
        return new DeleteUserOutputData(false, errorCode, errorMessage, null, null);
    }
    
    public boolean isSuccess() { return success; }
    public String getErrorCode() { return errorCode; }
    public String getErrorMessage() { return errorMessage; }
    public Long getMaTaiKhoan() { return maTaiKhoan; }
    public String getTenDangNhap() { return tenDangNhap; }
}
