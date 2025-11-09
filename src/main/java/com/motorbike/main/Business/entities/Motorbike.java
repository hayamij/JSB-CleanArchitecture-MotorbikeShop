package com.motorbike.main.Business.entities;

public class Motorbike extends Products{
    private String thuongHieu;
    private String dongXe;
    private String mauXe;

    public Motorbike(int maSanPham, String tenSanPham, double gia, String thuongHieu, String dongXe, String mauXe) {
        super(maSanPham, tenSanPham, gia);
        this.thuongHieu = thuongHieu;
        this.dongXe = dongXe;
        this.mauXe = mauXe;
    }

    @Override 
    public void hienThiThongTin() {
        System.out.println("Mã phụ kiện: " + maSanPham);
        System.out.println("Tên phụ kiện: " + tenSanPham);
        System.out.println("Giá: " + gia);
        System.out.println("Thương hiệu: " + thuongHieu);
        System.out.println("Dòng xe: " + dongXe);
        System.out.println("Màu xe: " + mauXe);
    }
    @Override
    public double tinhGiaBan() {
        return gia*soLuong;
    }
    
}
