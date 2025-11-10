package com.motorbike.business.usecase.impl;

import com.motorbike.business.entity.Product;
import com.motorbike.business.repository.ProductRepository;
import com.motorbike.business.usecase.GetProductDetailUseCase;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetProductDetailUseCaseImplTest {

    @Test
    void execute_shouldReturnProductDetail_whenProductExists() {
        ProductRepository repo = mock(ProductRepository.class);
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(12345));
        when(repo.findById(1L)).thenReturn(Optional.of(product));

        GetProductDetailUseCaseImpl useCase = new GetProductDetailUseCaseImpl(repo);
        GetProductDetailUseCase.ProductDetailRequest req = new GetProductDetailUseCase.ProductDetailRequest(1L);

        GetProductDetailUseCase.ProductDetailResponse resp = useCase.execute(req);

        assertNotNull(resp);
        assertEquals(1L, resp.getId());
        assertEquals("Test Product", resp.getName());
    }

    @Test
    void execute_shouldThrowNotFound_whenProductMissing() {
        ProductRepository repo = mock(ProductRepository.class);
        when(repo.findById(99L)).thenReturn(Optional.empty());

        GetProductDetailUseCaseImpl useCase = new GetProductDetailUseCaseImpl(repo);
        GetProductDetailUseCase.ProductDetailRequest req = new GetProductDetailUseCase.ProductDetailRequest(99L);

        assertThrows(ProductNotFoundException.class, () -> useCase.execute(req));
    }
}
