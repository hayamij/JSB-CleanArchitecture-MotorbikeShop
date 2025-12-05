package com.motorbike.business.dto.user;

import com.motorbike.domain.entities.VaiTro;
import java.time.LocalDateTime;
import java.util.List;

public class GetAllUsersOutputData {

    private final boolean success;
    private final String errorCode;
    private final String errorMessage;
    private final List<UserItem> users;

    public GetAllUsersOutputData(List<UserItem> users) {
        this.success = true;
        this.errorCode = null;
        this.errorMessage = null;
        this.users = users;
    }

    public GetAllUsersOutputData(String errorCode, String errorMessage) {
        this.success = false;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.users = java.util.Collections.emptyList();
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public List<UserItem> getUsers() {
        return users;
    }

    // ================== INNER CLASS ==================

    public static class UserItem {
        private final Long maTaiKhoan;
        private final String email;
        private final String tenDangNhap;
        private final String soDienThoai;
        private final String diaChi;
        private final VaiTro vaiTro;
        private final boolean hoatDong;
        private final LocalDateTime ngayTao;
        private final LocalDateTime ngayCapNhat;
        private final LocalDateTime lanDangNhapCuoi;

        public UserItem(Long maTaiKhoan,
                       String email,
                       String tenDangNhap,
                       String soDienThoai,
                       String diaChi,
                       VaiTro vaiTro,
                       boolean hoatDong,
                       LocalDateTime ngayTao,
                       LocalDateTime ngayCapNhat,
                       LocalDateTime lanDangNhapCuoi) {
            this.maTaiKhoan = maTaiKhoan;
            this.email = email;
            this.tenDangNhap = tenDangNhap;
            this.soDienThoai = soDienThoai;
            this.diaChi = diaChi;
            this.vaiTro = vaiTro;
            this.hoatDong = hoatDong;
            this.ngayTao = ngayTao;
            this.ngayCapNhat = ngayCapNhat;
            this.lanDangNhapCuoi = lanDangNhapCuoi;
        }

        public Long getMaTaiKhoan() { return maTaiKhoan; }
        public String getEmail() { return email; }
        public String getTenDangNhap() { return tenDangNhap; }
        public String getSoDienThoai() { return soDienThoai; }
        public String getDiaChi() { return diaChi; }
        public VaiTro getVaiTro() { return vaiTro; }
        public boolean isHoatDong() { return hoatDong; }
        public LocalDateTime getNgayTao() { return ngayTao; }
        public LocalDateTime getNgayCapNhat() { return ngayCapNhat; }
        public LocalDateTime getLanDangNhapCuoi() { return lanDangNhapCuoi; }
    }
}
