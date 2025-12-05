package com.motorbike.business.usecase.control;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.motorbike.business.dto.listmyorders.ListMyOrdersInputData;
import com.motorbike.business.dto.listmyorders.ListMyOrdersOutputData;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.business.usecase.output.ListMyOrdersOutputBoundary;
import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.entities.TrangThaiDonHang;

class ListMyOrdersUseCaseControlTest {

    private ListMyOrdersOutputBoundary mockOutputBoundary;
    private OrderRepository mockOrderRepository;
    private ListMyOrdersUseCaseControl useCase;

    @BeforeEach
    void setUp() {
        mockOutputBoundary = mock(ListMyOrdersOutputBoundary.class);
        mockOrderRepository = mock(OrderRepository.class);
        useCase = new ListMyOrdersUseCaseControl(mockOutputBoundary, mockOrderRepository);
    }

    @Test
    void execute_WithValidUserId_ShouldReturnUserOrders() {
        // Arrange
        Long userId = 1L;
        ListMyOrdersInputData inputData = ListMyOrdersInputData.forUser(userId);

        DonHang order1 = mock(DonHang.class);
        when(order1.getMaDonHang()).thenReturn(101L);
        when(order1.getMaTaiKhoan()).thenReturn(userId);
        when(order1.getTenNguoiNhan()).thenReturn("Nguyen Van A");
        when(order1.getSoDienThoai()).thenReturn("0123456789");
        when(order1.getDiaChiGiaoHang()).thenReturn("123 Street");
        when(order1.getTrangThai()).thenReturn(TrangThaiDonHang.CHO_XAC_NHAN);
        when(order1.getTongTien()).thenReturn(new BigDecimal("1000000"));
        when(order1.getDanhSachSanPham()).thenReturn(new ArrayList<>());
        when(order1.getNgayDat()).thenReturn(LocalDateTime.now().minusDays(1));
        when(order1.getGhiChu()).thenReturn("Note 1");

        DonHang order2 = mock(DonHang.class);
        when(order2.getMaDonHang()).thenReturn(102L);
        when(order2.getMaTaiKhoan()).thenReturn(userId);
        when(order2.getTenNguoiNhan()).thenReturn("Nguyen Van A");
        when(order2.getSoDienThoai()).thenReturn("0123456789");
        when(order2.getDiaChiGiaoHang()).thenReturn("456 Avenue");
        when(order2.getTrangThai()).thenReturn(TrangThaiDonHang.DA_GIAO);
        when(order2.getTongTien()).thenReturn(new BigDecimal("2000000"));
        when(order2.getDanhSachSanPham()).thenReturn(new ArrayList<>());
        when(order2.getNgayDat()).thenReturn(LocalDateTime.now().minusDays(2));
        when(order2.getGhiChu()).thenReturn("Note 2");

        List<DonHang> userOrders = Arrays.asList(order1, order2);
        when(mockOrderRepository.findByUserId(userId)).thenReturn(userOrders);

        ArgumentCaptor<ListMyOrdersOutputData> captor = ArgumentCaptor.forClass(ListMyOrdersOutputData.class);

        // Act
        useCase.execute(inputData);

        // Assert
        verify(mockOutputBoundary).present(captor.capture());
        ListMyOrdersOutputData output = captor.getValue();

        assertTrue(output.isSuccess());
        assertFalse(output.isEmpty());
        assertEquals(2, output.getOrders().size());
        
        // Verify order is sorted by date descending (order1 is newer)
        assertEquals(101L, output.getOrders().get(0).getOrderId());
        assertEquals(102L, output.getOrders().get(1).getOrderId());
    }

    @Test
    void execute_WithNullUserId_ShouldReturnError() {
        // Arrange
        ListMyOrdersInputData inputData = ListMyOrdersInputData.forUser(null);
        ArgumentCaptor<ListMyOrdersOutputData> captor = ArgumentCaptor.forClass(ListMyOrdersOutputData.class);

        // Act
        useCase.execute(inputData);

        // Assert
        verify(mockOutputBoundary).present(captor.capture());
        ListMyOrdersOutputData output = captor.getValue();

        assertFalse(output.isSuccess());
        assertEquals("INVALID_USER_ID", output.getErrorCode());
        assertNotNull(output.getErrorMessage());
    }

    @Test
    void execute_WithNullInputData_ShouldReturnError() {
        // Arrange
        ArgumentCaptor<ListMyOrdersOutputData> captor = ArgumentCaptor.forClass(ListMyOrdersOutputData.class);

        // Act
        useCase.execute(null);

        // Assert
        verify(mockOutputBoundary).present(captor.capture());
        ListMyOrdersOutputData output = captor.getValue();

        assertFalse(output.isSuccess());
        assertEquals("INVALID_USER_ID", output.getErrorCode());
    }

    @Test
    void execute_WhenUserHasNoOrders_ShouldReturnEmptyList() {
        // Arrange
        Long userId = 1L;
        ListMyOrdersInputData inputData = ListMyOrdersInputData.forUser(userId);
        
        when(mockOrderRepository.findByUserId(userId)).thenReturn(new ArrayList<>());
        ArgumentCaptor<ListMyOrdersOutputData> captor = ArgumentCaptor.forClass(ListMyOrdersOutputData.class);

        // Act
        useCase.execute(inputData);

        // Assert
        verify(mockOutputBoundary).present(captor.capture());
        ListMyOrdersOutputData output = captor.getValue();

        assertTrue(output.isSuccess());
        assertTrue(output.isEmpty());
        assertEquals(0, output.getOrders().size());
    }

    @Test
    void execute_WhenRepositoryThrowsException_ShouldReturnError() {
        // Arrange
        Long userId = 1L;
        ListMyOrdersInputData inputData = ListMyOrdersInputData.forUser(userId);
        
        when(mockOrderRepository.findByUserId(userId)).thenThrow(new RuntimeException("Database error"));
        ArgumentCaptor<ListMyOrdersOutputData> captor = ArgumentCaptor.forClass(ListMyOrdersOutputData.class);

        // Act
        useCase.execute(inputData);

        // Assert
        verify(mockOutputBoundary).present(captor.capture());
        ListMyOrdersOutputData output = captor.getValue();

        assertFalse(output.isSuccess());
        assertEquals("SYSTEM_ERROR", output.getErrorCode());
        assertEquals("Database error", output.getErrorMessage());
    }

    @Test
    void execute_ShouldOnlyReturnOrdersForSpecificUser() {
        // Arrange
        Long userId = 1L;
        ListMyOrdersInputData inputData = ListMyOrdersInputData.forUser(userId);

        DonHang userOrder = mock(DonHang.class);
        when(userOrder.getMaDonHang()).thenReturn(101L);
        when(userOrder.getMaTaiKhoan()).thenReturn(userId);
        when(userOrder.getTenNguoiNhan()).thenReturn("Nguyen Van A");
        when(userOrder.getSoDienThoai()).thenReturn("0123456789");
        when(userOrder.getDiaChiGiaoHang()).thenReturn("123 Street");
        when(userOrder.getTrangThai()).thenReturn(TrangThaiDonHang.CHO_XAC_NHAN);
        when(userOrder.getTongTien()).thenReturn(new BigDecimal("1000000"));
        when(userOrder.getDanhSachSanPham()).thenReturn(new ArrayList<>());
        when(userOrder.getNgayDat()).thenReturn(LocalDateTime.now());
        when(userOrder.getGhiChu()).thenReturn("Note");

        List<DonHang> userOrders = Arrays.asList(userOrder);
        when(mockOrderRepository.findByUserId(userId)).thenReturn(userOrders);

        ArgumentCaptor<ListMyOrdersOutputData> captor = ArgumentCaptor.forClass(ListMyOrdersOutputData.class);

        // Act
        useCase.execute(inputData);

        // Assert
        verify(mockOrderRepository).findByUserId(userId);
        verify(mockOrderRepository, never()).findAll();
        
        verify(mockOutputBoundary).present(captor.capture());
        ListMyOrdersOutputData output = captor.getValue();

        assertTrue(output.isSuccess());
        assertEquals(1, output.getOrders().size());
        assertEquals(userId, output.getOrders().get(0).getCustomerId());
    }
}
