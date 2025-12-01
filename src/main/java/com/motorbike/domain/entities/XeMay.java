package com.motorbike.domain.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class XeMay extends SanPham {
    private String hangXe;
    private String dongXe;
    private String mauSac;
    private int namSanXuat;
    private int dungTich;

    public XeMay(String tenSanPham, String moTa, BigDecimal gia,
                 String hinhAnh, int soLuongTonKho,
                 String hangXe, String dongXe, String mauSac,
                 int namSanXuat, int dungTich) {
        super(tenSanPham, moTa, gia, hinhAnh, soLuongTonKho);
        this.hangXe = hangXe;
        this.dongXe = dongXe;
        this.mauSac = mauSac;
        this.namSanXuat = namSanXuat;
        this.dungTich = dungTich;
    }

    public XeMay(Long maSanPham, String tenSanPham, String moTa, BigDecimal gia,
                 String hinhAnh, int soLuongTonKho, boolean conHang,
                 LocalDateTime ngayTao, LocalDateTime ngayCapNhat,
                 String hangXe, String dongXe, String mauSac,
                 int namSanXuat, int dungTich) {
        super(maSanPham, tenSanPham, moTa, gia, hinhAnh, soLuongTonKho,
              conHang, ngayTao, ngayCapNhat);
        this.hangXe = hangXe;
        this.dongXe = dongXe;
        this.mauSac = mauSac;
        this.namSanXuat = namSanXuat;
        this.dungTich = dungTich;
    }

    @Override
    public BigDecimal tinhGiaSauKhuyenMai() {
        int namHienTai = LocalDateTime.now().getYear();
        if (namHienTai - this.namSanXuat > 1) {
            return this.gia.multiply(BigDecimal.valueOf(0.95));
        }
        return this.gia;
    }

    @Override
    public String layThongTinChiTiet() {
        return String.format(
            "Xe máy: %s %s\n" +
            "Màu sắc: %s\n" +
            "Năm sản xuất: %d\n" +
            "Dung tích: %d cc\n" +
            "Giá: %s VND\n" +
            "Tồn kho: %d",
            hangXe, dongXe, mauSac, namSanXuat, dungTich,
            gia.toString(), soLuongTonKho
        );
    }

    public boolean laXeMoi() {int namHienTai = LocalDateTime.now().getYear(); return namHienTai == this.namSanXuat;}
    public String getHangXe() {return hangXe;}
    public String getDongXe() {return dongXe;}
    public String getMauSac() {return mauSac;}
    public int getNamSanXuat() {return namSanXuat;}
    public int getDungTich() {return dungTich;}
    public void setHangXe(String hangXe) {this.hangXe = hangXe; this.ngayCapNhat = LocalDateTime.now();}
    public void setDongXe(String dongXe) {this.dongXe = dongXe; this.ngayCapNhat = LocalDateTime.now();}
    public void setMauSac(String mauSac) {this.mauSac = mauSac; this.ngayCapNhat = LocalDateTime.now();}
    public void setNamSanXuat(int namSanXuat) {this.namSanXuat = namSanXuat; this.ngayCapNhat = LocalDateTime.now();}
    public void setDungTich(int dungTich) {this.dungTich = dungTich; this.ngayCapNhat = LocalDateTime.now();}
}
