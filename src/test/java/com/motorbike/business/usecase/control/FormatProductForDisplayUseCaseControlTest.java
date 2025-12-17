package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.formatproductfordisplay.FormatProductForDisplayInputData;
import com.motorbike.business.dto.formatproductfordisplay.FormatProductForDisplayOutputData;
import com.motorbike.business.usecase.output.FormatProductForDisplayOutputBoundary;
import com.motorbike.domain.entities.SanPham;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FormatProductForDisplayUseCaseControlTest {

    @Mock
    private FormatProductForDisplayOutputBoundary outputBoundary;

    private FormatProductForDisplayUseCaseControl useCase;

    @BeforeEach
    void setUp() {
        useCase = new FormatProductForDisplayUseCaseControl(outputBoundary);
    }

    @Test
    void shouldFormatProductSuccessfully() {
        // Given
        SanPham product = SanPham.createForTest("XE001", "Yamaha Exciter", "Xe thể thao", 45000000.0, 10, true, false);
        product.setMaSP(1L);

        FormatProductForDisplayInputData inputData = new FormatProductForDisplayInputData(product);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(FormatProductForDisplayOutputData.class));
    }

    @Test
    void shouldFormatAccessoryProduct() {
        // Given
        SanPham accessory = SanPham.createForTest("PT001", "Mũ bảo hiểm", "Phụ tùng", 500000.0, 20, true, true);
        accessory.setMaSP(2L);

        FormatProductForDisplayInputData inputData = new FormatProductForDisplayInputData(accessory);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(FormatProductForDisplayOutputData.class));
    }
}
