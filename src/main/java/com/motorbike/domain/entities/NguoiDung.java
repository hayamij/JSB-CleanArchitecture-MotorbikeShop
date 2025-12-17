package com.motorbike.domain.entities;

/**
 * Type alias cho TaiKhoan (User account).
 * Sử dụng để maintain backward compatibility với code cũ dùng tên tiếng Việt.
 */
public class NguoiDung extends TaiKhoan {
    
    public NguoiDung(String hoTen, String email, String tenDangNhap, String matKhau,
                     String soDienThoai, String diaChi) {
        super(hoTen, email, tenDangNhap, matKhau, soDienThoai, diaChi);
    }
    
    public NguoiDung(Long maTaiKhoan, String hoTen, String email, String tenDangNhap, String matKhau,
                     String soDienThoai, String diaChi, VaiTro vaiTro, boolean hoatDong,
                     java.time.LocalDateTime ngayTao, java.time.LocalDateTime ngayCapNhat, 
                     java.time.LocalDateTime lanDangNhapCuoi) {
        super(maTaiKhoan, hoTen, email, tenDangNhap, matKhau, soDienThoai, diaChi, 
              vaiTro, hoatDong, ngayTao, ngayCapNhat, lanDangNhapCuoi);
    }
}
