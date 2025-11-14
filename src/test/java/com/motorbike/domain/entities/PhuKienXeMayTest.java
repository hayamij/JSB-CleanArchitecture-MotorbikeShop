package com.motorbike.domain.entities;

import com.motorbike.domain.exceptions.InvalidProductException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PhuKienXeMay entity
 */
class PhuKienXeMayTest {

    @Test
    void testCreatePhuKien_Success() {
        // Given
        String tenSanPham = "Mũ bảo hiểm Royal M139";
        BigDecimal gia = BigDecimal.valueOf(850000);
        int soLuong = 50;

        // When
        PhuKienXeMay phuKien = new PhuKienXeMay(tenSanPham, "Mũ bảo hiểm fullface", gia, 
                                                "/images/helmet.jpg", soLuong,
                                                "Mũ bảo hiểm", "Royal", "ABS + EPS", "L");

        // Then
        assertNotNull(phuKien);
        assertEquals(tenSanPham, phuKien.getTenSanPham());
        assertEquals(gia, phuKien.getGia());
        assertEquals(soLuong, phuKien.getSoLuongTonKho());
        assertEquals("Mũ bảo hiểm", phuKien.getLoaiPhuKien());
        assertEquals("Royal", phuKien.getThuongHieu());
        assertTrue(phuKien.coConHang());
    }

    @Test
    void testCreatePhuKien_InvalidName_ThrowsException() {
        // Given
        BigDecimal gia = BigDecimal.valueOf(850000);

        // When & Then
        assertThrows(InvalidProductException.class, () -> {
            new PhuKienXeMay("", "Mô tả", gia, "/images/helmet.jpg", 50,
                            "Mũ bảo hiểm", "Royal", "ABS", "L");
        });
    }

    @Test
    void testCreatePhuKien_NegativeStock_ThrowsException() {
        // Given & When & Then
        assertThrows(InvalidProductException.class, () -> {
            new PhuKienXeMay("Mũ bảo hiểm", "Mô tả", BigDecimal.valueOf(850000), 
                            "/images/helmet.jpg", -5,
                            "Mũ bảo hiểm", "Royal", "ABS", "L");
        });
    }

    @Test
    void testGiamTonKho_Success() {
        // Given
        PhuKienXeMay phuKien = new PhuKienXeMay("Mũ bảo hiểm", "Mô tả", 
                                               BigDecimal.valueOf(850000), "/images/helmet.jpg", 50,
                                               "Mũ bảo hiểm", "Royal", "ABS", "L");

        // When
        phuKien.giamTonKho(10);

        // Then
        assertEquals(40, phuKien.getSoLuongTonKho());
        assertTrue(phuKien.coConHang());
    }

    @Test
    void testTangTonKho_RestoringFromZero() {
        // Given
        PhuKienXeMay phuKien = new PhuKienXeMay("Găng tay", "Mô tả", 
                                               BigDecimal.valueOf(450000), "/images/gloves.jpg", 5,
                                               "Găng tay", "Komine", "Da", "XL");
        phuKien.giamTonKho(5); // Reduce to 0

        // When
        phuKien.tangTonKho(20);

        // Then
        assertEquals(20, phuKien.getSoLuongTonKho());
        assertTrue(phuKien.coConHang());
    }

    @Test
    void testTinhGiaSauKhuyenMai_StockOver100_HasDiscount() {
        // Given
        PhuKienXeMay phuKien = new PhuKienXeMay("Áo mưa", "Mô tả", 
                                               BigDecimal.valueOf(250000), "/images/raincoat.jpg", 150,
                                               "Áo mưa", "Givi", "PVC", "L");

        // When
        BigDecimal giaSauKhuyenMai = phuKien.tinhGiaSauKhuyenMai();

        // Then
        BigDecimal expectedPrice = BigDecimal.valueOf(250000).multiply(BigDecimal.valueOf(0.90));
        assertEquals(0, expectedPrice.compareTo(giaSauKhuyenMai));
    }

    @Test
    void testTinhGiaSauKhuyenMai_StockUnder100_NoDiscount() {
        // Given
        PhuKienXeMay phuKien = new PhuKienXeMay("Găng tay", "Mô tả", 
                                               BigDecimal.valueOf(450000), "/images/gloves.jpg", 50,
                                               "Găng tay", "Komine", "Da", "XL");

        // When
        BigDecimal giaSauKhuyenMai = phuKien.tinhGiaSauKhuyenMai();

        // Then
        assertEquals(0, BigDecimal.valueOf(450000).compareTo(giaSauKhuyenMai));
    }

    @Test
    void testLayThongTinChiTiet_ReturnsFormattedString() {
        // Given
        PhuKienXeMay phuKien = new PhuKienXeMay("Mũ bảo hiểm Royal", "Mô tả", 
                                               BigDecimal.valueOf(850000), "/images/helmet.jpg", 50,
                                               "Mũ bảo hiểm", "Royal", "ABS + EPS", "L");

        // When
        String thongTin = phuKien.layThongTinChiTiet();

        // Then
        assertNotNull(thongTin);
        assertTrue(thongTin.contains("Mũ bảo hiểm"));
        assertTrue(thongTin.contains("Royal"));
        assertTrue(thongTin.contains("ABS + EPS"));
    }

    @Test
    void testLaPhuKienAnToan_Helmet_ReturnsTrue() {
        // Given
        PhuKienXeMay phuKien = new PhuKienXeMay("Mũ bảo hiểm Royal", "Mô tả", 
                                               BigDecimal.valueOf(850000), "/images/helmet.jpg", 50,
                                               "Mũ bảo hiểm", "Royal", "ABS", "L");

        // When & Then
        assertTrue(phuKien.laPhuKienAnToan());
    }

    @Test
    void testLaPhuKienAnToan_ProtectiveGloves_ReturnsTrue() {
        // Given
        PhuKienXeMay phuKien = new PhuKienXeMay("Găng tay bảo hộ", "Mô tả", 
                                               BigDecimal.valueOf(450000), "/images/gloves.jpg", 50,
                                               "Găng tay bảo hộ", "Komine", "Da", "XL");

        // When & Then
        assertTrue(phuKien.laPhuKienAnToan());
    }

    @Test
    void testLaPhuKienAnToan_Raincoat_ReturnsFalse() {
        // Given
        PhuKienXeMay phuKien = new PhuKienXeMay("Áo mưa", "Mô tả", 
                                               BigDecimal.valueOf(250000), "/images/raincoat.jpg", 100,
                                               "Áo mưa", "Givi", "PVC", "L");

        // When & Then
        assertFalse(phuKien.laPhuKienAnToan());
    }

    @Test
    void testCapNhatGia_Success() {
        // Given
        PhuKienXeMay phuKien = new PhuKienXeMay("Mũ bảo hiểm", "Mô tả", 
                                               BigDecimal.valueOf(850000), "/images/helmet.jpg", 50,
                                               "Mũ bảo hiểm", "Royal", "ABS", "L");

        // When
        phuKien.capNhatGia(BigDecimal.valueOf(900000));

        // Then
        assertEquals(0, BigDecimal.valueOf(900000).compareTo(phuKien.getGia()));
    }

    @Test
    void testNgungKinhDoanh_Success() {
        // Given
        PhuKienXeMay phuKien = new PhuKienXeMay("Găng tay", "Mô tả", 
                                               BigDecimal.valueOf(450000), "/images/gloves.jpg", 50,
                                               "Găng tay", "Komine", "Da", "XL");

        // When
        phuKien.ngungKinhDoanh();

        // Then
        assertFalse(phuKien.isConHang());
    }
}
