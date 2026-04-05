package com.motorbike.business.dto.order;

public class UpdateOrderOutputData {
    private final boolean success;
    private final String errorCode;
    private final String errorMessage;
    
    private final Long maDonHang;
    private final String message;

    private UpdateOrderOutputData(boolean success, String errorCode, String errorMessage,
                                 Long maDonHang, String message) {
        this.success = success;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.maDonHang = maDonHang;
        this.message = message;
    }

    public static UpdateOrderOutputData forSuccess(Long maDonHang, String message) {
        return new UpdateOrderOutputData(true, null, null, maDonHang, message);
    }

    public static UpdateOrderOutputData forError(String errorCode, String errorMessage) {
        return new UpdateOrderOutputData(false, errorCode, errorMessage, null, null);
    }

    public boolean isSuccess() {return success;}
    public String getErrorCode() {return errorCode;}
    public String getErrorMessage() {return errorMessage;}
    public Long getMaDonHang() {return maDonHang;}
    public String getMessage() {return message;}
}
