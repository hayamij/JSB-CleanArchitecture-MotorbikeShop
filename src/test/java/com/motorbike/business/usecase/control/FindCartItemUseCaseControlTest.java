package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.findcartitem.FindCartItemInputData;
import com.motorbike.business.dto.findcartitem.FindCartItemOutputData;
import com.motorbike.business.usecase.output.FindCartItemOutputBoundary;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.business.ports.GioHangRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FindCartItemUseCaseControlTest {

    @Mock
    private GioHangRepository gioHangRepository;

    @Mock
    private FindCartItemOutputBoundary outputBoundary;

    private FindCartItemUseCaseControl useCase;

    @BeforeEach
    void setUp() {
        useCase = new FindCartItemUseCaseControl(gioHangRepository, outputBoundary);
    }

    @Test
    void shouldFindCartItemSuccessfully() {
        // Given
        Long userId = 1L;
        Long productId = 100L;
        GioHang cartItem = new GioHang(userId, productId, 5);
        cartItem.setMaGH(1L);

        when(gioHangRepository.findByUserIdAndProductId(userId, productId))
                .thenReturn(Optional.of(cartItem));

        FindCartItemInputData inputData = new FindCartItemInputData(userId, productId);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(FindCartItemOutputData.class));
        verify(gioHangRepository).findByUserIdAndProductId(userId, productId);
    }

    @Test
    void shouldHandleCartItemNotFound() {
        // Given
        Long userId = 1L;
        Long productId = 999L;

        when(gioHangRepository.findByUserIdAndProductId(userId, productId))
                .thenReturn(Optional.empty());

        FindCartItemInputData inputData = new FindCartItemInputData(userId, productId);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(FindCartItemOutputData.class));
        verify(gioHangRepository).findByUserIdAndProductId(userId, productId);
    }
}
