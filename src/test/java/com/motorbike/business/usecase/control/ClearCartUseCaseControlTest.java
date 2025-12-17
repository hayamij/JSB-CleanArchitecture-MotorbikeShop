package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.clearcart.ClearCartInputData;
import com.motorbike.business.dto.clearcart.ClearCartOutputData;
import com.motorbike.business.usecase.output.ClearCartOutputBoundary;
import com.motorbike.business.ports.GioHangRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClearCartUseCaseControlTest {

    @Mock
    private GioHangRepository gioHangRepository;

    @Mock
    private ClearCartOutputBoundary outputBoundary;

    private ClearCartUseCaseControl useCase;

    @BeforeEach
    void setUp() {
        useCase = new ClearCartUseCaseControl(gioHangRepository, outputBoundary);
    }

    @Test
    void shouldClearCartSuccessfully() {
        // Given
        Long userId = 1L;
        doNothing().when(gioHangRepository).deleteAllByUserId(userId);

        ClearCartInputData inputData = new ClearCartInputData(userId);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(ClearCartOutputData.class));
        verify(gioHangRepository).deleteAllByUserId(userId);
    }
}
