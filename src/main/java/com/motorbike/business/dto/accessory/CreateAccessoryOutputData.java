package com.motorbike.business.dto.accessory;

import java.math.BigDecimal;

public class CreateAccessoryOutputData {
    private final boolean success;
    private final String errorCode;
    private final String errorMessage;
    
    private final Long maSanPham;
    private final String tenSanPham;
    private final BigDecimal gia;

    private CreateAccessoryOutputData(boolean success, String errorCode, String errorMessage,
                                     Long maSanPham, String tenSanPham, BigDecimal gia) {
        this.success = success;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.gia = gia;
    }

    public static CreateAccessoryOutputData forSuccess(Long maSanPham, String tenSanPham, BigDecimal gia) {
        return new CreateAccessoryOutputData(true, null, null, maSanPham, tenSanPham, gia);
    }

    public static CreateAccessoryOutputData forError(String errorCode, String errorMessage) {
        return new CreateAccessoryOutputData(false, errorCode, errorMessage, null, null, null);
    }

    public boolean isSuccess() {return success;}
    public String getErrorCode() {return errorCode;}
    public String getErrorMessage() {return errorMessage;}
    public Long getMaSanPham() {return maSanPham;}
    public String getTenSanPham() {return tenSanPham;}
    public BigDecimal getGia() {return gia;}
}
