package com.motorbike.domain.entities;

import com.motorbike.domain.exceptions.InvalidCartException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GioHang entity
 */
class GioHangTest {

    @Test
    void testCreateGioHang_Success() {
        // Given
        Long maTaiKhoan = 1L;

        // When
        GioHang gioHang = new GioHang(maTaiKhoan);

        // Then
        assertNotNull(gioHang);
        assertEquals(maTaiKhoan, gioHang.getMaTaiKhoan());
        assertTrue(gioHang.coTrong());
        assertEquals(BigDecimal.ZERO, gioHang.getTongTien());
    }

    @Test
    void testThemSanPham_NewProduct_Success() {
        // Given
        GioHang gioHang = new GioHang(1L);
        ChiTietGioHang chiTiet = new ChiTietGioHang(1L, "Honda Winner X", 
                                                   BigDecimal.valueOf(46000000), 1);

        // When
        gioHang.themSanPham(chiTiet);

        // Then
        assertEquals(1, gioHang.demSoSanPham());
        assertEquals(1, gioHang.tongSoLuong());
        assertEquals(0, BigDecimal.valueOf(46000000).compareTo(gioHang.getTongTien()));
    }

    @Test
    void testThemSanPham_ExistingProduct_IncreasesQuantity() {
        // Given
        GioHang gioHang = new GioHang(1L);
        ChiTietGioHang chiTiet1 = new ChiTietGioHang(1L, "Honda Winner X", 
                                                    BigDecimal.valueOf(46000000), 1);
        ChiTietGioHang chiTiet2 = new ChiTietGioHang(1L, "Honda Winner X", 
                                                    BigDecimal.valueOf(46000000), 2);

        // When
        gioHang.themSanPham(chiTiet1);
        gioHang.themSanPham(chiTiet2);

        // Then
        assertEquals(1, gioHang.demSoSanPham()); // Chỉ có 1 mặt hàng
        assertEquals(3, gioHang.tongSoLuong()); // Tổng số lượng = 1 + 2
        assertEquals(0, BigDecimal.valueOf(138000000).compareTo(gioHang.getTongTien()));
    }

    @Test
    void testThemSanPham_MultipleProducts_Success() {
        // Given
        GioHang gioHang = new GioHang(1L);
        ChiTietGioHang chiTiet1 = new ChiTietGioHang(1L, "Honda Winner X", 
                                                    BigDecimal.valueOf(46000000), 1);
        ChiTietGioHang chiTiet2 = new ChiTietGioHang(2L, "Mũ bảo hiểm", 
                                                    BigDecimal.valueOf(850000), 2);

        // When
        gioHang.themSanPham(chiTiet1);
        gioHang.themSanPham(chiTiet2);

        // Then
        assertEquals(2, gioHang.demSoSanPham());
        assertEquals(3, gioHang.tongSoLuong());
        BigDecimal expectedTotal = BigDecimal.valueOf(46000000).add(BigDecimal.valueOf(1700000));
        assertEquals(0, expectedTotal.compareTo(gioHang.getTongTien()));
    }

    @Test
    void testThemSanPham_NullItem_ThrowsException() {
        // Given
        GioHang gioHang = new GioHang(1L);

        // When & Then
        assertThrows(InvalidCartException.class, () -> {
            gioHang.themSanPham(null);
        });
    }

    @Test
    void testXoaSanPham_Success() {
        // Given
        GioHang gioHang = new GioHang(1L);
        ChiTietGioHang chiTiet = new ChiTietGioHang(1L, "Honda Winner X", 
                                                   BigDecimal.valueOf(46000000), 1);
        gioHang.themSanPham(chiTiet);

        // When
        gioHang.xoaSanPham(1L);

        // Then
        assertTrue(gioHang.coTrong());
        assertEquals(BigDecimal.ZERO, gioHang.getTongTien());
    }

    @Test
    void testXoaSanPham_NonExistentProduct_ThrowsException() {
        // Given
        GioHang gioHang = new GioHang(1L);
        ChiTietGioHang chiTiet = new ChiTietGioHang(1L, "Honda Winner X", 
                                                   BigDecimal.valueOf(46000000), 1);
        gioHang.themSanPham(chiTiet);

        // When & Then
        assertThrows(com.motorbike.domain.exceptions.ProductNotInCartException.class, 
            () -> gioHang.xoaSanPham(999L));
    }

    @Test
    void testCapNhatSoLuong_Success() {
        // Given
        GioHang gioHang = new GioHang(1L);
        ChiTietGioHang chiTiet = new ChiTietGioHang(1L, "Honda Winner X", 
                                                   BigDecimal.valueOf(46000000), 1);
        gioHang.themSanPham(chiTiet);

        // When
        gioHang.capNhatSoLuong(1L, 3);

        // Then
        assertEquals(1, gioHang.demSoSanPham());
        assertEquals(3, gioHang.tongSoLuong());
        assertEquals(0, BigDecimal.valueOf(138000000).compareTo(gioHang.getTongTien()));
    }

    @Test
    void testCapNhatSoLuong_ToZero_RemovesProduct() {
        // Given
        GioHang gioHang = new GioHang(1L);
        ChiTietGioHang chiTiet = new ChiTietGioHang(1L, "Honda Winner X", 
                                                   BigDecimal.valueOf(46000000), 1);
        gioHang.themSanPham(chiTiet);

        // When
        gioHang.capNhatSoLuong(1L, 0);

        // Then
        assertTrue(gioHang.coTrong());
        assertEquals(BigDecimal.ZERO, gioHang.getTongTien());
    }

    @Test
    void testCapNhatSoLuong_NegativeQuantity_ThrowsException() {
        // Given
        GioHang gioHang = new GioHang(1L);
        ChiTietGioHang chiTiet = new ChiTietGioHang(1L, "Honda Winner X", 
                                                   BigDecimal.valueOf(46000000), 1);
        gioHang.themSanPham(chiTiet);

        // When & Then
        assertThrows(InvalidCartException.class, () -> {
            gioHang.capNhatSoLuong(1L, -1);
        });
    }

    @Test
    void testXoaToanBoGioHang_Success() {
        // Given
        GioHang gioHang = new GioHang(1L);
        ChiTietGioHang chiTiet1 = new ChiTietGioHang(1L, "Honda Winner X", 
                                                    BigDecimal.valueOf(46000000), 1);
        ChiTietGioHang chiTiet2 = new ChiTietGioHang(2L, "Mũ bảo hiểm", 
                                                    BigDecimal.valueOf(850000), 2);
        gioHang.themSanPham(chiTiet1);
        gioHang.themSanPham(chiTiet2);

        // When
        gioHang.xoaToanBoGioHang();

        // Then
        assertTrue(gioHang.coTrong());
        assertEquals(0, gioHang.demSoSanPham());
        assertEquals(BigDecimal.ZERO, gioHang.getTongTien());
    }

    @Test
    void testCoTrong_EmptyCart_ReturnsTrue() {
        // Given
        GioHang gioHang = new GioHang(1L);

        // When & Then
        assertTrue(gioHang.coTrong());
    }

    @Test
    void testCoTrong_NonEmptyCart_ReturnsFalse() {
        // Given
        GioHang gioHang = new GioHang(1L);
        ChiTietGioHang chiTiet = new ChiTietGioHang(1L, "Honda Winner X", 
                                                   BigDecimal.valueOf(46000000), 1);
        gioHang.themSanPham(chiTiet);

        // When & Then
        assertFalse(gioHang.coTrong());
    }

    @Test
    void testDemSoSanPham_ReturnsCorrectCount() {
        // Given
        GioHang gioHang = new GioHang(1L);
        ChiTietGioHang chiTiet1 = new ChiTietGioHang(1L, "Honda Winner X", 
                                                    BigDecimal.valueOf(46000000), 1);
        ChiTietGioHang chiTiet2 = new ChiTietGioHang(2L, "Mũ bảo hiểm", 
                                                    BigDecimal.valueOf(850000), 2);
        ChiTietGioHang chiTiet3 = new ChiTietGioHang(3L, "Găng tay", 
                                                    BigDecimal.valueOf(450000), 1);

        // When
        gioHang.themSanPham(chiTiet1);
        gioHang.themSanPham(chiTiet2);
        gioHang.themSanPham(chiTiet3);

        // Then
        assertEquals(3, gioHang.demSoSanPham());
    }

    @Test
    void testTongSoLuong_ReturnsCorrectSum() {
        // Given
        GioHang gioHang = new GioHang(1L);
        ChiTietGioHang chiTiet1 = new ChiTietGioHang(1L, "Honda Winner X", 
                                                    BigDecimal.valueOf(46000000), 2);
        ChiTietGioHang chiTiet2 = new ChiTietGioHang(2L, "Mũ bảo hiểm", 
                                                    BigDecimal.valueOf(850000), 3);
        ChiTietGioHang chiTiet3 = new ChiTietGioHang(3L, "Găng tay", 
                                                    BigDecimal.valueOf(450000), 5);

        // When
        gioHang.themSanPham(chiTiet1);
        gioHang.themSanPham(chiTiet2);
        gioHang.themSanPham(chiTiet3);

        // Then
        assertEquals(10, gioHang.tongSoLuong()); // 2 + 3 + 5
    }
}
