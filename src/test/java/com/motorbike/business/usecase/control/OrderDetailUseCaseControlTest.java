package com.motorbike.business.usecase.control;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.motorbike.business.dto.orderdetail.OrderDetailInputData;
import com.motorbike.business.dto.orderdetail.OrderDetailOutputData;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.business.usecase.output.OrderDetailOutputBoundary;
import com.motorbike.domain.entities.ChiTietDonHang;
import com.motorbike.domain.entities.DonHang;

class OrderDetailUseCaseControlTest {

    private OrderRepository orderRepository;
    private OrderDetailOutputBoundary outputBoundary;
    private OrderDetailUseCaseControl useCase;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        outputBoundary = mock(OrderDetailOutputBoundary.class);
        useCase = new OrderDetailUseCaseControl(outputBoundary, orderRepository);
    }

    @Test
    @DisplayName("Admin: should return order detail with mapped items")
    void adminSuccess() {
        DonHang order = buildOrder(1L, 99L);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderDetailInputData input = OrderDetailInputData.forAdmin(1L);
        useCase.execute(input);

        verify(orderRepository).findById(1L);
        ArgumentCaptor<OrderDetailOutputData> captor = ArgumentCaptor.forClass(OrderDetailOutputData.class);
        verify(outputBoundary).present(captor.capture());

        OrderDetailOutputData output = captor.getValue();
        assertTrue(output.isSuccess());
        assertNotNull(output.getOrder());
        assertEquals(2, output.getOrder().getItems().size());
        assertEquals(2, output.getOrder().getTotalItems());
        assertEquals(3, output.getOrder().getTotalQuantity());
    }

    @Test
    @DisplayName("User: should reject when user is not owner")
    void userUnauthorized() {
        DonHang order = buildOrder(1L, 99L);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderDetailInputData input = OrderDetailInputData.forUser(1L, 2L); // not owner
        useCase.execute(input);

        ArgumentCaptor<OrderDetailOutputData> captor = ArgumentCaptor.forClass(OrderDetailOutputData.class);
        verify(outputBoundary).present(captor.capture());
        OrderDetailOutputData output = captor.getValue();

        assertFalse(output.isSuccess());
        assertEquals("CANNOT_UPDATE_ORDER", output.getErrorCode());
    }

    @Test
    @DisplayName("Should fail when input is null")
    void nullInput() {
        useCase.execute(null);

        verify(orderRepository, never()).findById(1L);
        ArgumentCaptor<OrderDetailOutputData> captor = ArgumentCaptor.forClass(OrderDetailOutputData.class);
        verify(outputBoundary).present(captor.capture());
        OrderDetailOutputData output = captor.getValue();

        assertFalse(output.isSuccess());
        assertEquals("INVALID_INPUT", output.getErrorCode());
    }

    @Test
    @DisplayName("Should fail when orderId is null")
    void nullOrderId() {
        OrderDetailInputData input = OrderDetailInputData.forAdmin(null);
        useCase.execute(input);

        verify(orderRepository, never()).findById(null);
        ArgumentCaptor<OrderDetailOutputData> captor = ArgumentCaptor.forClass(OrderDetailOutputData.class);
        verify(outputBoundary).present(captor.capture());
        assertEquals("NULL_ORDER_ID", captor.getValue().getErrorCode());
    }

    @Test
    @DisplayName("Should fail when userId is null for non-admin")
    void nullUserIdForUser() {
        OrderDetailInputData input = OrderDetailInputData.forUser(1L, null);
        useCase.execute(input);

        ArgumentCaptor<OrderDetailOutputData> captor = ArgumentCaptor.forClass(OrderDetailOutputData.class);
        verify(outputBoundary).present(captor.capture());
        assertEquals("INVALID_USER_ID", captor.getValue().getErrorCode());
    }

    @Test
    @DisplayName("Should return not found when repository empty")
    void orderNotFound() {
        when(orderRepository.findById(42L)).thenReturn(Optional.empty());

        OrderDetailInputData input = OrderDetailInputData.forAdmin(42L);
        useCase.execute(input);

        ArgumentCaptor<OrderDetailOutputData> captor = ArgumentCaptor.forClass(OrderDetailOutputData.class);
        verify(outputBoundary).present(captor.capture());
        OrderDetailOutputData output = captor.getValue();

        assertFalse(output.isSuccess());
        assertEquals("ORDER_NOT_FOUND", output.getErrorCode());
    }

    @Test
    @DisplayName("Should surface repository exceptions as SYSTEM_ERROR")
    void repositoryThrows() {
        when(orderRepository.findById(5L)).thenThrow(new RuntimeException("DB boom"));

        OrderDetailInputData input = OrderDetailInputData.forAdmin(5L);
        useCase.execute(input);

        ArgumentCaptor<OrderDetailOutputData> captor = ArgumentCaptor.forClass(OrderDetailOutputData.class);
        verify(outputBoundary).present(captor.capture());
        OrderDetailOutputData output = captor.getValue();

        assertFalse(output.isSuccess());
        assertEquals("SYSTEM_ERROR", output.getErrorCode());
        assertEquals("DB boom", output.getErrorMessage());
    }

    private DonHang buildOrder(Long orderId, Long userId) {
        DonHang order = new DonHang(userId, "Ten", "0123", "Dia chi", "Ghi chu");
        order.setMaDonHang(orderId);
        order.setDanhSachSanPham(Arrays.asList(
                new ChiTietDonHang(10L, "SP A", BigDecimal.valueOf(100000), 1),
                new ChiTietDonHang(11L, "SP B", BigDecimal.valueOf(200000), 2)
        ));
        return order;
    }
}
