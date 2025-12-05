package com.motorbike.business.dto.accessory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AddAccessoryOutputData {
    private final boolean success;
    private final String errorCode;
    private final String errorMessage;
    
    private final Long maSanPham;
    private final String tenSanPham;
    private final String loaiPhuKien;
    private final String thuongHieu;
    private final String chatLieu;
    private final String kichThuoc;
    private final BigDecimal gia;
    private final LocalDateTime ngayTao;
    
    private AddAccessoryOutputData(boolean success, String errorCode, String errorMessage,
                                  Long maSanPham, String tenSanPham, String loaiPhuKien,
                                  String thuongHieu, String chatLieu, String kichThuoc,
                                  BigDecimal gia, LocalDateTime ngayTao) {
        this.success = success;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.loaiPhuKien = loaiPhuKien;
        this.thuongHieu = thuongHieu;
        this.chatLieu = chatLieu;
        this.kichThuoc = kichThuoc;
        this.gia = gia;
        this.ngayTao = ngayTao;
    }
    
    public static AddAccessoryOutputData forSuccess(Long maSanPham, String tenSanPham,
                                                   String loaiPhuKien, String thuongHieu,
                                                   String chatLieu, String kichThuoc,
                                                   BigDecimal gia, LocalDateTime ngayTao) {
        return new AddAccessoryOutputData(true, null, null, maSanPham, tenSanPham,
                                         loaiPhuKien, thuongHieu, chatLieu, kichThuoc, gia, ngayTao);
    }
    
    public static AddAccessoryOutputData forError(String errorCode, String errorMessage) {
        return new AddAccessoryOutputData(false, errorCode, errorMessage,
                                         null, null, null, null, null, null, null, null);
    }
    
    public boolean isSuccess() { return success; }
    public String getErrorCode() { return errorCode; }
    public String getErrorMessage() { return errorMessage; }
    public Long getMaSanPham() { return maSanPham; }
    public String getTenSanPham() { return tenSanPham; }
    public String getLoaiPhuKien() { return loaiPhuKien; }
    public String getThuongHieu() { return thuongHieu; }
    public String getChatLieu() { return chatLieu; }
    public String getKichThuoc() { return kichThuoc; }
    public BigDecimal getGia() { return gia; }
    public LocalDateTime getNgayTao() { return ngayTao; }
}
