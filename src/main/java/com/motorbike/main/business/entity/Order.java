package com.motorbike.main.business.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class Order {
    protected int maDonHang;
    protected int maKhachHang;
    protected Date ngayDatHang;
    protected String trangThai;
    protected List<String> danhSachSanPham = new ArrayList<>();

    public abstract void chuyenTrangThai(String trangThai);

    public int getMaDonHang() { return maDonHang; }
    public int getMaKhachHang() { return maKhachHang; }
    public Date getNgayDatHang() { return ngayDatHang; }
    public String getTrangThai() { return trangThai; }
    public List<String> getDanhSachSanPham() { return danhSachSanPham; }
}

