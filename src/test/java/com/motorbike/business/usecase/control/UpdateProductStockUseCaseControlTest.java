package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.updateproductstock.UpdateProductStockInputData;
import com.motorbike.business.dto.updateproductstock.UpdateProductStockOutputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.entities.XeMay;
import com.motorbike.domain.entities.PhuKienXeMay;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateProductStockUseCaseControlTest {

    // UpdateProductStock use case expects productId, quantityChange, and operation
    
    static class MockProductRepository implements ProductRepository {
        private SanPham product;
        
        public void setProduct(SanPham product) {
            this.product = product;
        }
        
        @Override
        public Optional<SanPham> findById(Long id) {
            return product != null && product.getMaSP().equals(id) ? Optional.of(product) : Optional.empty();
        }
        
        @Override
        public SanPham save(SanPham sanPham) {
            this.product = sanPham;
            return sanPham;
        }
        
        @Override
        public java.util.List<SanPham> findAll() { return null; }
        
        @Override
        public void deleteById(Long id) {}
        
        @Override
        public boolean existsById(Long id) { return product != null; }
        
        @Override
        public Optional<SanPham> findByTenSanPham(String tenSanPham) { return Optional.empty(); }
        
        @Override
        public Optional<SanPham> findByMaSanPham(String maSanPham) { return Optional.empty(); }
        
        @Override
        public java.util.List<PhuKienXeMay> findAllAccessories() { return new java.util.ArrayList<>(); }
        
        @Override
        public java.util.List<PhuKienXeMay> searchAccessories(String keyword) { return new java.util.ArrayList<>(); }
        
        @Override
        public java.util.List<XeMay> findAllMotorbikes() { return new java.util.ArrayList<>(); }
        
        @Override
        public java.util.List<XeMay> searchMotorbikes(String keyword) { return new java.util.ArrayList<>(); }
    }

    @Test
    void shouldIncreaseStockSuccessfully() {
        // Given - Increase stock by 5
        Long productId = 1L;
        XeMay product = new XeMay(productId, "Yamaha Exciter", "Xe thể thao",
            BigDecimal.valueOf(45000000), "exciter.jpg", 10, true, null, null,
            "Yamaha", "Exciter", "Đỏ", 2024, 155);

        MockProductRepository productRepo = new MockProductRepository();
        productRepo.setProduct(product);
        UpdateProductStockUseCaseControl useCase = new UpdateProductStockUseCaseControl(null, productRepo);
        UpdateProductStockInputData inputData = new UpdateProductStockInputData(productId, 5, "INCREASE");

        // When
        UpdateProductStockOutputData outputData = useCase.updateInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertEquals("Product stock updated successfully", outputData.getMessage());
        assertEquals(15, outputData.getNewStock()); // 10 + 5
    }

    @Test
    void shouldDecreaseStockSuccessfully() {
        // Given - Decrease stock by 3
        Long productId = 1L;
        XeMay product = new XeMay(productId, "Yamaha Exciter", "Xe thể thao",
            BigDecimal.valueOf(45000000), "exciter.jpg", 20, true, null, null,
            "Yamaha", "Exciter", "Đỏ", 2024, 155);

        MockProductRepository productRepo = new MockProductRepository();
        productRepo.setProduct(product);
        UpdateProductStockUseCaseControl useCase = new UpdateProductStockUseCaseControl(null, productRepo);
        UpdateProductStockInputData inputData = new UpdateProductStockInputData(productId, 3, "DECREASE");

        // When
        UpdateProductStockOutputData outputData = useCase.updateInternal(inputData);

        // Then
        assertTrue(outputData.isSuccess());
        assertEquals("Product stock updated successfully", outputData.getMessage());
        assertEquals(17, outputData.getNewStock()); // 20 - 3
    }
}
