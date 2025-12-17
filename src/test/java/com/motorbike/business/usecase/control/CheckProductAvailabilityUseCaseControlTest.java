package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.checkproductavailability.CheckProductAvailabilityInputData;
import com.motorbike.business.dto.checkproductavailability.CheckProductAvailabilityOutputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.domain.entities.SanPham;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CheckProductAvailabilityUseCaseControlTest {

    @Test
    void shouldConfirmProductIsAvailable() {
        // Given
        SanPham product = SanPham.createForTest("XE001", "Yamaha Exciter", "Xe", 45000000.0, 10, true, false);
        product.setMaSP(1L);

        ProductRepository productRepo = new MockProductRepository(product);
        CheckProductAvailabilityUseCaseControl useCase = new CheckProductAvailabilityUseCaseControl(null, productRepo);

        CheckProductAvailabilityInputData inputData = new CheckProductAvailabilityInputData(1L, 5);

        // When
        CheckProductAvailabilityOutputData outputData = useCase.checkInternal(inputData);

        // Then
        assertTrue(outputData.isAvailable());
        assertEquals(1L, outputData.getProductId());
        assertEquals(10, outputData.getAvailableQuantity());
    }

    @Test
    void shouldConfirmProductIsNotAvailable() {
        // Given
        SanPham product = SanPham.createForTest("XE001", "Yamaha Exciter", "Xe", 45000000.0, 0, false, false);
        product.setMaSP(1L);

        ProductRepository productRepo = new MockProductRepository(product);
        CheckProductAvailabilityUseCaseControl useCase = new CheckProductAvailabilityUseCaseControl(null, productRepo);

        CheckProductAvailabilityInputData inputData = new CheckProductAvailabilityInputData(1L, 5);

        // When
        CheckProductAvailabilityOutputData outputData = useCase.checkInternal(inputData);

        // Then
        assertFalse(outputData.isAvailable());
        assertEquals(1L, outputData.getProductId());
    }

    @Test
    void shouldFailWhenProductNotFound() {
        // Given
        ProductRepository productRepo = new MockProductRepository(null);
        CheckProductAvailabilityUseCaseControl useCase = new CheckProductAvailabilityUseCaseControl(null, productRepo);

        CheckProductAvailabilityInputData inputData = new CheckProductAvailabilityInputData(999L, 5);

        // When
        CheckProductAvailabilityOutputData outputData = useCase.checkInternal(inputData);

        // Then
        assertFalse(outputData.isAvailable());
    }

    private static class MockProductRepository implements ProductRepository {
        private final SanPham product;

        public MockProductRepository(SanPham product) {
            this.product = product;
        }

        @Override
        public Optional<SanPham> findById(Long id) {
            if (product != null && product.getMaSanPham().equals(id)) {
                return Optional.of(product);
            }
            return Optional.empty();
        }

        @Override
        public SanPham save(SanPham product) {
            return product;
        }

        @Override
        public boolean existsById(Long productId) {
            return false;
        }

        @Override
        public java.util.List<SanPham> findAll() {
            return new java.util.ArrayList<>();
        }

        @Override
        public void deleteById(Long productId) {
        }

        @Override
        public java.util.List<com.motorbike.domain.entities.PhuKienXeMay> findAllAccessories() {
            return new java.util.ArrayList<>();
        }

        @Override
        public java.util.List<com.motorbike.domain.entities.PhuKienXeMay> searchAccessories(String keyword) {
            return new java.util.ArrayList<>();
        }

        @Override
        public java.util.List<com.motorbike.domain.entities.XeMay> findAllMotorbikes() {
            return new java.util.ArrayList<>();
        }

        @Override
        public java.util.List<com.motorbike.domain.entities.XeMay> searchMotorbikes(String keyword) {
            return new java.util.ArrayList<>();
        }

        @Override
        public Optional<SanPham> findByTenSanPham(String tenSanPham) {
            return Optional.empty();
        }

        @Override
        public Optional<SanPham> findByMaSanPham(String maSanPham) {
            return Optional.empty();
        }
    }
}
