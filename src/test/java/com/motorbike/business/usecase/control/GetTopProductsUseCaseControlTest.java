package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.topproducts.GetTopProductsInputData;
import com.motorbike.business.usecase.output.GetTopProductsOutputBoundary;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.domain.entities.ProductSalesStats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetTopProductsUseCaseControlTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private GetTopProductsOutputBoundary outputBoundary;

    private GetTopProductsUseCaseControl useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetTopProductsUseCaseControl(outputBoundary, orderRepository);
    }

    @Test
    void shouldGetTopProductsSuccessfully() {
        // Given
        ProductSalesStats stats1 = new ProductSalesStats(1L, "Yamaha Exciter", 100);
        ProductSalesStats stats2 = new ProductSalesStats(2L, "Mũ bảo hiểm", 50);

        List<ProductSalesStats> topProducts = Arrays.asList(stats1, stats2);

        when(orderRepository.getTopSellingProducts(10)).thenReturn(topProducts);

        GetTopProductsInputData inputData = new GetTopProductsInputData(10);

        // When
        useCase.execute(inputData);

        // Then
        verify(orderRepository).getTopSellingProducts(10);
        verify(outputBoundary).present(any());
    }

    @Test
    void shouldHandleNoProducts() {
        // Given
        List<ProductSalesStats> topProducts = Arrays.asList();

        when(orderRepository.getTopSellingProducts(anyInt())).thenReturn(topProducts);

        GetTopProductsInputData inputData = new GetTopProductsInputData(10);

        // When
        useCase.execute(inputData);

        // Then
        verify(orderRepository).getTopSellingProducts(10);
    }
}
