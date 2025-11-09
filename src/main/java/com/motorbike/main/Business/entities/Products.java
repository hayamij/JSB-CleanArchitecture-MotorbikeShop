package com.motorbike.main.Business.entities;

public abstract class Products {
    protected int maSanPham;
    protected String tenSanPham;
    protected double gia;
    protected int soLuong;
    protected String moTa;

    public Products(int maSanPham, String tenSanPham, double gia) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.gia = gia;
    }

    public abstract void hienThiThongTin();
    public abstract double tinhGiaBan();

}
