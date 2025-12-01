package com.motorbike.domain.entities;

import com.motorbike.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaiKhoanTest {

    @Test
    void testCreateTaiKhoan_Success() {
        String email = "customer@gmail.com";
        String tenDangNhap = "customer1";
        String matKhau = "password123";
        String soDienThoai = "0912345678";
        String diaChi = "Hà Nội";

        TaiKhoan taiKhoan = new TaiKhoan(email, tenDangNhap, matKhau, soDienThoai, diaChi);

        assertNotNull(taiKhoan);
        assertEquals(email, taiKhoan.getEmail());
        assertEquals(tenDangNhap, taiKhoan.getTenDangNhap());
        assertEquals(VaiTro.CUSTOMER, taiKhoan.getVaiTro());
        assertTrue(taiKhoan.isHoatDong());
    }

    @Test
    void testCreateTaiKhoan_InvalidEmail_ThrowsException() {
        assertThrows(ValidationException.class, () -> {
            new TaiKhoan("invalid-email", "user1", "password123", "0912345678", "Hà Nội");
        });
    }

    @Test
    void testCreateTaiKhoan_EmptyUsername_ThrowsException() {
        assertThrows(ValidationException.class, () -> {
            new TaiKhoan("test@gmail.com", "", "password123", "0912345678", "Hà Nội");
        });
    }

    @Test
    void testCreateTaiKhoan_ShortPassword_ThrowsException() {
        assertThrows(ValidationException.class, () -> {
            new TaiKhoan("test@gmail.com", "user1", "12345", "0912345678", "Hà Nội");
        });
    }

    @Test
    void testCreateTaiKhoan_InvalidPhoneNumber_ThrowsException() {
        assertThrows(ValidationException.class, () -> {
            new TaiKhoan("test@gmail.com", "user1", "password123", "123", "Hà Nội");
        });
    }

    @Test
    void testValidateEmail_ValidFormats_Success() {
        assertDoesNotThrow(() -> TaiKhoan.validateEmail("test@gmail.com"));
        assertDoesNotThrow(() -> TaiKhoan.validateEmail("user.name@example.co.uk"));
        assertDoesNotThrow(() -> TaiKhoan.validateEmail("user+tag@domain.com"));
    }

    @Test
    void testValidateSoDienThoai_ValidFormats_Success() {
        assertDoesNotThrow(() -> TaiKhoan.validateSoDienThoai("0912345678"));
        assertDoesNotThrow(() -> TaiKhoan.validateSoDienThoai("+84912345678"));
        assertDoesNotThrow(() -> TaiKhoan.validateSoDienThoai("0123456789"));
    }

    @Test
    void testKiemTraMatKhau_CorrectPassword_ReturnsTrue() {
        TaiKhoan taiKhoan = new TaiKhoan("test@gmail.com", "user1", "password123",
                                        "0912345678", "Hà Nội");

        assertTrue(taiKhoan.kiemTraMatKhau("password123"));
    }

    @Test
    void testKiemTraMatKhau_WrongPassword_ReturnsFalse() {
        TaiKhoan taiKhoan = new TaiKhoan("test@gmail.com", "user1", "password123",
                                        "0912345678", "Hà Nội");

        assertFalse(taiKhoan.kiemTraMatKhau("wrongpassword"));
    }

    @Test
    void testCapNhatMatKhau_Success() {
        TaiKhoan taiKhoan = new TaiKhoan("test@gmail.com", "user1", "oldpassword",
                                        "0912345678", "Hà Nội");

        taiKhoan.capNhatMatKhau("newpassword123");

        assertTrue(taiKhoan.kiemTraMatKhau("newpassword123"));
        assertFalse(taiKhoan.kiemTraMatKhau("oldpassword"));
    }

    @Test
    void testDangNhapThanhCong_ActiveAccount_UpdatesLastLogin() {
        TaiKhoan taiKhoan = new TaiKhoan("test@gmail.com", "user1", "password123",
                                        "0912345678", "Hà Nội");
        assertNull(taiKhoan.getLanDangNhapCuoi());

        taiKhoan.dangNhapThanhCong();

        assertNotNull(taiKhoan.getLanDangNhapCuoi());
    }

    @Test
    void testDangNhapThanhCong_InactiveAccount_ThrowsException() {
        TaiKhoan taiKhoan = new TaiKhoan("test@gmail.com", "user1", "password123",
                                        "0912345678", "Hà Nội");
        taiKhoan.khoaTaiKhoan();

        assertThrows(ValidationException.class, () -> {
            taiKhoan.dangNhapThanhCong();
        });
    }

    @Test
    void testKhoaTaiKhoan_Success() {
        TaiKhoan taiKhoan = new TaiKhoan("test@gmail.com", "user1", "password123",
                                        "0912345678", "Hà Nội");

        taiKhoan.khoaTaiKhoan();

        assertFalse(taiKhoan.isHoatDong());
    }

    @Test
    void testMoKhoaTaiKhoan_Success() {
        TaiKhoan taiKhoan = new TaiKhoan("test@gmail.com", "user1", "password123",
                                        "0912345678", "Hà Nội");
        taiKhoan.khoaTaiKhoan();

        taiKhoan.moKhoaTaiKhoan();

        assertTrue(taiKhoan.isHoatDong());
    }

    @Test
    void testThangCapAdmin_Success() {
        TaiKhoan taiKhoan = new TaiKhoan("test@gmail.com", "user1", "password123",
                                        "0912345678", "Hà Nội");
        assertEquals(VaiTro.CUSTOMER, taiKhoan.getVaiTro());

        taiKhoan.thangCapAdmin();

        assertEquals(VaiTro.ADMIN, taiKhoan.getVaiTro());
        assertTrue(taiKhoan.laAdmin());
        assertFalse(taiKhoan.laCustomer());
    }

    @Test
    void testHaCapCustomer_Success() {
        TaiKhoan taiKhoan = new TaiKhoan("admin@motorbike.com", "admin", "password123",
                                        "0901234567", "Hà Nội");
        taiKhoan.thangCapAdmin();

        taiKhoan.haCapCustomer();

        assertEquals(VaiTro.CUSTOMER, taiKhoan.getVaiTro());
        assertTrue(taiKhoan.laCustomer());
        assertFalse(taiKhoan.laAdmin());
    }
}
