package com.motorbike.business.dto.order;

public class UpdateOrderInputData {
    private final Long maDonHang;
    private final String trangThai;
    private final String ghiChu;

    public UpdateOrderInputData(Long maDonHang, String trangThai, String ghiChu) {
        this.maDonHang = maDonHang;
        this.trangThai = trangThai;
        this.ghiChu = ghiChu;
    }

    public Long getMaDonHang() {return maDonHang;}
    public String getTrangThai() {return trangThai;}
    public String getGhiChu() {return ghiChu;}
}
