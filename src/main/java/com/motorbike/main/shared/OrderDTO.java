package com.motorbike.main.shared;

import java.time.LocalDate;

public class OrderDTO {
    private final int maDonHang;
    private final int maKhachHang;
    private final String trangThai;
    private final LocalDate ngayDatHang;

    public OrderDTO(int maDonHang, int maKhachHang, String trangThai, LocalDate ngayDatHang) {
        this.maDonHang = maDonHang;
        this.maKhachHang = maKhachHang;
        this.trangThai = trangThai;
        this.ngayDatHang = ngayDatHang;
    }

    public int getMaDonHang() {
        return maDonHang;
    }

    public int getMaKhachHang() {
        return maKhachHang;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public LocalDate getNgayDatHang() {
        return ngayDatHang;
    }
    @Override
public String toString() {
    return "OrderDTO{" +
            "maDonHang=" + maDonHang +
            ", maKhachHang=" + maKhachHang +
            ", trangThai='" + trangThai + '\'' +
            ", ngayDatHang=" + ngayDatHang +
            '}';
}
}
