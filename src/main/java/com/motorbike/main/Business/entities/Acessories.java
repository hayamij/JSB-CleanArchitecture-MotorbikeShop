package com.motorbike.main.Business.entities;

public class Acessories extends Products {
    private String loaiPhuKien;
    private String thuongHieu;

    public Acessories(int maSanPham, String tenSanPham, double gia, String loaiPhuKien, String thuongHieu) {
        super(maSanPham, tenSanPham, gia);
        this.loaiPhuKien = loaiPhuKien;
        this.thuongHieu = thuongHieu;
    }

    @Override
    public void hienThiThongTin() {
        System.out.println("Mã phụ kiện: " + maSanPham);
        System.out.println("Tên phụ kiện: " + tenSanPham);
        System.out.println("Giá: " + gia);
        System.out.println("Loại phụ kiện: " + loaiPhuKien);
        System.out.println("Thương hiệu: " + thuongHieu);
    }

    @Override
    public double tinhGiaBan() {
        return gia*soLuong; // Giá bán chính là giá gốc cho phụ kiện
    }
}
