package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.checkproductavailability.CheckProductAvailabilityInputData;
import com.motorbike.business.dto.checkproductavailability.CheckProductAvailabilityOutputData;
import com.motorbike.business.usecase.output.CheckProductAvailabilityOutputBoundary;
import com.motorbike.domain.entities.SanPham;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CheckProductAvailabilityUseCaseControlTest {

    @Mock
    private CheckProductAvailabilityOutputBoundary outputBoundary;

    private CheckProductAvailabilityUseCaseControl useCase;

    @BeforeEach
    void setUp() {
        useCase = new CheckProductAvailabilityUseCaseControl(outputBoundary);
    }

    @Test
    void shouldConfirmProductIsAvailable() {
        // Given
        SanPham product = SanPham.createForTest("XE001", "Yamaha Exciter", "Xe", 45000000.0, 10, true, false);
        product.setMaSP(1L);

        CheckProductAvailabilityInputData inputData = new CheckProductAvailabilityInputData(product);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(CheckProductAvailabilityOutputData.class));
    }

    @Test
    void shouldConfirmProductIsNotAvailable() {
        // Given
        SanPham product = SanPham.createForTest("XE001", "Yamaha Exciter", "Xe", 45000000.0, 0, false, false);
        product.setMaSP(1L);

        CheckProductAvailabilityInputData inputData = new CheckProductAvailabilityInputData(product);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(CheckProductAvailabilityOutputData.class));
    }
}
