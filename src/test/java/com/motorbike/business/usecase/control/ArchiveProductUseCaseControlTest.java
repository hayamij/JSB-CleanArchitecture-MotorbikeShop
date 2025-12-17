package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.archiveproduct.ArchiveProductInputData;
import com.motorbike.business.dto.archiveproduct.ArchiveProductOutputData;
import com.motorbike.business.usecase.output.ArchiveProductOutputBoundary;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.business.ports.SanPhamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ArchiveProductUseCaseControlTest {

    @Mock
    private SanPhamRepository sanPhamRepository;

    @Mock
    private ArchiveProductOutputBoundary outputBoundary;

    private ArchiveProductUseCaseControl useCase;

    @BeforeEach
    void setUp() {
        useCase = new ArchiveProductUseCaseControl(sanPhamRepository, outputBoundary);
    }

    @Test
    void shouldArchiveProductSuccessfully() {
        // Given
        SanPham product = SanPham.createForTest("XE001", "Yamaha Exciter", "Xe", 45000000.0, 10, true, false);
        product.setMaSP(1L);

        when(sanPhamRepository.save(any(SanPham.class))).thenReturn(product);

        ArchiveProductInputData inputData = new ArchiveProductInputData(product);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(ArchiveProductOutputData.class));
        verify(sanPhamRepository).save(any(SanPham.class));
    }
}
