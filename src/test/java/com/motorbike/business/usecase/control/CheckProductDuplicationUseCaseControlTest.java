package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.checkproductduplication.CheckProductDuplicationInputData;
import com.motorbike.business.dto.checkproductduplication.CheckProductDuplicationOutputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.domain.entities.SanPham;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CheckProductDuplicationUseCaseControlTest {

    @Test
    void shouldDetectDuplicateProductCode() {
        // Given
        String productCode = "XE001";
        String productName = "Yamaha Exciter";
        SanPham existingProduct = SanPham.createForTest(productCode, productName, "Xe", 45000000.0, 10, true, false);
        existingProduct.setMaSP(1L);

        ProductRepository productRepo = new MockProductRepository(existingProduct);
        CheckProductDuplicationUseCaseControl useCase = new CheckProductDuplicationUseCaseControl(null, productRepo);

        CheckProductDuplicationInputData inputData = new CheckProductDuplicationInputData(productName, productCode, null);

        // When
        CheckProductDuplicationOutputData outputData = useCase.checkDuplicationInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertTrue(outputData.isDuplicate());
        assertEquals("code", outputData.getDuplicatedField());
        assertEquals(1L, outputData.getExistingProductId());
    }

    @Test
    void shouldAllowUniqueProductCode() {
        // Given
        String productCode = "XE999";
        String productName = "New Product";

        ProductRepository productRepo = new MockProductRepository(null);
        CheckProductDuplicationUseCaseControl useCase = new CheckProductDuplicationUseCaseControl(null, productRepo);

        CheckProductDuplicationInputData inputData = new CheckProductDuplicationInputData(productName, productCode, null);

        // When
        CheckProductDuplicationOutputData outputData = useCase.checkDuplicationInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertFalse(outputData.isDuplicate());
    }

    @Test
    void shouldAllowSameProductForUpdate() {
        // Given
        String productCode = "XE001";
        String productName = "Yamaha Exciter";
        Long productId = 1L;
        SanPham existingProduct = SanPham.createForTest(productCode, productName, "Xe", 45000000.0, 10, true, false);
        existingProduct.setMaSP(productId);

        ProductRepository productRepo = new MockProductRepository(existingProduct);
        CheckProductDuplicationUseCaseControl useCase = new CheckProductDuplicationUseCaseControl(null, productRepo);

        CheckProductDuplicationInputData inputData = new CheckProductDuplicationInputData(productName, productCode, productId);

        // When
        CheckProductDuplicationOutputData outputData = useCase.checkDuplicationInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertFalse(outputData.isDuplicate());
    }

    private static class MockProductRepository implements ProductRepository {
        private final SanPham product;

        public MockProductRepository(SanPham product) {
            this.product = product;
        }

        @Override
        public Optional<SanPham> findById(Long id) {
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
            if (product != null && product.getTenSanPham().equals(tenSanPham)) {
                return Optional.of(product);
            }
            return Optional.empty();
        }

        @Override
        public Optional<SanPham> findByMaSanPham(String maSanPham) {
            if (product != null && product.getMaSP() != null) {
                // Assuming maSanPham is the product code stored somewhere
                return Optional.of(product);
            }
            return Optional.empty();
        }
    }
}
