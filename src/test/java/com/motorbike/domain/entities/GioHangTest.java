package com.motorbike.domain.entities;

import com.motorbike.domain.exceptions.InvalidCartException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class GioHangTest {

    @Test
    void testCreateGioHang_Success() {
        Long maTaiKhoan = 1L;

        GioHang gioHang = new GioHang(maTaiKhoan);

        assertNotNull(gioHang);
        assertEquals(maTaiKhoan, gioHang.getMaTaiKhoan());
        assertTrue(gioHang.coTrong());
        assertEquals(BigDecimal.ZERO, gioHang.getTongTien());
    }

    @Test
    void testThemSanPham_NewProduct_Success() {
        GioHang gioHang = new GioHang(1L);
        ChiTietGioHang chiTiet = new ChiTietGioHang(1L, "Honda Winner X",
                                                   BigDecimal.valueOf(46000000), 1);

        gioHang.themSanPham(chiTiet);

        assertEquals(1, gioHang.demSoSanPham());
        assertEquals(1, gioHang.tongSoLuong());
        assertEquals(0, BigDecimal.valueOf(46000000).compareTo(gioHang.getTongTien()));
    }

    @Test
    void testThemSanPham_ExistingProduct_IncreasesQuantity() {
        GioHang gioHang = new GioHang(1L);
        ChiTietGioHang chiTiet1 = new ChiTietGioHang(1L, "Honda Winner X",
                                                    BigDecimal.valueOf(46000000), 1);
        ChiTietGioHang chiTiet2 = new ChiTietGioHang(1L, "Honda Winner X",
                                                    BigDecimal.valueOf(46000000), 2);

        gioHang.themSanPham(chiTiet1);
        gioHang.themSanPham(chiTiet2);

        assertEquals(1, gioHang.demSoSanPham());
        assertEquals(3, gioHang.tongSoLuong());
        assertEquals(0, BigDecimal.valueOf(138000000).compareTo(gioHang.getTongTien()));
    }

    @Test
    void testThemSanPham_MultipleProducts_Success() {
        GioHang gioHang = new GioHang(1L);
        ChiTietGioHang chiTiet1 = new ChiTietGioHang(1L, "Honda Winner X",
                                                    BigDecimal.valueOf(46000000), 1);
        ChiTietGioHang chiTiet2 = new ChiTietGioHang(2L, "Mũ bảo hiểm",
                                                    BigDecimal.valueOf(850000), 2);

        gioHang.themSanPham(chiTiet1);
        gioHang.themSanPham(chiTiet2);

        assertEquals(2, gioHang.demSoSanPham());
        assertEquals(3, gioHang.tongSoLuong());
        BigDecimal expectedTotal = BigDecimal.valueOf(46000000).add(BigDecimal.valueOf(1700000));
        assertEquals(0, expectedTotal.compareTo(gioHang.getTongTien()));
    }

    @Test
    void testThemSanPham_NullItem_ThrowsException() {
        GioHang gioHang = new GioHang(1L);

        assertThrows(InvalidCartException.class, () -> {
            gioHang.themSanPham(null);
        });
    }

    @Test
    void testXoaSanPham_Success() {
        GioHang gioHang = new GioHang(1L);
        ChiTietGioHang chiTiet = new ChiTietGioHang(1L, "Honda Winner X",
                                                   BigDecimal.valueOf(46000000), 1);
        gioHang.themSanPham(chiTiet);

        gioHang.xoaSanPham(1L);

        assertTrue(gioHang.coTrong());
        assertEquals(BigDecimal.ZERO, gioHang.getTongTien());
    }

    @Test
    void testXoaSanPham_NonExistentProduct_ThrowsException() {
        GioHang gioHang = new GioHang(1L);
        ChiTietGioHang chiTiet = new ChiTietGioHang(1L, "Honda Winner X",
                                                   BigDecimal.valueOf(46000000), 1);
        gioHang.themSanPham(chiTiet);

        assertThrows(com.motorbike.domain.exceptions.ProductNotInCartException.class,
            () -> gioHang.xoaSanPham(999L));
    }

    @Test
    void testCapNhatSoLuong_Success() {
        GioHang gioHang = new GioHang(1L);
        ChiTietGioHang chiTiet = new ChiTietGioHang(1L, "Honda Winner X",
                                                   BigDecimal.valueOf(46000000), 1);
        gioHang.themSanPham(chiTiet);

        gioHang.capNhatSoLuong(1L, 3);

        assertEquals(1, gioHang.demSoSanPham());
        assertEquals(3, gioHang.tongSoLuong());
        assertEquals(0, BigDecimal.valueOf(138000000).compareTo(gioHang.getTongTien()));
    }

    @Test
    void testCapNhatSoLuong_ToZero_RemovesProduct() {
        GioHang gioHang = new GioHang(1L);
        ChiTietGioHang chiTiet = new ChiTietGioHang(1L, "Honda Winner X",
                                                   BigDecimal.valueOf(46000000), 1);
        gioHang.themSanPham(chiTiet);

        gioHang.capNhatSoLuong(1L, 0);

        assertTrue(gioHang.coTrong());
        assertEquals(BigDecimal.ZERO, gioHang.getTongTien());
    }

    @Test
    void testCapNhatSoLuong_NegativeQuantity_ThrowsException() {
        GioHang gioHang = new GioHang(1L);
        ChiTietGioHang chiTiet = new ChiTietGioHang(1L, "Honda Winner X",
                                                   BigDecimal.valueOf(46000000), 1);
        gioHang.themSanPham(chiTiet);

        assertThrows(InvalidCartException.class, () -> {
            gioHang.capNhatSoLuong(1L, -1);
        });
    }

    @Test
    void testXoaToanBoGioHang_Success() {
        GioHang gioHang = new GioHang(1L);
        ChiTietGioHang chiTiet1 = new ChiTietGioHang(1L, "Honda Winner X",
                                                    BigDecimal.valueOf(46000000), 1);
        ChiTietGioHang chiTiet2 = new ChiTietGioHang(2L, "Mũ bảo hiểm",
                                                    BigDecimal.valueOf(850000), 2);
        gioHang.themSanPham(chiTiet1);
        gioHang.themSanPham(chiTiet2);

        gioHang.xoaToanBoGioHang();

        assertTrue(gioHang.coTrong());
        assertEquals(0, gioHang.demSoSanPham());
        assertEquals(BigDecimal.ZERO, gioHang.getTongTien());
    }

    @Test
    void testCoTrong_EmptyCart_ReturnsTrue() {
        GioHang gioHang = new GioHang(1L);

        assertTrue(gioHang.coTrong());
    }

    @Test
    void testCoTrong_NonEmptyCart_ReturnsFalse() {
        GioHang gioHang = new GioHang(1L);
        ChiTietGioHang chiTiet = new ChiTietGioHang(1L, "Honda Winner X",
                                                   BigDecimal.valueOf(46000000), 1);
        gioHang.themSanPham(chiTiet);

        assertFalse(gioHang.coTrong());
    }

    @Test
    void testDemSoSanPham_ReturnsCorrectCount() {
        GioHang gioHang = new GioHang(1L);
        ChiTietGioHang chiTiet1 = new ChiTietGioHang(1L, "Honda Winner X",
                                                    BigDecimal.valueOf(46000000), 1);
        ChiTietGioHang chiTiet2 = new ChiTietGioHang(2L, "Mũ bảo hiểm",
                                                    BigDecimal.valueOf(850000), 2);
        ChiTietGioHang chiTiet3 = new ChiTietGioHang(3L, "Găng tay",
                                                    BigDecimal.valueOf(450000), 1);

        gioHang.themSanPham(chiTiet1);
        gioHang.themSanPham(chiTiet2);
        gioHang.themSanPham(chiTiet3);

        assertEquals(3, gioHang.demSoSanPham());
    }

    @Test
    void testTongSoLuong_ReturnsCorrectSum() {
        GioHang gioHang = new GioHang(1L);
        ChiTietGioHang chiTiet1 = new ChiTietGioHang(1L, "Honda Winner X",
                                                    BigDecimal.valueOf(46000000), 2);
        ChiTietGioHang chiTiet2 = new ChiTietGioHang(2L, "Mũ bảo hiểm",
                                                    BigDecimal.valueOf(850000), 3);
        ChiTietGioHang chiTiet3 = new ChiTietGioHang(3L, "Găng tay",
                                                    BigDecimal.valueOf(450000), 5);

        gioHang.themSanPham(chiTiet1);
        gioHang.themSanPham(chiTiet2);
        gioHang.themSanPham(chiTiet3);

        assertEquals(10, gioHang.tongSoLuong());
    }
}
