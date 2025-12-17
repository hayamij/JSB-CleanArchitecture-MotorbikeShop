package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.createorder.CreateOrderFromCartInputData;
import com.motorbike.business.dto.createorder.CreateOrderFromCartOutputData;
import com.motorbike.business.usecase.output.CreateOrderFromCartOutputBoundary;
import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.entities.GioHang;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.business.ports.DonHangRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateOrderFromCartUseCaseControlTest {

    @Mock
    private DonHangRepository donHangRepository;

    @Mock
    private CreateOrderFromCartOutputBoundary outputBoundary;

    private CreateOrderFromCartUseCaseControl useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateOrderFromCartUseCaseControl(donHangRepository, outputBoundary);
    }

    @Test
    void shouldCreateOrderSuccessfully() {
        // Given
        Long userId = 1L;
        
        SanPham product = SanPham.createForTest("XE001", "Yamaha Exciter", "Xe", 45000000.0, 10, true, false);
        product.setMaSP(1L);

        GioHang item = new GioHang(userId, 1L, 2);
        item.setSanPham(product);

        List<GioHang> cartItems = Arrays.asList(item);
        
        GioHang cart = new GioHang(userId);
        cart.setMaGioHang(1L);
        
        DonHang order = new DonHang(userId, 90000000.0, "PENDING");
        order.setMaDH(1L);

        when(donHangRepository.save(any(DonHang.class))).thenReturn(order);

        CreateOrderFromCartInputData inputData = new CreateOrderFromCartInputData(
            cart, "Test User", "0912345678", "123 Main St", "Test note", null
        );

        // When
        useCase.execute(inputData);

        // Then
        verify(outputBoundary).present(any(CreateOrderFromCartOutputData.class));
        verify(donHangRepository).save(any(DonHang.class));
    }
}
