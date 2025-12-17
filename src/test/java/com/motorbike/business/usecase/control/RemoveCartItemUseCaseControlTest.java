package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.removecartitem.RemoveCartItemInputData;
import com.motorbike.business.dto.removecartitem.RemoveCartItemOutputData;
import com.motorbike.business.usecase.output.RemoveCartItemOutputBoundary;
import com.motorbike.business.ports.GioHangRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RemoveCartItemUseCaseControlTest {

    @Mock
    private GioHangRepository gioHangRepository;

    @Mock
    private RemoveCartItemOutputBoundary outputBoundary;

    private RemoveCartItemUseCaseControl useCase;

    @BeforeEach
    void setUp() {
        useCase = new RemoveCartItemUseCaseControl(gioHangRepository, outputBoundary);
    }

    @Test
    void shouldRemoveCartItemSuccessfully() {
        // Given
        Long cartItemId = 1L;
        doNothing().when(gioHangRepository).deleteById(cartItemId);

        RemoveCartItemInputData inputData = new RemoveCartItemInputData(cartItemId);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(RemoveCartItemOutputData.class));
        verify(gioHangRepository).deleteById(cartItemId);
    }

    @Test
    void shouldHandleRepositoryException() {
        // Given
        Long cartItemId = 999L;
        doThrow(new RuntimeException("Database error")).when(gioHangRepository).deleteById(cartItemId);

        RemoveCartItemInputData inputData = new RemoveCartItemInputData(cartItemId);

        // When/Then
        try {
            useCase.execute(inputData);
        } catch (RuntimeException e) {
            // Expected
        }

        verify(gioHangRepository).deleteById(cartItemId);
    }
}
