package com.motorbike.business.dto.motorbike;

import java.math.BigDecimal;

public class CreateMotorbikeOutputData {
    private final boolean success;
    private final String errorCode;
    private final String errorMessage;
    
    private final Long maSanPham;
    private final String tenSanPham;
    private final BigDecimal gia;

    private CreateMotorbikeOutputData(boolean success, String errorCode, String errorMessage,
                                     Long maSanPham, String tenSanPham, BigDecimal gia) {
        this.success = success;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.gia = gia;
    }

    public static CreateMotorbikeOutputData forSuccess(Long maSanPham, String tenSanPham, BigDecimal gia) {
        return new CreateMotorbikeOutputData(true, null, null, maSanPham, tenSanPham, gia);
    }

    public static CreateMotorbikeOutputData forError(String errorCode, String errorMessage) {
        return new CreateMotorbikeOutputData(false, errorCode, errorMessage, null, null, null);
    }

    public boolean isSuccess() {return success;}
    public String getErrorCode() {return errorCode;}
    public String getErrorMessage() {return errorMessage;}
    public Long getMaSanPham() {return maSanPham;}
    public String getTenSanPham() {return tenSanPham;}
    public BigDecimal getGia() {return gia;}
}
