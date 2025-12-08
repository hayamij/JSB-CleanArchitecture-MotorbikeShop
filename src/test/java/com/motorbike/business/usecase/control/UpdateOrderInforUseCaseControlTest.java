package com.motorbike.business.usecase.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.motorbike.business.dto.updateorderinfor.UpdateOrderInforInputData;
import com.motorbike.business.dto.updateorderinfor.UpdateOrderInforOutputData;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.business.usecase.output.UpdateOrderInforOutputBoundary;
import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.entities.TrangThaiDonHang;

class UpdateOrderInforUseCaseControlTest {

    private UpdateOrderInforUseCaseControl useCase;
    private OrderRepository orderRepository;
    private UpdateOrderInforOutputBoundary outputBoundary;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        outputBoundary = mock(UpdateOrderInforOutputBoundary.class);
        useCase = new UpdateOrderInforUseCaseControl(outputBoundary, orderRepository);
    }

    @Test
    @DisplayName("Should update shipping info when order is pending and user owns it")
    void testUpdateSuccess() {
        Long orderId = 1L;
        Long userId = 10L;

        DonHang order = new DonHang(
            orderId,
            userId,
            new ArrayList<>(),
            BigDecimal.valueOf(5000000),
            TrangThaiDonHang.CHO_XAC_NHAN,
            "Old Name",
            "0909000000",
            "Old Address",
            "Old Note",
            LocalDateTime.now().minusDays(1),
            LocalDateTime.now().minusDays(1)
        );

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(DonHang.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UpdateOrderInforInputData input = new UpdateOrderInforInputData(
            orderId,
            userId,
            "New Name",
            "0988000000",
            "123 New Street",
            "Please call before delivery"
        );

        useCase.execute(input);

        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(any(DonHang.class));

        ArgumentCaptor<UpdateOrderInforOutputData> captor = ArgumentCaptor.forClass(UpdateOrderInforOutputData.class);
        verify(outputBoundary).present(captor.capture());

        UpdateOrderInforOutputData output = captor.getValue();
        assertTrue(output.isSuccess());
        assertEquals(orderId, output.getOrderId());
        assertEquals(userId, output.getCustomerId());
        assertEquals("New Name", output.getReceiverName());
        assertEquals("0988000000", output.getPhoneNumber());
        assertEquals("123 New Street", output.getShippingAddress());
        assertEquals("Please call before delivery", output.getNote());
        assertEquals(TrangThaiDonHang.CHO_XAC_NHAN.name(), output.getOrderStatus());
    }

    @Test
    @DisplayName("Should fail when order status is not editable")
    void testUpdateFailsForInvalidStatus() {
        Long orderId = 2L;
        Long userId = 10L;

        DonHang order = new DonHang(
            orderId,
            userId,
            new ArrayList<>(),
            BigDecimal.valueOf(2000000),
            TrangThaiDonHang.DA_XAC_NHAN,
            "Old Name",
            "0909000000",
            "Old Address",
            null,
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        UpdateOrderInforInputData input = new UpdateOrderInforInputData(
            orderId,
            userId,
            "New Name",
            "0988000000",
            "New Address",
            "Note"
        );

        useCase.execute(input);

        verify(orderRepository).findById(orderId);
        verify(orderRepository, never()).save(any(DonHang.class));

        ArgumentCaptor<UpdateOrderInforOutputData> captor = ArgumentCaptor.forClass(UpdateOrderInforOutputData.class);
        verify(outputBoundary).present(captor.capture());

        UpdateOrderInforOutputData output = captor.getValue();
        assertFalse(output.isSuccess());
        assertEquals("CANNOT_UPDATE_ORDER", output.getErrorCode());
    }

    @Test
    @DisplayName("Should fail when user does not own the order")
    void testUpdateFailsForWrongUser() {
        Long orderId = 3L;
        Long ownerId = 20L;
        Long otherUser = 30L;

        DonHang order = new DonHang(
            orderId,
            ownerId,
            new ArrayList<>(),
            BigDecimal.valueOf(3000000),
            TrangThaiDonHang.CHO_XAC_NHAN,
            "Old",
            "0909000000",
            "Old Address",
            null,
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        UpdateOrderInforInputData input = new UpdateOrderInforInputData(
            orderId,
            otherUser,
            "New",
            "0988111111",
            "New Address",
            null
        );

        useCase.execute(input);

        verify(orderRepository).findById(orderId);
        verify(orderRepository, never()).save(any(DonHang.class));

        ArgumentCaptor<UpdateOrderInforOutputData> captor = ArgumentCaptor.forClass(UpdateOrderInforOutputData.class);
        verify(outputBoundary).present(captor.capture());

        UpdateOrderInforOutputData output = captor.getValue();
        assertFalse(output.isSuccess());
        assertEquals("CANNOT_UPDATE_ORDER", output.getErrorCode());
    }

    @Test
    @DisplayName("Should fail when order not found")
    void testUpdateFailsWhenOrderNotFound() {
        Long orderId = 99L;
        Long userId = 1L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        UpdateOrderInforInputData input = new UpdateOrderInforInputData(
            orderId,
            userId,
            "Name",
            "0988000000",
            "Address",
            null
        );

        useCase.execute(input);

        verify(orderRepository).findById(orderId);
        verify(orderRepository, never()).save(any(DonHang.class));

        ArgumentCaptor<UpdateOrderInforOutputData> captor = ArgumentCaptor.forClass(UpdateOrderInforOutputData.class);
        verify(outputBoundary).present(captor.capture());

        UpdateOrderInforOutputData output = captor.getValue();
        assertFalse(output.isSuccess());
        assertEquals("CANNOT_UPDATE_ORDER", output.getErrorCode());
    }

    @Test
    @DisplayName("Should fail when input is null")
    void testUpdateFailsWithNullInput() {
        useCase.execute(null);

        verify(orderRepository, never()).findById(any());
        verify(orderRepository, never()).save(any());

        ArgumentCaptor<UpdateOrderInforOutputData> captor = ArgumentCaptor.forClass(UpdateOrderInforOutputData.class);
        verify(outputBoundary).present(captor.capture());

        UpdateOrderInforOutputData output = captor.getValue();
        assertFalse(output.isSuccess());
        assertEquals("INVALID_INPUT", output.getErrorCode());
    }
}
