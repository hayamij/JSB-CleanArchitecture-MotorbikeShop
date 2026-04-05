package com.motorbike.business.dto.user;

public class CreateUserOutputData {
    private final boolean success;
    private final String errorCode;
    private final String errorMessage;
    
    private final Long maTaiKhoan;
    private final String email;
    private final String tenDangNhap;

    private CreateUserOutputData(boolean success, String errorCode, String errorMessage,
                                Long maTaiKhoan, String email, String tenDangNhap) {
        this.success = success;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.maTaiKhoan = maTaiKhoan;
        this.email = email;
        this.tenDangNhap = tenDangNhap;
    }

    public static CreateUserOutputData forSuccess(Long maTaiKhoan, String email, String tenDangNhap) {
        return new CreateUserOutputData(true, null, null, maTaiKhoan, email, tenDangNhap);
    }

    public static CreateUserOutputData forError(String errorCode, String errorMessage) {
        return new CreateUserOutputData(false, errorCode, errorMessage, null, null, null);
    }

    public boolean isSuccess() {return success;}
    public String getErrorCode() {return errorCode;}
    public String getErrorMessage() {return errorMessage;}
    public Long getMaTaiKhoan() {return maTaiKhoan;}
    public String getEmail() {return email;}
    public String getTenDangNhap() {return tenDangNhap;}
}
