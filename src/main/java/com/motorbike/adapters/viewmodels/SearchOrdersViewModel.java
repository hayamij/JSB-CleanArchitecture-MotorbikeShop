package com.motorbike.adapters.viewmodels;

import java.math.BigDecimal;
import java.util.List;

public class SearchOrdersViewModel {
    public boolean success;
    public boolean hasError;
    public String errorCode;
    public String errorMessage;
    
    public List<OrderItem> orders;
    
    public static class OrderItem {
        public Long maDonHang;
        public Long maTaiKhoan;
        public String emailKhachHang;
        public String tenNguoiNhan;
        public String soDienThoai;
        public String diaChiGiaoHang;
        public BigDecimal tongTien;
        public String trangThai;
        public String ngayDat;
        public String ngayCapNhat;
        public Integer soMatHang;
        public List<ProductItem> sanPham;
    }

    public static class ProductItem {
        public Long maSanPham;
        public String tenSanPham;
        public BigDecimal giaBan;
        public int soLuong;
        public BigDecimal thanhTien;
    }
}
