package com.motorbike.business.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class SearchOrdersOutputData {
    private final boolean success;
    private final String errorCode;
    private final String errorMessage;
    private final List<OrderItem> orders;

    public SearchOrdersOutputData(List<OrderItem> orders) {
        this.success = true;
        this.errorCode = null;
        this.errorMessage = null;
        this.orders = orders;
    }

    public SearchOrdersOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.orders = null;
    }

    public static class OrderItem {
        private final Long maDonHang;
        private final Long maTaiKhoan;
        private final String emailKhachHang;
        private final String tenNguoiNhan;
        private final String soDienThoai;
        private final String diaChiGiaoHang;
        private final BigDecimal tongTien;
        private final String trangThai;
        private final LocalDateTime ngayDat;
        private final LocalDateTime ngayCapNhat;

        public OrderItem(Long maDonHang, Long maTaiKhoan, String emailKhachHang,
                        String tenNguoiNhan, String soDienThoai, String diaChiGiaoHang,
                        BigDecimal tongTien, String trangThai,
                        LocalDateTime ngayDat, LocalDateTime ngayCapNhat) {
            this.maDonHang = maDonHang;
            this.maTaiKhoan = maTaiKhoan;
            this.emailKhachHang = emailKhachHang;
            this.tenNguoiNhan = tenNguoiNhan;
            this.soDienThoai = soDienThoai;
            this.diaChiGiaoHang = diaChiGiaoHang;
            this.tongTien = tongTien;
            this.trangThai = trangThai;
            this.ngayDat = ngayDat;
            this.ngayCapNhat = ngayCapNhat;
        }

        public Long getMaDonHang() {return maDonHang;}
        public Long getMaTaiKhoan() {return maTaiKhoan;}
        public String getEmailKhachHang() {return emailKhachHang;}
        public String getTenNguoiNhan() {return tenNguoiNhan;}
        public String getSoDienThoai() {return soDienThoai;}
        public String getDiaChiGiaoHang() {return diaChiGiaoHang;}
        public BigDecimal getTongTien() {return tongTien;}
        public String getTrangThai() {return trangThai;}
        public LocalDateTime getNgayDat() {return ngayDat;}
        public LocalDateTime getNgayCapNhat() {return ngayCapNhat;}
    }

    public boolean isSuccess() {return success;}
    public String getErrorCode() {return errorCode;}
    public String getErrorMessage() {return errorMessage;}
    public List<OrderItem> getOrders() {return orders;}
}
