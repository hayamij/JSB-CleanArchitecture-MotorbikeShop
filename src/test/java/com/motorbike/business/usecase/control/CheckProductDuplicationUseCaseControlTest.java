package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.checkproductduplication.CheckProductDuplicationInputData;
import com.motorbike.business.dto.checkproductduplication.CheckProductDuplicationOutputData;
import com.motorbike.business.usecase.output.CheckProductDuplicationOutputBoundary;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.business.ports.SanPhamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CheckProductDuplicationUseCaseControlTest {

    @Mock
    private SanPhamRepository sanPhamRepository;

    @Mock
    private CheckProductDuplicationOutputBoundary outputBoundary;

    private CheckProductDuplicationUseCaseControl useCase;

    @BeforeEach
    void setUp() {
        useCase = new CheckProductDuplicationUseCaseControl(sanPhamRepository, outputBoundary);
    }

    @Test
    void shouldDetectDuplicateProductCode() {
        // Given
        String productCode = "XE001";
        SanPham existingProduct = SanPham.createForTest(productCode, "Yamaha Exciter", "Xe", 45000000.0, 10, true, false);

        when(sanPhamRepository.findByMaSanPham(productCode)).thenReturn(Optional.of(existingProduct));

        CheckProductDuplicationInputData inputData = new CheckProductDuplicationInputData(productCode, null);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(CheckProductDuplicationOutputData.class));
        verify(sanPhamRepository).findByMaSanPham(productCode);
    }

    @Test
    void shouldAllowUniqueProductCode() {
        // Given
        String productCode = "XE999";

        when(sanPhamRepository.findByMaSanPham(productCode)).thenReturn(Optional.empty());

        CheckProductDuplicationInputData inputData = new CheckProductDuplicationInputData(productCode, null);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(CheckProductDuplicationOutputData.class));
        verify(sanPhamRepository).findByMaSanPham(productCode);
    }

    @Test
    void shouldAllowSameProductForUpdate() {
        // Given
        String productCode = "XE001";
        Long productId = 1L;
        SanPham existingProduct = SanPham.createForTest(productCode, "Yamaha Exciter", "Xe", 45000000.0, 10, true, false);
        existingProduct.setMaSP(productId);

        when(sanPhamRepository.findByMaSanPham(productCode)).thenReturn(Optional.of(existingProduct));

        CheckProductDuplicationInputData inputData = new CheckProductDuplicationInputData(productCode, productId);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(CheckProductDuplicationOutputData.class));
    }
}
