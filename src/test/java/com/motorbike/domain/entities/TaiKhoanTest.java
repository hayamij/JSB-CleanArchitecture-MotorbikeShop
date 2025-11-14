package com.motorbike.domain.entities;

import com.motorbike.domain.exceptions.InvalidUserException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TaiKhoan entity
 */
class TaiKhoanTest {

    @Test
    void testCreateTaiKhoan_Success() {
        // Given
        String email = "customer@gmail.com";
        String tenDangNhap = "customer1";
        String matKhau = "password123";
        String soDienThoai = "0912345678";
        String diaChi = "Hà Nội";

        // When
        TaiKhoan taiKhoan = new TaiKhoan(email, tenDangNhap, matKhau, soDienThoai, diaChi);

        // Then
        assertNotNull(taiKhoan);
        assertEquals(email, taiKhoan.getEmail());
        assertEquals(tenDangNhap, taiKhoan.getTenDangNhap());
        assertEquals(VaiTro.CUSTOMER, taiKhoan.getVaiTro());
        assertTrue(taiKhoan.isHoatDong());
    }

    @Test
    void testCreateTaiKhoan_InvalidEmail_ThrowsException() {
        // Given & When & Then
        assertThrows(InvalidUserException.class, () -> {
            new TaiKhoan("invalid-email", "user1", "password123", "0912345678", "Hà Nội");
        });
    }

    @Test
    void testCreateTaiKhoan_EmptyUsername_ThrowsException() {
        // Given & When & Then
        assertThrows(InvalidUserException.class, () -> {
            new TaiKhoan("test@gmail.com", "", "password123", "0912345678", "Hà Nội");
        });
    }

    @Test
    void testCreateTaiKhoan_ShortPassword_ThrowsException() {
        // Given & When & Then
        assertThrows(InvalidUserException.class, () -> {
            new TaiKhoan("test@gmail.com", "user1", "12345", "0912345678", "Hà Nội");
        });
    }

    @Test
    void testCreateTaiKhoan_InvalidPhoneNumber_ThrowsException() {
        // Given & When & Then
        assertThrows(InvalidUserException.class, () -> {
            new TaiKhoan("test@gmail.com", "user1", "password123", "123", "Hà Nội");
        });
    }

    @Test
    void testValidateEmail_ValidFormats_Success() {
        // Given & When & Then
        assertDoesNotThrow(() -> TaiKhoan.validateEmail("test@gmail.com"));
        assertDoesNotThrow(() -> TaiKhoan.validateEmail("user.name@example.co.uk"));
        assertDoesNotThrow(() -> TaiKhoan.validateEmail("user+tag@domain.com"));
    }

    @Test
    void testValidateSoDienThoai_ValidFormats_Success() {
        // Given & When & Then
        assertDoesNotThrow(() -> TaiKhoan.validateSoDienThoai("0912345678"));
        assertDoesNotThrow(() -> TaiKhoan.validateSoDienThoai("+84912345678"));
        assertDoesNotThrow(() -> TaiKhoan.validateSoDienThoai("0123456789"));
    }

    @Test
    void testKiemTraMatKhau_CorrectPassword_ReturnsTrue() {
        // Given
        TaiKhoan taiKhoan = new TaiKhoan("test@gmail.com", "user1", "password123", 
                                        "0912345678", "Hà Nội");

        // When & Then
        assertTrue(taiKhoan.kiemTraMatKhau("password123"));
    }

    @Test
    void testKiemTraMatKhau_WrongPassword_ReturnsFalse() {
        // Given
        TaiKhoan taiKhoan = new TaiKhoan("test@gmail.com", "user1", "password123", 
                                        "0912345678", "Hà Nội");

        // When & Then
        assertFalse(taiKhoan.kiemTraMatKhau("wrongpassword"));
    }

    @Test
    void testCapNhatMatKhau_Success() {
        // Given
        TaiKhoan taiKhoan = new TaiKhoan("test@gmail.com", "user1", "oldpassword", 
                                        "0912345678", "Hà Nội");

        // When
        taiKhoan.capNhatMatKhau("newpassword123");

        // Then
        assertTrue(taiKhoan.kiemTraMatKhau("newpassword123"));
        assertFalse(taiKhoan.kiemTraMatKhau("oldpassword"));
    }

    @Test
    void testDangNhapThanhCong_ActiveAccount_UpdatesLastLogin() {
        // Given
        TaiKhoan taiKhoan = new TaiKhoan("test@gmail.com", "user1", "password123", 
                                        "0912345678", "Hà Nội");
        assertNull(taiKhoan.getLanDangNhapCuoi());

        // When
        taiKhoan.dangNhapThanhCong();

        // Then
        assertNotNull(taiKhoan.getLanDangNhapCuoi());
    }

    @Test
    void testDangNhapThanhCong_InactiveAccount_ThrowsException() {
        // Given
        TaiKhoan taiKhoan = new TaiKhoan("test@gmail.com", "user1", "password123", 
                                        "0912345678", "Hà Nội");
        taiKhoan.khoaTaiKhoan();

        // When & Then
        assertThrows(InvalidUserException.class, () -> {
            taiKhoan.dangNhapThanhCong();
        });
    }

    @Test
    void testKhoaTaiKhoan_Success() {
        // Given
        TaiKhoan taiKhoan = new TaiKhoan("test@gmail.com", "user1", "password123", 
                                        "0912345678", "Hà Nội");

        // When
        taiKhoan.khoaTaiKhoan();

        // Then
        assertFalse(taiKhoan.isHoatDong());
    }

    @Test
    void testMoKhoaTaiKhoan_Success() {
        // Given
        TaiKhoan taiKhoan = new TaiKhoan("test@gmail.com", "user1", "password123", 
                                        "0912345678", "Hà Nội");
        taiKhoan.khoaTaiKhoan();

        // When
        taiKhoan.moKhoaTaiKhoan();

        // Then
        assertTrue(taiKhoan.isHoatDong());
    }

    @Test
    void testThangCapAdmin_Success() {
        // Given
        TaiKhoan taiKhoan = new TaiKhoan("test@gmail.com", "user1", "password123", 
                                        "0912345678", "Hà Nội");
        assertEquals(VaiTro.CUSTOMER, taiKhoan.getVaiTro());

        // When
        taiKhoan.thangCapAdmin();

        // Then
        assertEquals(VaiTro.ADMIN, taiKhoan.getVaiTro());
        assertTrue(taiKhoan.laAdmin());
        assertFalse(taiKhoan.laCustomer());
    }

    @Test
    void testHaCapCustomer_Success() {
        // Given
        TaiKhoan taiKhoan = new TaiKhoan("admin@motorbike.com", "admin", "password123", 
                                        "0901234567", "Hà Nội");
        taiKhoan.thangCapAdmin();

        // When
        taiKhoan.haCapCustomer();

        // Then
        assertEquals(VaiTro.CUSTOMER, taiKhoan.getVaiTro());
        assertTrue(taiKhoan.laCustomer());
        assertFalse(taiKhoan.laAdmin());
    }
}
