package com.motorbike.business.usecase.control;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.motorbike.business.dto.cancelorder.CancelOrderInputData;
import com.motorbike.business.dto.cancelorder.CancelOrderOutputData;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.output.CancelOrderOutputBoundary;
import com.motorbike.domain.entities.ChiTietDonHang;
import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.entities.PhuKienXeMay;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.entities.TrangThaiDonHang;
import com.motorbike.domain.entities.XeMay;

@DisplayName("Cancel Order Use Case Tests")
class CancelOrderUseCaseControlTest {

    private CancelOrderUseCaseControl useCase;
    private OrderRepository orderRepository;
    private ProductRepository productRepository;
    private CancelOrderOutputBoundary outputBoundary;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        productRepository = mock(ProductRepository.class);
        outputBoundary = mock(CancelOrderOutputBoundary.class);
        useCase = new CancelOrderUseCaseControl(outputBoundary, orderRepository, productRepository);
    }

    @Test
    @DisplayName("Should cancel order successfully when status is CHO_XAC_NHAN")
    void testCancelOrderSuccess() {
        Long orderId = 1L;
        Long userId = 2L;
        Long productId1 = 1L;
        Long productId2 = 6L;
        
        DonHang order = new DonHang(
            orderId, userId,
            null,
            BigDecimal.valueOf(47700000),
            TrangThaiDonHang.CHO_XAC_NHAN,
            "Nguyễn Văn A",
            "0912345678",
            "123 Nguyễn Trãi, Q1, TP.HCM",
            "Giao giờ hành chính",
            LocalDateTime.now(),
            LocalDateTime.now()
        );
        
        order.themSanPham(new ChiTietDonHang(
            productId1, "Honda Winner X", BigDecimal.valueOf(46000000), 1
        ));
        order.themSanPham(new ChiTietDonHang(
            productId2, "Mũ bảo hiểm Royal", BigDecimal.valueOf(850000), 2
        ));
        
        XeMay motorbike = new XeMay(
            productId1, "Honda Winner X", "Xe thể thao",
            BigDecimal.valueOf(46000000), "/images/honda.jpg", 9, true,
            LocalDateTime.now(), LocalDateTime.now(),
            "Honda", "Winner X", "Đỏ đen", 2025, 150
        );
        
        PhuKienXeMay helmet = new PhuKienXeMay(
            productId2, "Mũ bảo hiểm Royal", "Mũ bảo hiểm fullface",
            BigDecimal.valueOf(850000), "/images/helmet.jpg", 48, true,
            LocalDateTime.now(), LocalDateTime.now(),
            "Mũ bảo hiểm", "Royal", "ABS + EPS", "L"
        );
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(productRepository.findById(productId1)).thenReturn(Optional.of(motorbike));
        when(productRepository.findById(productId2)).thenReturn(Optional.of(helmet));
        when(productRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderRepository.save(any(DonHang.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        CancelOrderInputData inputData = new CancelOrderInputData(
            orderId,
            userId,
            "Tôi muốn thay đổi địa chỉ giao hàng"
        );
        useCase.execute(inputData);
        
        verify(orderRepository).findById(orderId);
        verify(productRepository, times(2)).findById(any());
        verify(productRepository, times(2)).save(any());
        verify(orderRepository).save(any(DonHang.class));
        ArgumentCaptor<CancelOrderOutputData> captor = ArgumentCaptor.forClass(CancelOrderOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        CancelOrderOutputData output = captor.getValue();
        assertTrue(output.isSuccess());
        assertEquals(orderId, output.getOrderId());
        assertEquals(userId, output.getCustomerId());
        assertEquals(TrangThaiDonHang.DA_HUY.name(), output.getOrderStatus());
        assertEquals(0, BigDecimal.valueOf(47700000).compareTo(output.getRefundAmount()));
    }

    @Test
    @DisplayName("KB3 - Fail when order status is invalid for cancellation")
    void testCancelOrder_KB3_InvalidStatus() {

        Long orderId = 1L;
        Long userId = 2L;

        DonHang order = new DonHang(
            orderId, userId, null,
            BigDecimal.valueOf(30000000),
            TrangThaiDonHang.DA_XAC_NHAN, // ❌ không cho phép hủy
            null, null, null, null,
            LocalDateTime.now(), LocalDateTime.now()
        );

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        CancelOrderInputData input = new CancelOrderInputData(orderId, userId, "Hủy");

        useCase.execute(input);

        verify(orderRepository).findById(orderId);
        verify(productRepository, never()).findById(any());
        verify(productRepository, never()).save(any());
        verify(orderRepository, never()).save(any());

        ArgumentCaptor<CancelOrderOutputData> captor = ArgumentCaptor.forClass(CancelOrderOutputData.class);
        verify(outputBoundary).present(captor.capture());

        CancelOrderOutputData output = captor.getValue();
        assertFalse(output.isSuccess());
        assertEquals("CANNOT_CANCEL_ORDER", output.getErrorCode());
    }


    @Test
    @DisplayName("KB1 - Fail when order not found")
    void testCancelOrder_KB1_OrderNotFound() {

        Long orderId = 10L;
        Long userId = 20L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        CancelOrderInputData input = new CancelOrderInputData(orderId, userId, "Hủy đơn");

        useCase.execute(input);

        verify(orderRepository).findById(orderId);
        verify(productRepository, never()).findById(any());
        verify(productRepository, never()).save(any());
        verify(orderRepository, never()).save(any());

        ArgumentCaptor<CancelOrderOutputData> captor = ArgumentCaptor.forClass(CancelOrderOutputData.class);
        verify(outputBoundary).present(captor.capture());

        CancelOrderOutputData output = captor.getValue();
        assertFalse(output.isSuccess());
        assertEquals("CANNOT_CANCEL_ORDER", output.getErrorCode());
    }


    @Test
@DisplayName("KB2 - Fail when user does not have permission to cancel")
void testCancelOrder_KB2_NoPermission() {

    Long orderId = 1L;
    Long ownerId = 2L;       // chủ đơn
    Long wrongUserId = 999L; // user không có quyền

    DonHang order = new DonHang(
        orderId, ownerId, null,
        BigDecimal.valueOf(20000000),
        TrangThaiDonHang.CHO_XAC_NHAN,
        null, null, null, null,
        LocalDateTime.now(), LocalDateTime.now()
    );

    when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

    CancelOrderInputData input = new CancelOrderInputData(orderId, wrongUserId, "Hủy");

    useCase.execute(input);

    verify(orderRepository).findById(orderId);
    verify(productRepository, never()).findById(any());
    verify(productRepository, never()).save(any());
    verify(orderRepository, never()).save(any());

    ArgumentCaptor<CancelOrderOutputData> captor = ArgumentCaptor.forClass(CancelOrderOutputData.class);
    verify(outputBoundary).present(captor.capture());

    CancelOrderOutputData output = captor.getValue();
    assertFalse(output.isSuccess());
    assertEquals("CANNOT_CANCEL_ORDER", output.getErrorCode());
}


    @Test
    @DisplayName("Should fail with null input")
    void testCancelOrderFailNullInput() {
        useCase.execute(null);
        
        verify(orderRepository, never()).findById(any());
        
        ArgumentCaptor<CancelOrderOutputData> captor = ArgumentCaptor.forClass(CancelOrderOutputData.class);
        verify(outputBoundary).present(captor.capture());
        
        CancelOrderOutputData output = captor.getValue();
        assertFalse(output.isSuccess());
        assertEquals("INVALID_INPUT", output.getErrorCode());
    }

    @Test
    @DisplayName("Should recover stock correctly")
    void testStockRecovery() {
        Long orderId = 1L;
        Long userId = 2L;
        Long productId1 = 1L;
        Long productId2 = 6L;
        
        DonHang order = new DonHang(
            orderId, userId, null,
            BigDecimal.valueOf(47700000),
            TrangThaiDonHang.CHO_XAC_NHAN,
            "Nguyễn Văn A",
            "0912345678",
            "123 Nguyễn Trãi, Q1, TP.HCM",
            null,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
        
        order.themSanPham(new ChiTietDonHang(
            productId1, "Honda Winner X", BigDecimal.valueOf(46000000), 1
        ));
        order.themSanPham(new ChiTietDonHang(
            productId2, "Mũ bảo hiểm Royal", BigDecimal.valueOf(850000), 2
        ));
        
        XeMay motorbike = new XeMay(
            productId1, "Honda Winner X", "Xe thể thao",
            BigDecimal.valueOf(46000000), "/images/honda.jpg", 9, true,
            LocalDateTime.now(), LocalDateTime.now(),
            "Honda", "Winner X", "Đỏ đen", 2025, 150
        );
        
        PhuKienXeMay helmet = new PhuKienXeMay(
            productId2, "Mũ bảo hiểm Royal", "Mũ bảo hiểm fullface",
            BigDecimal.valueOf(850000), "/images/helmet.jpg", 48, true,
            LocalDateTime.now(), LocalDateTime.now(),
            "Mũ bảo hiểm", "Royal", "ABS + EPS", "L"
        );
        
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(productRepository.findById(productId1)).thenReturn(Optional.of(motorbike));
        when(productRepository.findById(productId2)).thenReturn(Optional.of(helmet));
        when(productRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderRepository.save(any(DonHang.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        CancelOrderInputData inputData = new CancelOrderInputData(
            orderId,
            userId,
            "Hủy đơn"
        );
        useCase.execute(inputData);
        
        ArgumentCaptor<SanPham> productCaptor = ArgumentCaptor.forClass(SanPham.class);
        verify(productRepository, times(2)).save(productCaptor.capture());
        
    }
}
