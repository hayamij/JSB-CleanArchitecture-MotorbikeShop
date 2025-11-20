package com.motorbike.domain.entities;

/**
 * TrangThaiDonHang (Order Status)
 * Enum định nghĩa các trạng thái của đơn hàng
 */
public enum TrangThaiDonHang {
    CHO_XAC_NHAN("Chờ xác nhận"),      // PENDING - Đơn hàng mới tạo
    DA_XAC_NHAN("Đã xác nhận"),        // CONFIRMED - Admin đã xác nhận
    DANG_GIAO("Đang giao hàng"),       // SHIPPING - Đang vận chuyển
    DA_GIAO("Đã giao hàng"),           // DELIVERED - Giao thành công
    DA_HUY("Đã hủy");                  // CANCELLED - Đơn hàng bị hủy

    private final String moTa;

    TrangThaiDonHang(String moTa) {
        this.moTa = moTa;
    }

    public String getMoTa() {
        return moTa;
    }

    // Business Rule: Kiểm tra có thể chuyển sang trạng thái khác không
    public boolean coTheChuyenSang(TrangThaiDonHang trangThaiMoi) {
        switch (this) {
            case CHO_XAC_NHAN:
                return trangThaiMoi == DA_XAC_NHAN || trangThaiMoi == DA_HUY;
            case DA_XAC_NHAN:
                return trangThaiMoi == DANG_GIAO || trangThaiMoi == DA_HUY;
            case DANG_GIAO:
                return trangThaiMoi == DA_GIAO || trangThaiMoi == DA_HUY;
            case DA_GIAO:
                return false; // Không thể chuyển từ trạng thái cuối
            case DA_HUY:
                return false; // Không thể chuyển từ trạng thái cuối
            default:
                return false;
        }
    }

    @Override
    public String toString() {
        return this.moTa;
    }
}
