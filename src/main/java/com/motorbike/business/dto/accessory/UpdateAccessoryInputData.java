package com.motorbike.business.dto.accessory;

import java.math.BigDecimal;

public class UpdateAccessoryInputData {
    private final Long maSanPham;
    private final String tenSanPham;
    private final String moTa;
    private final BigDecimal gia;
    private final String hinhAnh;
    private final int soLuongTonKho;
    private final String loaiPhuKien;
    private final String thuongHieu;
    private final String chatLieu;
    private final String kichThuoc;
    
    public UpdateAccessoryInputData(Long maSanPham, String tenSanPham, String moTa,
                                   BigDecimal gia, String hinhAnh, int soLuongTonKho,
                                   String loaiPhuKien, String thuongHieu, String chatLieu,
                                   String kichThuoc) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.moTa = moTa;
        this.gia = gia;
        this.hinhAnh = hinhAnh;
        this.soLuongTonKho = soLuongTonKho;
        this.loaiPhuKien = loaiPhuKien;
        this.thuongHieu = thuongHieu;
        this.chatLieu = chatLieu;
        this.kichThuoc = kichThuoc;
    }
    
    public Long getMaSanPham() { return maSanPham; }
    public String getTenSanPham() { return tenSanPham; }
    public String getMoTa() { return moTa; }
    public BigDecimal getGia() { return gia; }
    public String getHinhAnh() { return hinhAnh; }
    public int getSoLuongTonKho() { return soLuongTonKho; }
    public String getLoaiPhuKien() { return loaiPhuKien; }
    public String getThuongHieu() { return thuongHieu; }
    public String getChatLieu() { return chatLieu; }
    public String getKichThuoc() { return kichThuoc; }
}
