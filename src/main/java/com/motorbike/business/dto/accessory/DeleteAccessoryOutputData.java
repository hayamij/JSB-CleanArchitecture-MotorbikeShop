package com.motorbike.business.dto.accessory;

public class DeleteAccessoryOutputData {
    private final boolean success;
    private final String errorCode;
    private final String errorMessage;
    private final Long maSanPham;
    private final String message;
    
    private DeleteAccessoryOutputData(boolean success, String errorCode, String errorMessage,
                                     Long maSanPham, String message) {
        this.success = success;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.maSanPham = maSanPham;
        this.message = message;
    }
    
    public static DeleteAccessoryOutputData forSuccess(Long maSanPham, String message) {
        return new DeleteAccessoryOutputData(true, null, null, maSanPham, message);
    }
    
    public static DeleteAccessoryOutputData forError(String errorCode, String errorMessage) {
        return new DeleteAccessoryOutputData(false, errorCode, errorMessage, null, null);
    }
    
    public boolean isSuccess() { return success; }
    public String getErrorCode() { return errorCode; }
    public String getErrorMessage() { return errorMessage; }
    public Long getMaSanPham() { return maSanPham; }
    public String getMessage() { return message; }
}
