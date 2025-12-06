package com.motorbike.domain.entities;

import com.motorbike.domain.exceptions.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DonHang {
    private Long maDonHang;
    private Long maTaiKhoan;
    private List<ChiTietDonHang> danhSachSanPham;
    private BigDecimal tongTien;
    private TrangThaiDonHang trangThai;
    
    private String tenNguoiNhan;
    private String soDienThoai;
    private String diaChiGiaoHang;
    private String ghiChu;
    private PhuongThucThanhToan phuongThucThanhToan;
    
    private LocalDateTime ngayDat;
    private LocalDateTime ngayCapNhat;

    public DonHang(Long maTaiKhoan, String tenNguoiNhan, String soDienThoai,
                   String diaChiGiaoHang, String ghiChu, PhuongThucThanhToan phuongThucThanhToan) {
        validateThongTinNguoiNhan(tenNguoiNhan, soDienThoai, diaChiGiaoHang);
        
        this.maTaiKhoan = maTaiKhoan;
        this.danhSachSanPham = new ArrayList<>();
        this.tongTien = BigDecimal.ZERO;
        this.trangThai = TrangThaiDonHang.CHO_XAC_NHAN;
        this.tenNguoiNhan = tenNguoiNhan;
        this.soDienThoai = soDienThoai;
        this.diaChiGiaoHang = diaChiGiaoHang;
        this.ghiChu = ghiChu;
        this.phuongThucThanhToan = phuongThucThanhToan != null ? phuongThucThanhToan : PhuongThucThanhToan.THANH_TOAN_TRUC_TIEP;
        this.ngayDat = LocalDateTime.now();
        this.ngayCapNhat = LocalDateTime.now();
    }

    public DonHang(Long maDonHang, Long maTaiKhoan, List<ChiTietDonHang> danhSachSanPham,
                   BigDecimal tongTien, TrangThaiDonHang trangThai,
                   String tenNguoiNhan, String soDienThoai, String diaChiGiaoHang, String ghiChu,
                   PhuongThucThanhToan phuongThucThanhToan,
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
        this.phuongThucThanhToan = phuongThucThanhToan;
        this.ngayDat = ngayDat;
        this.ngayCapNhat = ngayCapNhat;
    }

    private void validateThongTinNguoiNhan(String tenNguoiNhan, String soDienThoai, String diaChiGiaoHang) {
        if (tenNguoiNhan == null || tenNguoiNhan.trim().isEmpty()) {
            throw ValidationException.missingReceiverName();
        }
        if (soDienThoai == null || soDienThoai.trim().isEmpty()) {
            throw ValidationException.missingPhone();
        }
        if (diaChiGiaoHang == null || diaChiGiaoHang.trim().isEmpty()) {
            throw ValidationException.missingAddress();
        }
    }

    public static void checkInput(Long userId, String tenNguoiNhan, String soDienThoai, String diaChiGiaoHang) {
        if (userId == null) {
            throw ValidationException.invalidUserId();
        }
        if (tenNguoiNhan == null || tenNguoiNhan.trim().isEmpty()) {
            throw ValidationException.missingReceiverName();
        }
        if (soDienThoai == null || soDienThoai.trim().isEmpty()) {
            throw ValidationException.missingPhone();
        }
        if (diaChiGiaoHang == null || diaChiGiaoHang.trim().isEmpty()) {
            throw ValidationException.missingAddress();
        }
    }

    public void themSanPham(ChiTietDonHang chiTiet) {
        if (chiTiet == null) {
            throw ValidationException.nullOrderItem();
        }
        
        if (this.trangThai != TrangThaiDonHang.CHO_XAC_NHAN) {
            throw DomainException.invalidOrderState();
        }
        
        this.danhSachSanPham.add(chiTiet);
        tinhLaiTongTien();
        this.ngayCapNhat = LocalDateTime.now();
    }

    public void tinhLaiTongTien() {
        this.tongTien = this.danhSachSanPham.stream()
            .map(ChiTietDonHang::getThanhTien)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void validate() {
        if (this.danhSachSanPham == null || this.danhSachSanPham.isEmpty()) {
            throw DomainException.emptyOrder();
        }
        
        if (this.tongTien == null || this.tongTien.compareTo(BigDecimal.ZERO) <= 0) {
            throw DomainException.invalidTotal();
        }
    }

    public void chuyenTrangThai(TrangThaiDonHang trangThaiMoi) {
        if (trangThaiMoi == null) {
            throw ValidationException.nullStatus();
        }
        
        if (!this.trangThai.coTheChuyenSang(trangThaiMoi)) {
            throw DomainException.invalidStatusTransition(
                this.trangThai.getMoTa(),
                trangThaiMoi.getMoTa()
            );
        }
        
        this.trangThai = trangThaiMoi;
        this.ngayCapNhat = LocalDateTime.now();
    }

    public void huyDonHang() {
        if (this.trangThai == TrangThaiDonHang.DA_GIAO) {
            throw DomainException.cannotCancelDelivered();
        }
        
        if (this.trangThai == TrangThaiDonHang.DA_HUY) {
            throw DomainException.alreadyCancelled();
        }
        
        this.trangThai = TrangThaiDonHang.DA_HUY;
        this.ngayCapNhat = LocalDateTime.now();
    }

    public static DonHang fromGioHang(GioHang gioHang, String tenNguoiNhan,
                                      String soDienThoai, String diaChiGiaoHang, String ghiChu) {
        return fromGioHang(gioHang, tenNguoiNhan, soDienThoai, diaChiGiaoHang, ghiChu, 
                          PhuongThucThanhToan.THANH_TOAN_TRUC_TIEP);
    }

    public static DonHang fromGioHang(GioHang gioHang, String tenNguoiNhan,
                                      String soDienThoai, String diaChiGiaoHang, String ghiChu,
                                      PhuongThucThanhToan phuongThucThanhToan) {
        if (gioHang == null) {
            throw ValidationException.nullCart();
        }
        
        if (gioHang.getDanhSachSanPham() == null || gioHang.getDanhSachSanPham().isEmpty()) {
            throw DomainException.emptyCart();
        }
        
        DonHang donHang = new DonHang(
            gioHang.getMaTaiKhoan(),
            tenNguoiNhan,
            soDienThoai,
            diaChiGiaoHang,
            ghiChu,
            phuongThucThanhToan != null ? phuongThucThanhToan : PhuongThucThanhToan.THANH_TOAN_TRUC_TIEP
        );
        
        for (ChiTietGioHang chiTietGioHang : gioHang.getDanhSachSanPham()) {
            ChiTietDonHang chiTietDonHang = ChiTietDonHang.fromChiTietGioHang(chiTietGioHang);
            donHang.themSanPham(chiTietDonHang);
        }
        
        donHang.validate();
        return donHang;
    }

    public boolean coTheChinhSua() {return this.trangThai == TrangThaiDonHang.CHO_XAC_NHAN;}
    public boolean coTheHuy() {return this.trangThai != TrangThaiDonHang.DA_GIAO && this.trangThai != TrangThaiDonHang.DA_HUY;}

    public Long getMaDonHang() {return maDonHang;}
    public Long getMaTaiKhoan() {return maTaiKhoan;}
    public List<ChiTietDonHang> getDanhSachSanPham() {return new ArrayList<>(danhSachSanPham);}
    public BigDecimal getTongTien() {return tongTien;}
    public TrangThaiDonHang getTrangThai() {return trangThai;}
    public String getTenNguoiNhan() {return tenNguoiNhan;}
    public String getSoDienThoai() {return soDienThoai;}
    public String getDiaChiGiaoHang() {return diaChiGiaoHang;}
    public String getGhiChu() {return ghiChu;}
    public PhuongThucThanhToan getPhuongThucThanhToan() {return phuongThucThanhToan;}
    public LocalDateTime getNgayDat() {return ngayDat;}
    public LocalDateTime getNgayCapNhat() {return ngayCapNhat;}
    public void setMaDonHang(Long maDonHang) {this.maDonHang = maDonHang;}
    public void setDanhSachSanPham(List<ChiTietDonHang> danhSachSanPham) {this.danhSachSanPham = danhSachSanPham;}
    public void setPhuongThucThanhToan(PhuongThucThanhToan phuongThucThanhToan) {this.phuongThucThanhToan = phuongThucThanhToan;}

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
