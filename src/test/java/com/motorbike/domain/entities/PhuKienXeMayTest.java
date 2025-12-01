package com.motorbike.domain.entities;

import com.motorbike.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PhuKienXeMayTest {

    @Test
    void testCreatePhuKien_Success() {
        String tenSanPham = "Mũ bảo hiểm Royal M139";
        BigDecimal gia = BigDecimal.valueOf(850000);
        int soLuong = 50;

        PhuKienXeMay phuKien = new PhuKienXeMay(tenSanPham, "Mũ bảo hiểm fullface", gia,
                                                "/images/helmet.jpg", soLuong,
                                                "Mũ bảo hiểm", "Royal", "ABS + EPS", "L");

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
        BigDecimal gia = BigDecimal.valueOf(850000);

        assertThrows(ValidationException.class, () -> {
            new PhuKienXeMay("", "Mô tả", gia, "/images/helmet.jpg", 50,
                            "Mũ bảo hiểm", "Royal", "ABS", "L");
        });
    }

    @Test
    void testCreatePhuKien_NegativeStock_ThrowsException() {
        assertThrows(ValidationException.class, () -> {
            new PhuKienXeMay("Mũ bảo hiểm", "Mô tả", BigDecimal.valueOf(850000),
                            "/images/helmet.jpg", -5,
                            "Mũ bảo hiểm", "Royal", "ABS", "L");
        });
    }

    @Test
    void testGiamTonKho_Success() {
        PhuKienXeMay phuKien = new PhuKienXeMay("Mũ bảo hiểm", "Mô tả",
                                               BigDecimal.valueOf(850000), "/images/helmet.jpg", 50,
                                               "Mũ bảo hiểm", "Royal", "ABS", "L");

        phuKien.giamTonKho(10);

        assertEquals(40, phuKien.getSoLuongTonKho());
        assertTrue(phuKien.coConHang());
    }

    @Test
    void testTangTonKho_RestoringFromZero() {
        PhuKienXeMay phuKien = new PhuKienXeMay("Găng tay", "Mô tả",
                                               BigDecimal.valueOf(450000), "/images/gloves.jpg", 5,
                                               "Găng tay", "Komine", "Da", "XL");
        phuKien.giamTonKho(5);

        phuKien.tangTonKho(20);

        assertEquals(20, phuKien.getSoLuongTonKho());
        assertTrue(phuKien.coConHang());
    }

    @Test
    void testTinhGiaSauKhuyenMai_StockOver100_HasDiscount() {
        PhuKienXeMay phuKien = new PhuKienXeMay("Áo mưa", "Mô tả",
                                               BigDecimal.valueOf(250000), "/images/raincoat.jpg", 150,
                                               "Áo mưa", "Givi", "PVC", "L");

        BigDecimal giaSauKhuyenMai = phuKien.tinhGiaSauKhuyenMai();

        BigDecimal expectedPrice = BigDecimal.valueOf(250000).multiply(BigDecimal.valueOf(0.90));
        assertEquals(0, expectedPrice.compareTo(giaSauKhuyenMai));
    }

    @Test
    void testTinhGiaSauKhuyenMai_StockUnder100_NoDiscount() {
        PhuKienXeMay phuKien = new PhuKienXeMay("Găng tay", "Mô tả",
                                               BigDecimal.valueOf(450000), "/images/gloves.jpg", 50,
                                               "Găng tay", "Komine", "Da", "XL");

        BigDecimal giaSauKhuyenMai = phuKien.tinhGiaSauKhuyenMai();

        assertEquals(0, BigDecimal.valueOf(450000).compareTo(giaSauKhuyenMai));
    }

    @Test
    void testLayThongTinChiTiet_ReturnsFormattedString() {
        PhuKienXeMay phuKien = new PhuKienXeMay("Mũ bảo hiểm Royal", "Mô tả",
                                               BigDecimal.valueOf(850000), "/images/helmet.jpg", 50,
                                               "Mũ bảo hiểm", "Royal", "ABS + EPS", "L");

        String thongTin = phuKien.layThongTinChiTiet();

        assertNotNull(thongTin);
        assertTrue(thongTin.contains("Mũ bảo hiểm"));
        assertTrue(thongTin.contains("Royal"));
        assertTrue(thongTin.contains("ABS + EPS"));
    }

    @Test
    void testLaPhuKienAnToan_Helmet_ReturnsTrue() {
        PhuKienXeMay phuKien = new PhuKienXeMay("Mũ bảo hiểm Royal", "Mô tả",
                                               BigDecimal.valueOf(850000), "/images/helmet.jpg", 50,
                                               "Mũ bảo hiểm", "Royal", "ABS", "L");

        assertTrue(phuKien.laPhuKienAnToan());
    }

    @Test
    void testLaPhuKienAnToan_ProtectiveGloves_ReturnsTrue() {
        PhuKienXeMay phuKien = new PhuKienXeMay("Găng tay bảo hộ", "Mô tả",
                                               BigDecimal.valueOf(450000), "/images/gloves.jpg", 50,
                                               "Găng tay bảo hộ", "Komine", "Da", "XL");

        assertTrue(phuKien.laPhuKienAnToan());
    }

    @Test
    void testLaPhuKienAnToan_Raincoat_ReturnsFalse() {
        PhuKienXeMay phuKien = new PhuKienXeMay("Áo mưa", "Mô tả",
                                               BigDecimal.valueOf(250000), "/images/raincoat.jpg", 100,
                                               "Áo mưa", "Givi", "PVC", "L");

        assertFalse(phuKien.laPhuKienAnToan());
    }

    @Test
    void testCapNhatGia_Success() {
        PhuKienXeMay phuKien = new PhuKienXeMay("Mũ bảo hiểm", "Mô tả",
                                               BigDecimal.valueOf(850000), "/images/helmet.jpg", 50,
                                               "Mũ bảo hiểm", "Royal", "ABS", "L");

        phuKien.capNhatGia(BigDecimal.valueOf(900000));

        assertEquals(0, BigDecimal.valueOf(900000).compareTo(phuKien.getGia()));
    }

    @Test
    void testNgungKinhDoanh_Success() {
        PhuKienXeMay phuKien = new PhuKienXeMay("Găng tay", "Mô tả",
                                               BigDecimal.valueOf(450000), "/images/gloves.jpg", 50,
                                               "Găng tay", "Komine", "Da", "XL");

        phuKien.ngungKinhDoanh();

        assertFalse(phuKien.isConHang());
    }
}
