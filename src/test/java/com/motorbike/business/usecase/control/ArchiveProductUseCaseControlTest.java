package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.archiveproduct.ArchiveProductInputData;
import com.motorbike.business.dto.archiveproduct.ArchiveProductOutputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.domain.entities.SanPham;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ArchiveProductUseCaseControlTest {

    @Test
    void shouldArchiveProductSuccessfully() {
        // Given
        SanPham product = SanPham.createForTest("XE001", "Yamaha Exciter", "Xe", 45000000.0, 10, true, false);
        product.setMaSP(1L);

        ProductRepository productRepo = new MockProductRepository(product);
        ArchiveProductUseCaseControl useCase = new ArchiveProductUseCaseControl(null, productRepo);

        ArchiveProductInputData inputData = new ArchiveProductInputData(1L);

        // When
        ArchiveProductOutputData outputData = useCase.archiveInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertEquals(1L, outputData.getProductId());
        assertEquals("Yamaha Exciter", outputData.getProductName());
        assertNotEquals(null, outputData.getArchivedAt());
    }

    @Test
    void shouldFailWhenProductNotFound() {
        // Given
        ProductRepository productRepo = new MockProductRepository(null);
        ArchiveProductUseCaseControl useCase = new ArchiveProductUseCaseControl(null, productRepo);

        ArchiveProductInputData inputData = new ArchiveProductInputData(999L);

        // When
        ArchiveProductOutputData outputData = useCase.archiveInternal(inputData);

        // Then
        assertFalse(outputData.isSuccess());
        assertNotEquals(null, outputData.getErrorCode());
        assertNotEquals(null, outputData.getErrorMessage());
    }

    @Test
    void shouldFailWhenProductIdIsNull() {
        // Given
        ProductRepository productRepo = new MockProductRepository(null);
        ArchiveProductUseCaseControl useCase = new ArchiveProductUseCaseControl(null, productRepo);

        ArchiveProductInputData inputData = new ArchiveProductInputData((Long) null);

        // When
        ArchiveProductOutputData outputData = useCase.archiveInternal(inputData);

        // Then
        assertFalse(outputData.isSuccess());
        assertNotEquals(null, outputData.getErrorCode());
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
