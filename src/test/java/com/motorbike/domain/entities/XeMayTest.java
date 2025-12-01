package com.motorbike.domain.entities;

import com.motorbike.domain.exceptions.ValidationException;
import com.motorbike.domain.exceptions.DomainException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class XeMayTest {

    @Test
    void testCreateXeMay_Success() {
        String tenSanPham = "Honda Winner X";
        BigDecimal gia = BigDecimal.valueOf(46000000);
        int soLuong = 10;

        XeMay xeMay = new XeMay(tenSanPham, "Xe thể thao", gia, "/images/honda.jpg",
                                soLuong, "Honda", "Winner X", "Đỏ đen", 2025, 150);

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
        BigDecimal gia = BigDecimal.valueOf(46000000);

        assertThrows(ValidationException.class, () -> {
            new XeMay(null, "Mô tả", gia, "/images/honda.jpg", 10,
                     "Honda", "Winner X", "Đỏ", 2025, 150);
        });
    }

    @Test
    void testCreateXeMay_InvalidPrice_ThrowsException() {
        assertThrows(ValidationException.class, () -> {
            new XeMay("Honda Winner X", "Mô tả", BigDecimal.ZERO, "/images/honda.jpg", 10,
                     "Honda", "Winner X", "Đỏ", 2025, 150);
        });
    }

    @Test
    void testGiamTonKho_Success() {
        XeMay xeMay = new XeMay("Honda Winner X", "Mô tả", BigDecimal.valueOf(46000000),
                               "/images/honda.jpg", 10, "Honda", "Winner X", "Đỏ", 2025, 150);

        xeMay.giamTonKho(5);

        assertEquals(5, xeMay.getSoLuongTonKho());
        assertTrue(xeMay.coConHang());
    }

    @Test
    void testGiamTonKho_ToZero_MarksAsUnavailable() {
        XeMay xeMay = new XeMay("Honda Winner X", "Mô tả", BigDecimal.valueOf(46000000),
                               "/images/honda.jpg", 5, "Honda", "Winner X", "Đỏ", 2025, 150);

        xeMay.giamTonKho(5);

        assertEquals(0, xeMay.getSoLuongTonKho());
        assertFalse(xeMay.coConHang());
    }

    @Test
    void testGiamTonKho_InsufficientStock_ThrowsException() {
        XeMay xeMay = new XeMay("Honda Winner X", "Mô tả", BigDecimal.valueOf(46000000),
                               "/images/honda.jpg", 5, "Honda", "Winner X", "Đỏ", 2025, 150);

        assertThrows(DomainException.class, () -> {
            xeMay.giamTonKho(10);
        });
    }

    @Test
    void testTangTonKho_Success() {
        XeMay xeMay = new XeMay("Honda Winner X", "Mô tả", BigDecimal.valueOf(46000000),
                               "/images/honda.jpg", 5, "Honda", "Winner X", "Đỏ", 2025, 150);

        xeMay.tangTonKho(10);

        assertEquals(15, xeMay.getSoLuongTonKho());
        assertTrue(xeMay.coConHang());
    }

    @Test
    void testTinhGiaSauKhuyenMai_XeMoi_NoDiscount() {
        XeMay xeMay = new XeMay("Honda Winner X", "Mô tả", BigDecimal.valueOf(46000000),
                               "/images/honda.jpg", 10, "Honda", "Winner X", "Đỏ", 2025, 150);

        BigDecimal giaSauKhuyenMai = xeMay.tinhGiaSauKhuyenMai();

        assertEquals(0, BigDecimal.valueOf(46000000).compareTo(giaSauKhuyenMai));
    }

    @Test
    void testTinhGiaSauKhuyenMai_XeCu_HasDiscount() {
        XeMay xeMay = new XeMay("Honda Winner X", "Mô tả", BigDecimal.valueOf(46000000),
                               "/images/honda.jpg", 10, "Honda", "Winner X", "Đỏ", 2023, 150);

        BigDecimal giaSauKhuyenMai = xeMay.tinhGiaSauKhuyenMai();

        BigDecimal expectedPrice = BigDecimal.valueOf(46000000).multiply(BigDecimal.valueOf(0.95));
        assertEquals(0, expectedPrice.compareTo(giaSauKhuyenMai));
    }

    @Test
    void testLayThongTinChiTiet_ReturnsFormattedString() {
        XeMay xeMay = new XeMay("Honda Winner X", "Mô tả", BigDecimal.valueOf(46000000),
                               "/images/honda.jpg", 10, "Honda", "Winner X", "Đỏ đen", 2025, 150);

        String thongTin = xeMay.layThongTinChiTiet();

        assertNotNull(thongTin);
        assertTrue(thongTin.contains("Honda"));
        assertTrue(thongTin.contains("Winner X"));
        assertTrue(thongTin.contains("Đỏ đen"));
    }

    @Test
    void testLaXeMoi_ReturnsTrueForCurrentYear() {
        XeMay xeMay = new XeMay("Honda Winner X", "Mô tả", BigDecimal.valueOf(46000000),
                               "/images/honda.jpg", 10, "Honda", "Winner X", "Đỏ", 2025, 150);

        assertTrue(xeMay.laXeMoi());
    }

    @Test
    void testLaXeMoi_ReturnsFalseForOldYear() {
        XeMay xeMay = new XeMay("Honda Winner X", "Mô tả", BigDecimal.valueOf(46000000),
                               "/images/honda.jpg", 10, "Honda", "Winner X", "Đỏ", 2023, 150);

        assertFalse(xeMay.laXeMoi());
    }
}
