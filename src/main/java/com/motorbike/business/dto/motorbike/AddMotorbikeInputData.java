package com.motorbike.business.dto.motorbike;

import java.math.BigDecimal;

public class AddMotorbikeInputData {
    private final String tenSanPham;
    private final String moTa;
    private final BigDecimal gia;
    private final String hinhAnh;
    private final int soLuongTonKho;
    private final String hangXe;
    private final String dongXe;
    private final String mauSac;
    private final int namSanXuat;
    private final int dungTich;
    
    public AddMotorbikeInputData(String tenSanPham, String moTa, BigDecimal gia,
                                String hinhAnh, int soLuongTonKho, String hangXe,
                                String dongXe, String mauSac, int namSanXuat, int dungTich) {
        this.tenSanPham = tenSanPham;
        this.moTa = moTa;
        this.gia = gia;
        this.hinhAnh = hinhAnh;
        this.soLuongTonKho = soLuongTonKho;
        this.hangXe = hangXe;
        this.dongXe = dongXe;
        this.mauSac = mauSac;
        this.namSanXuat = namSanXuat;
        this.dungTich = dungTich;
    }
    
    public String getTenSanPham() { return tenSanPham; }
    public String getMoTa() { return moTa; }
    public BigDecimal getGia() { return gia; }
    public String getHinhAnh() { return hinhAnh; }
    public int getSoLuongTonKho() { return soLuongTonKho; }
    public String getHangXe() { return hangXe; }
    public String getDongXe() { return dongXe; }
    public String getMauSac() { return mauSac; }
    public int getNamSanXuat() { return namSanXuat; }
    public int getDungTich() { return dungTich; }
}
