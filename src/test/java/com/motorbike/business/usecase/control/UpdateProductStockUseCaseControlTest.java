package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.updateproductstock.UpdateProductStockInputData;
import com.motorbike.business.dto.updateproductstock.UpdateProductStockOutputData;
import com.motorbike.business.usecase.output.UpdateProductStockOutputBoundary;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.business.ports.SanPhamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateProductStockUseCaseControlTest {

    @Mock
    private SanPhamRepository sanPhamRepository;

    @Mock
    private UpdateProductStockOutputBoundary outputBoundary;

    private UpdateProductStockUseCaseControl useCase;

    @BeforeEach
    void setUp() {
        useCase = new UpdateProductStockUseCaseControl(sanPhamRepository, outputBoundary);
    }

    @Test
    void shouldUpdateStockSuccessfully() {
        // Given
        SanPham product = SanPham.createForTest("XE001", "Yamaha Exciter", "Xe", 45000000.0, 10, true, false);
        product.setMaSP(1L);

        GioHang item = new GioHang(1L, 1L, 5);
        item.setSanPham(product);

        List<GioHang> cartItems = Arrays.asList(item);

        when(sanPhamRepository.findById(1L)).thenReturn(Optional.of(product));
        when(sanPhamRepository.save(any(SanPham.class))).thenReturn(product);

        UpdateProductStockInputData inputData = new UpdateProductStockInputData(cartItems);

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(UpdateProductStockOutputData.class));
        verify(sanPhamRepository, times(1)).save(any(SanPham.class));
    }
}
