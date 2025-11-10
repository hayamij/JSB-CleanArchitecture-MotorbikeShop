package com.motorbike.main.business.entity;

import java.util.Date;

public class ListOrder extends Order {

    public ListOrder(int maDonHang, int maKhachHang, String trangThai, Date ngayDatHang) {
        this.maDonHang = maDonHang;
        this.maKhachHang = maKhachHang;
        this.trangThai = trangThai;
        this.ngayDatHang = ngayDatHang;
    }

    @Override
    public void chuyenTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return String.format("Đơn #%d | KH:%d | Ngày: %tF | Trạng thái: %s",
                maDonHang, maKhachHang, ngayDatHang, trangThai);
    }
}
