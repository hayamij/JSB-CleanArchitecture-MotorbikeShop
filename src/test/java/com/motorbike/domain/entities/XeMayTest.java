package com.motorbike.domain.entities;

import com.motorbike.domain.exceptions.InvalidProductException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for XeMay entity
 */
class XeMayTest {

    @Test
    void testCreateXeMay_Success() {
        // Given
        String tenSanPham = "Honda Winner X";
        BigDecimal gia = BigDecimal.valueOf(46000000);
        int soLuong = 10;

        // When
        XeMay xeMay = new XeMay(tenSanPham, "Xe thể thao", gia, "/images/honda.jpg", 
                                soLuong, "Honda", "Winner X", "Đỏ đen", 2025, 150);

        // Then
        assertNotNull(xeMay);
        assertEquals(tenSanPham, xeMay.getTenSanPham());
        assertEquals(gia, xeMay.getGia());
        assertEquals(soLuong, xeMay.getSoLuongTonKho());
        assertEquals("Honda", xeMay.getHangXe());
        assertEquals(150, xeMay.getDungTich());
        assertTrue(xeMay.coConHang());
    }

    @Test
    void testCreateXeMay_InvalidName_ThrowsException() {
        // Given
        BigDecimal gia = BigDecimal.valueOf(46000000);

        // When & Then
        assertThrows(InvalidProductException.class, () -> {
            new XeMay(null, "Mô tả", gia, "/images/honda.jpg", 10, 
                     "Honda", "Winner X", "Đỏ", 2025, 150);
        });
    }

    @Test
    void testCreateXeMay_InvalidPrice_ThrowsException() {
        // Given & When & Then
        assertThrows(InvalidProductException.class, () -> {
            new XeMay("Honda Winner X", "Mô tả", BigDecimal.ZERO, "/images/honda.jpg", 10,
                     "Honda", "Winner X", "Đỏ", 2025, 150);
        });
    }

    @Test
    void testGiamTonKho_Success() {
        // Given
        XeMay xeMay = new XeMay("Honda Winner X", "Mô tả", BigDecimal.valueOf(46000000), 
                               "/images/honda.jpg", 10, "Honda", "Winner X", "Đỏ", 2025, 150);

        // When
        xeMay.giamTonKho(5);

        // Then
        assertEquals(5, xeMay.getSoLuongTonKho());
        assertTrue(xeMay.coConHang());
    }

    @Test
    void testGiamTonKho_ToZero_MarksAsUnavailable() {
        // Given
        XeMay xeMay = new XeMay("Honda Winner X", "Mô tả", BigDecimal.valueOf(46000000),
                               "/images/honda.jpg", 5, "Honda", "Winner X", "Đỏ", 2025, 150);

        // When
        xeMay.giamTonKho(5);

        // Then
        assertEquals(0, xeMay.getSoLuongTonKho());
        assertFalse(xeMay.coConHang());
    }

    @Test
    void testGiamTonKho_InsufficientStock_ThrowsException() {
        // Given
        XeMay xeMay = new XeMay("Honda Winner X", "Mô tả", BigDecimal.valueOf(46000000),
                               "/images/honda.jpg", 5, "Honda", "Winner X", "Đỏ", 2025, 150);

        // When & Then
        assertThrows(InvalidProductException.class, () -> {
            xeMay.giamTonKho(10);
        });
    }

    @Test
    void testTangTonKho_Success() {
        // Given
        XeMay xeMay = new XeMay("Honda Winner X", "Mô tả", BigDecimal.valueOf(46000000),
                               "/images/honda.jpg", 5, "Honda", "Winner X", "Đỏ", 2025, 150);

        // When
        xeMay.tangTonKho(10);

        // Then
        assertEquals(15, xeMay.getSoLuongTonKho());
        assertTrue(xeMay.coConHang());
    }

    @Test
    void testTinhGiaSauKhuyenMai_XeMoi_NoDiscount() {
        // Given
        XeMay xeMay = new XeMay("Honda Winner X", "Mô tả", BigDecimal.valueOf(46000000),
                               "/images/honda.jpg", 10, "Honda", "Winner X", "Đỏ", 2025, 150);

        // When
        BigDecimal giaSauKhuyenMai = xeMay.tinhGiaSauKhuyenMai();

        // Then
        assertEquals(0, BigDecimal.valueOf(46000000).compareTo(giaSauKhuyenMai));
    }

    @Test
    void testTinhGiaSauKhuyenMai_XeCu_HasDiscount() {
        // Given
        XeMay xeMay = new XeMay("Honda Winner X", "Mô tả", BigDecimal.valueOf(46000000),
                               "/images/honda.jpg", 10, "Honda", "Winner X", "Đỏ", 2023, 150);

        // When
        BigDecimal giaSauKhuyenMai = xeMay.tinhGiaSauKhuyenMai();

        // Then
        BigDecimal expectedPrice = BigDecimal.valueOf(46000000).multiply(BigDecimal.valueOf(0.95));
        assertEquals(0, expectedPrice.compareTo(giaSauKhuyenMai));
    }

    @Test
    void testLayThongTinChiTiet_ReturnsFormattedString() {
        // Given
        XeMay xeMay = new XeMay("Honda Winner X", "Mô tả", BigDecimal.valueOf(46000000),
                               "/images/honda.jpg", 10, "Honda", "Winner X", "Đỏ đen", 2025, 150);

        // When
        String thongTin = xeMay.layThongTinChiTiet();

        // Then
        assertNotNull(thongTin);
        assertTrue(thongTin.contains("Honda"));
        assertTrue(thongTin.contains("Winner X"));
        assertTrue(thongTin.contains("Đỏ đen"));
    }

    @Test
    void testLaXeMoi_ReturnsTrueForCurrentYear() {
        // Given
        XeMay xeMay = new XeMay("Honda Winner X", "Mô tả", BigDecimal.valueOf(46000000),
                               "/images/honda.jpg", 10, "Honda", "Winner X", "Đỏ", 2025, 150);

        // When & Then
        assertTrue(xeMay.laXeMoi());
    }

    @Test
    void testLaXeMoi_ReturnsFalseForOldYear() {
        // Given
        XeMay xeMay = new XeMay("Honda Winner X", "Mô tả", BigDecimal.valueOf(46000000),
                               "/images/honda.jpg", 10, "Honda", "Winner X", "Đỏ", 2023, 150);

        // When & Then
        assertFalse(xeMay.laXeMoi());
    }
}
