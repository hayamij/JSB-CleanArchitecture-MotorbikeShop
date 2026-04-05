package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.topproducts.GetTopProductsInputData;
import com.motorbike.business.dto.topproducts.GetTopProductsOutputData;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.business.usecase.output.GetTopProductsOutputBoundary;
import com.motorbike.domain.entities.ProductSalesStats;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GetTopProductsUseCaseControlTest {

    @Test
    void shouldGetTopProductsSuccessfully() {
        // Given
        ProductSalesStats stats1 = new ProductSalesStats(1L, "Yamaha Exciter", 100);
        ProductSalesStats stats2 = new ProductSalesStats(2L, "Mũ bảo hiểm", 50);

        List<ProductSalesStats> topProducts = Arrays.asList(stats1, stats2);

        OrderRepository orderRepo = new MockOrderRepository(topProducts);
        MockOutputBoundary outputBoundary = new MockOutputBoundary();
        GetTopProductsUseCaseControl useCase = new GetTopProductsUseCaseControl(outputBoundary, orderRepo);

        GetTopProductsInputData inputData = new GetTopProductsInputData(10);

        // When
        useCase.execute(inputData);

        // Then
        GetTopProductsOutputData outputData = outputBoundary.getOutputData();
        assertNotNull(outputData);
        assertTrue(outputData.isSuccess());
        assertEquals(2, outputData.getProducts().size());
        assertEquals("Yamaha Exciter", outputData.getProducts().get(0).getProductName());
        assertEquals(100, outputData.getProducts().get(0).getTotalSold());
    }

    @Test
    void shouldHandleNoProducts() {
        // Given
        List<ProductSalesStats> topProducts = Collections.emptyList();

        OrderRepository orderRepo = new MockOrderRepository(topProducts);
        MockOutputBoundary outputBoundary = new MockOutputBoundary();
        GetTopProductsUseCaseControl useCase = new GetTopProductsUseCaseControl(outputBoundary, orderRepo);

        GetTopProductsInputData inputData = new GetTopProductsInputData(10);

        // When
        useCase.execute(inputData);

        // Then
        GetTopProductsOutputData outputData = outputBoundary.getOutputData();
        assertNotNull(outputData);
        assertTrue(outputData.isSuccess());
        assertEquals(0, outputData.getProducts().size());
    }

    @Test
    void shouldHandleDifferentLimit() {
        // Given
        ProductSalesStats stats1 = new ProductSalesStats(1L, "Product 1", 100);
        ProductSalesStats stats2 = new ProductSalesStats(2L, "Product 2", 80);
        ProductSalesStats stats3 = new ProductSalesStats(3L, "Product 3", 60);

        List<ProductSalesStats> topProducts = Arrays.asList(stats1, stats2, stats3);

        OrderRepository orderRepo = new MockOrderRepository(topProducts);
        MockOutputBoundary outputBoundary = new MockOutputBoundary();
        GetTopProductsUseCaseControl useCase = new GetTopProductsUseCaseControl(outputBoundary, orderRepo);

        GetTopProductsInputData inputData = new GetTopProductsInputData(5);

        // When
        useCase.execute(inputData);

        // Then
        GetTopProductsOutputData outputData = outputBoundary.getOutputData();
        assertNotNull(outputData);
        assertTrue(outputData.isSuccess());
        assertEquals(3, outputData.getProducts().size());
    }

    private static class MockOutputBoundary implements GetTopProductsOutputBoundary {
        private GetTopProductsOutputData outputData;

        @Override
        public void present(GetTopProductsOutputData outputData) {
            this.outputData = outputData;
        }

        public GetTopProductsOutputData getOutputData() {
            return outputData;
        }
    }

    private static class MockOrderRepository implements OrderRepository {
        private final List<ProductSalesStats> topProducts;

        public MockOrderRepository(List<ProductSalesStats> topProducts) {
            this.topProducts = topProducts;
        }

        @Override
        public List<ProductSalesStats> getTopSellingProducts(int limit) {
            return topProducts;
        }

        @Override
        public java.util.Optional<com.motorbike.domain.entities.DonHang> findById(Long id) {
            return java.util.Optional.empty();
        }

        @Override
        public com.motorbike.domain.entities.DonHang save(com.motorbike.domain.entities.DonHang donHang) {
            return donHang;
        }

        @Override
        public void deleteById(Long id) {
        }

        @Override
        public List<com.motorbike.domain.entities.DonHang> findAll() {
            return Collections.emptyList();
        }

        @Override
        public boolean existsById(Long id) {
            return false;
        }

        @Override
        public List<com.motorbike.domain.entities.DonHang> findByUserId(Long userId) {
            return Collections.emptyList();
        }

        @Override
        public List<com.motorbike.domain.entities.DonHang> findByStatus(com.motorbike.domain.entities.TrangThaiDonHang trangThai) {
            return Collections.emptyList();
        }

        @Override
        public List<com.motorbike.domain.entities.DonHang> findByUserIdAndStatus(Long userId, com.motorbike.domain.entities.TrangThaiDonHang trangThai) {
            return Collections.emptyList();
        }

        @Override
        public List<com.motorbike.domain.entities.DonHang> searchOrders(String keyword) {
            return Collections.emptyList();
        }
    }
}
