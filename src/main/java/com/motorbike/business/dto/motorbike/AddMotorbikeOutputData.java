package com.motorbike.business.dto.motorbike;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AddMotorbikeOutputData {
    private final boolean success;
    private final String errorCode;
    private final String errorMessage;
    
    private final Long maSanPham;
    private final String tenSanPham;
    private final String hangXe;
    private final String dongXe;
    private final String mauSac;
    private final int namSanXuat;
    private final int dungTich;
    private final BigDecimal gia;
    private final LocalDateTime ngayTao;
    
    private AddMotorbikeOutputData(boolean success, String errorCode, String errorMessage,
                                  Long maSanPham, String tenSanPham, String hangXe, String dongXe,
                                  String mauSac, int namSanXuat, int dungTich, BigDecimal gia,
                                  LocalDateTime ngayTao) {
        this.success = success;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.hangXe = hangXe;
        this.dongXe = dongXe;
        this.mauSac = mauSac;
        this.namSanXuat = namSanXuat;
        this.dungTich = dungTich;
        this.gia = gia;
        this.ngayTao = ngayTao;
    }
    
    public static AddMotorbikeOutputData forSuccess(Long maSanPham, String tenSanPham,
                                                   String hangXe, String dongXe, String mauSac,
                                                   int namSanXuat, int dungTich, BigDecimal gia,
                                                   LocalDateTime ngayTao) {
        return new AddMotorbikeOutputData(true, null, null, maSanPham, tenSanPham,
                                         hangXe, dongXe, mauSac, namSanXuat, dungTich, gia, ngayTao);
    }
    
    public static AddMotorbikeOutputData forError(String errorCode, String errorMessage) {
        return new AddMotorbikeOutputData(false, errorCode, errorMessage,
                                         null, null, null, null, null, 0, 0, null, null);
    }
    
    public boolean isSuccess() { return success; }
    public String getErrorCode() { return errorCode; }
    public String getErrorMessage() { return errorMessage; }
    public Long getMaSanPham() { return maSanPham; }
    public String getTenSanPham() { return tenSanPham; }
    public String getHangXe() { return hangXe; }
    public String getDongXe() { return dongXe; }
    public String getMauSac() { return mauSac; }
    public int getNamSanXuat() { return namSanXuat; }
    public int getDungTich() { return dungTich; }
    public BigDecimal getGia() { return gia; }
    public LocalDateTime getNgayTao() { return ngayTao; }
}
