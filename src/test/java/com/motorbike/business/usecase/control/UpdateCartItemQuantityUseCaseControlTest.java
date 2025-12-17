package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.updatecartitemquantity.UpdateCartItemQuantityInputData;
import com.motorbike.business.dto.updatecartitemquantity.UpdateCartItemQuantityOutputData;
import com.motorbike.business.usecase.output.UpdateCartItemQuantityOutputBoundary;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.business.ports.GioHangRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateCartItemQuantityUseCaseControlTest {

    @Mock
    private GioHangRepository gioHangRepository;

    @Mock
    private UpdateCartItemQuantityOutputBoundary outputBoundary;

    private UpdateCartItemQuantityUseCaseControl useCase;

    @BeforeEach
    void setUp() {
        useCase = new UpdateCartItemQuantityUseCaseControl(gioHangRepository, outputBoundary);
    }

    @Test
    void shouldUpdateCartItemQuantitySuccessfully() {
        // Given
        GioHang cartItem = new GioHang(1L, 100L, 5);
        cartItem.setMaGH(1L);
        int newQuantity = 10;

        when(gioHangRepository.save(any(GioHang.class))).thenReturn(cartItem);

        UpdateCartItemQuantityInputData inputData = new UpdateCartItemQuantityInputData(cartItem, newQuantity);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(UpdateCartItemQuantityOutputData.class));
        verify(gioHangRepository).save(any(GioHang.class));
    }

    @Test
    void shouldHandleZeroQuantity() {
        // Given
        GioHang cartItem = new GioHang(1L, 100L, 5);
        cartItem.setMaGH(1L);
        int newQuantity = 0;

        UpdateCartItemQuantityInputData inputData = new UpdateCartItemQuantityInputData(cartItem, newQuantity);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(UpdateCartItemQuantityOutputData.class));
    }
}
