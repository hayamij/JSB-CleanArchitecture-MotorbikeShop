package com.motorbike.business.dto.motorbike;

public class DeleteMotorbikeOutputData {
    private final boolean success;
    private final String errorCode;
    private final String errorMessage;
    private final Long maSanPham;
    private final String tenSanPham;
    
    private DeleteMotorbikeOutputData(boolean success, String errorCode, String errorMessage,
                                     Long maSanPham, String tenSanPham) {
        this.success = success;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
    }
    
    public static DeleteMotorbikeOutputData forSuccess(Long maSanPham, String tenSanPham) {
        return new DeleteMotorbikeOutputData(true, null, null, maSanPham, tenSanPham);
    }
    
    public static DeleteMotorbikeOutputData forError(String errorCode, String errorMessage) {
        return new DeleteMotorbikeOutputData(false, errorCode, errorMessage, null, null);
    }
    
    public boolean isSuccess() { return success; }
    public String getErrorCode() { return errorCode; }
    public String getErrorMessage() { return errorMessage; }
    public Long getMaSanPham() { return maSanPham; }
    public String getTenSanPham() { return tenSanPham; }
}
