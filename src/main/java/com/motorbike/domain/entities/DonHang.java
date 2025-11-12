package com.motorbike.domain.entities;

import com.motorbike.domain.exceptions.InvalidOrderException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DonHang (Order)
 * Entity chứa logic nghiệp vụ về đơn hàng
 * Business Rule: Xử lý tính toán tổng tiền, validate đơn hàng, quản lý trạng thái
 */
public class DonHang {
    private Long maDonHang; // id
    private Long maTaiKhoan; // customer id
    private List<ChiTietDonHang> danhSachSanPham; // order items
    private BigDecimal tongTien; // total amount (calculated from items)
    private TrangThaiDonHang trangThai; // order status
    
    // Thông tin người đặt hàng
    private String tenNguoiNhan;
    private String soDienThoai;
    private String diaChiGiaoHang;
    private String ghiChu;
    
    private LocalDateTime ngayDat; // order date
    private LocalDateTime ngayCapNhat; // last updated date

    // Constructor cho đơn hàng mới
    public DonHang(Long maTaiKhoan, String tenNguoiNhan, String soDienThoai, 
                   String diaChiGiaoHang, String ghiChu) {
        validateThongTinNguoiNhan(tenNguoiNhan, soDienThoai, diaChiGiaoHang);
        
        this.maTaiKhoan = maTaiKhoan;
        this.danhSachSanPham = new ArrayList<>();
        this.tongTien = BigDecimal.ZERO;
        this.trangThai = TrangThaiDonHang.CHO_XAC_NHAN;
        this.tenNguoiNhan = tenNguoiNhan;
        this.soDienThoai = soDienThoai;
        this.diaChiGiaoHang = diaChiGiaoHang;
        this.ghiChu = ghiChu;
        this.ngayDat = LocalDateTime.now();
        this.ngayCapNhat = LocalDateTime.now();
    }

    // Constructor đầy đủ (reconstruct từ DB)
    public DonHang(Long maDonHang, Long maTaiKhoan, List<ChiTietDonHang> danhSachSanPham,
                   BigDecimal tongTien, TrangThaiDonHang trangThai,
                   String tenNguoiNhan, String soDienThoai, String diaChiGiaoHang, String ghiChu,
                   LocalDateTime ngayDat, LocalDateTime ngayCapNhat) {
        this.maDonHang = maDonHang;
        this.maTaiKhoan = maTaiKhoan;
        this.danhSachSanPham = danhSachSanPham != null ? danhSachSanPham : new ArrayList<>();
        this.tongTien = tongTien;
        this.trangThai = trangThai;
        this.tenNguoiNhan = tenNguoiNhan;
        this.soDienThoai = soDienThoai;
        this.diaChiGiaoHang = diaChiGiaoHang;
        this.ghiChu = ghiChu;
        this.ngayDat = ngayDat;
        this.ngayCapNhat = ngayCapNhat;
    }

    // Business Rule: Validate thông tin người nhận
    private void validateThongTinNguoiNhan(String tenNguoiNhan, String soDienThoai, String diaChiGiaoHang) {
        if (tenNguoiNhan == null || tenNguoiNhan.trim().isEmpty()) {
            throw new InvalidOrderException("MISSING_RECEIVER_NAME", 
                "Tên người nhận không được để trống");
        }
        if (soDienThoai == null || soDienThoai.trim().isEmpty()) {
            throw new InvalidOrderException("MISSING_PHONE", 
                "Số điện thoại không được để trống");
        }
        if (diaChiGiaoHang == null || diaChiGiaoHang.trim().isEmpty()) {
            throw new InvalidOrderException("MISSING_ADDRESS", 
                "Địa chỉ giao hàng không được để trống");
        }
    }

    // Business Rule: Thêm sản phẩm vào đơn hàng
    public void themSanPham(ChiTietDonHang chiTiet) {
        if (chiTiet == null) {
            throw new InvalidOrderException("NULL_ORDER_ITEM", 
                "Chi tiết đơn hàng không được null");
        }
        
        // Chỉ cho phép thêm sản phẩm khi đơn hàng ở trạng thái CHO_XAC_NHAN
        if (this.trangThai != TrangThaiDonHang.CHO_XAC_NHAN) {
            throw new InvalidOrderException("INVALID_ORDER_STATE", 
                "Không thể thêm sản phẩm vào đơn hàng đã được xác nhận");
        }
        
        this.danhSachSanPham.add(chiTiet);
        tinhLaiTongTien();
        this.ngayCapNhat = LocalDateTime.now();
    }

    // Business Rule: Tính tổng tiền đơn hàng (quantity × price cho mỗi item)
    public void tinhLaiTongTien() {
        this.tongTien = this.danhSachSanPham.stream()
            .map(ChiTietDonHang::getThanhTien)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Business Rule: Validate đơn hàng trước khi tạo
    public void validate() {
        if (this.danhSachSanPham == null || this.danhSachSanPham.isEmpty()) {
            throw new InvalidOrderException("EMPTY_ORDER", 
                "Đơn hàng phải có ít nhất 1 sản phẩm");
        }
        
        if (this.tongTien == null || this.tongTien.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidOrderException("INVALID_TOTAL", 
                "Tổng tiền đơn hàng phải lớn hơn 0");
        }
    }

    // Business Rule: Chuyển trạng thái đơn hàng
    public void chuyenTrangThai(TrangThaiDonHang trangThaiMoi) {
        if (trangThaiMoi == null) {
            throw new InvalidOrderException("NULL_STATUS", 
                "Trạng thái mới không được null");
        }
        
        if (!this.trangThai.coTheChuyenSang(trangThaiMoi)) {
            throw new InvalidOrderException("INVALID_STATUS_TRANSITION", 
                "Không thể chuyển từ trạng thái '" + this.trangThai.getMoTa() + 
                "' sang '" + trangThaiMoi.getMoTa() + "'");
        }
        
        this.trangThai = trangThaiMoi;
        this.ngayCapNhat = LocalDateTime.now();
    }

    // Business Rule: Hủy đơn hàng
    public void huyDonHang() {
        if (this.trangThai == TrangThaiDonHang.DA_GIAO) {
            throw new InvalidOrderException("CANNOT_CANCEL_DELIVERED", 
                "Không thể hủy đơn hàng đã giao");
        }
        
        if (this.trangThai == TrangThaiDonHang.DA_HUY) {
            throw new InvalidOrderException("ALREADY_CANCELLED", 
                "Đơn hàng đã bị hủy trước đó");
        }
        
        this.trangThai = TrangThaiDonHang.DA_HUY;
        this.ngayCapNhat = LocalDateTime.now();
    }

    // Factory method: Tạo đơn hàng từ giỏ hàng
    public static DonHang fromGioHang(GioHang gioHang, String tenNguoiNhan, 
                                      String soDienThoai, String diaChiGiaoHang, String ghiChu) {
        if (gioHang == null) {
            throw new InvalidOrderException("NULL_CART", "Giỏ hàng không được null");
        }
        
        if (gioHang.getDanhSachSanPham() == null || gioHang.getDanhSachSanPham().isEmpty()) {
            throw new InvalidOrderException("EMPTY_CART", 
                "Giỏ hàng phải có ít nhất 1 sản phẩm");
        }
        
        DonHang donHang = new DonHang(
            gioHang.getMaTaiKhoan(),
            tenNguoiNhan,
            soDienThoai,
            diaChiGiaoHang,
            ghiChu
        );
        
        // Chuyển đổi từng item từ giỏ hàng sang đơn hàng
        for (ChiTietGioHang chiTietGioHang : gioHang.getDanhSachSanPham()) {
            ChiTietDonHang chiTietDonHang = ChiTietDonHang.fromChiTietGioHang(chiTietGioHang);
            donHang.themSanPham(chiTietDonHang);
        }
        
        donHang.validate();
        return donHang;
    }

    // Business logic: Kiểm tra đơn hàng có thể chỉnh sửa không
    public boolean coTheChinhSua() {
        return this.trangThai == TrangThaiDonHang.CHO_XAC_NHAN;
    }

    // Business logic: Kiểm tra đơn hàng có thể hủy không
    public boolean coTheHuy() {
        return this.trangThai != TrangThaiDonHang.DA_GIAO 
            && this.trangThai != TrangThaiDonHang.DA_HUY;
    }

    // Getters
    public Long getMaDonHang() {
        return maDonHang;
    }

    public Long getMaTaiKhoan() {
        return maTaiKhoan;
    }

    public List<ChiTietDonHang> getDanhSachSanPham() {
        return new ArrayList<>(danhSachSanPham); // Return copy for encapsulation
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public TrangThaiDonHang getTrangThai() {
        return trangThai;
    }

    public String getTenNguoiNhan() {
        return tenNguoiNhan;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public String getDiaChiGiaoHang() {
        return diaChiGiaoHang;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public LocalDateTime getNgayDat() {
        return ngayDat;
    }

    public LocalDateTime getNgayCapNhat() {
        return ngayCapNhat;
    }

    // Setters for reconstruction from DB
    public void setMaDonHang(Long maDonHang) {
        this.maDonHang = maDonHang;
    }

    public void setDanhSachSanPham(List<ChiTietDonHang> danhSachSanPham) {
        this.danhSachSanPham = danhSachSanPham;
    }

    @Override
    public String toString() {
        return "DonHang{" +
                "maDonHang=" + maDonHang +
                ", maTaiKhoan=" + maTaiKhoan +
                ", soLuongSanPham=" + danhSachSanPham.size() +
                ", tongTien=" + tongTien +
                ", trangThai=" + trangThai +
                ", tenNguoiNhan='" + tenNguoiNhan + '\'' +
                ", soDienThoai='" + soDienThoai + '\'' +
                ", diaChiGiaoHang='" + diaChiGiaoHang + '\'' +
                ", ngayDat=" + ngayDat +
                ", ngayCapNhat=" + ngayCapNhat +
                '}';
    }
}
