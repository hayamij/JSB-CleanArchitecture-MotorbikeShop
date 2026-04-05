package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.validateorder.ValidateOrderInputData;
import com.motorbike.business.dto.validateorder.ValidateOrderOutputData;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.entities.ChiTietDonHang;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ValidateOrderUseCaseControlTest {

    // ValidateOrder use case expects orderId and checks if order exists in DB with items
    
    static class MockOrderRepository implements OrderRepository {
        private DonHang order;
        
        public void setOrder(DonHang order) {
            this.order = order;
        }
        
        @Override
        public Optional<DonHang> findById(Long id) {
            return order != null && order.getMaDonHang().equals(id) ? Optional.of(order) : Optional.empty();
        }
        
        @Override
        public DonHang save(DonHang donHang) { return donHang; }
        
        @Override
        public java.util.List<DonHang> findAll() { return null; }
        
        @Override
        public java.util.List<DonHang> findByUserId(Long userId) { return null; }
        
        @Override
        public void deleteById(Long id) {}
        
        @Override
        public java.util.List<com.motorbike.domain.entities.ProductSalesStats> getTopSellingProducts(int limit) { return null; }
        
        @Override
        public boolean existsById(Long id) { return false; }
        
        @Override
        public java.util.List<DonHang> findByStatus(com.motorbike.domain.entities.TrangThaiDonHang trangThai) { return null; }
        
        @Override
        public java.util.List<DonHang> findByUserIdAndStatus(Long userId, com.motorbike.domain.entities.TrangThaiDonHang trangThai) { return null; }
        
        @Override
        public java.util.List<DonHang> searchOrders(String keyword) { return null; }
    }

    @Test
    void shouldValidateOrderSuccessfully() {
        // Given - Order exists with items
        Long orderId = 1L;
        DonHang order = new DonHang(orderId, 100000.0, "Customer Name");
        order.themSanPham(new ChiTietDonHang(1L, "Product", BigDecimal.valueOf(100000), 1));
        
        MockOrderRepository orderRepo = new MockOrderRepository();
        orderRepo.setOrder(order);
        ValidateOrderUseCaseControl useCase = new ValidateOrderUseCaseControl(null, orderRepo);
        ValidateOrderInputData inputData = new ValidateOrderInputData(orderId);

        // When
        ValidateOrderOutputData outputData = useCase.validateInternal(inputData);

        // Then
        assertTrue(outputData.isValid());
        assertEquals("Order is valid", outputData.getMessage());
        assertEquals(orderId, outputData.getOrderId());
    }

    @Test
    void shouldFailWhenOrderNotFound() {
        // Given - Order doesn't exist
        Long orderId = 999L;
        MockOrderRepository orderRepo = new MockOrderRepository();
        ValidateOrderUseCaseControl useCase = new ValidateOrderUseCaseControl(null, orderRepo);
        ValidateOrderInputData inputData = new ValidateOrderInputData(orderId);

        // When
        ValidateOrderOutputData outputData = useCase.validateInternal(inputData);

        // Then
        assertFalse(outputData.isValid());
        assertNotNull(outputData.getMessage());
    }

    @Test
    void shouldFailWhenOrderHasNoItems() {
        // Given - Order exists but has no items
        Long orderId = 1L;
        DonHang order = new DonHang(orderId, 0.0, "Customer Name");
        // No items added
        
        MockOrderRepository orderRepo = new MockOrderRepository();
        orderRepo.setOrder(order);
        ValidateOrderUseCaseControl useCase = new ValidateOrderUseCaseControl(null, orderRepo);
        ValidateOrderInputData inputData = new ValidateOrderInputData(orderId);

        // When
        ValidateOrderOutputData outputData = useCase.validateInternal(inputData);

        // Then
        assertFalse(outputData.isValid());
        assertNotNull(outputData.getMessage());
    }
    
    @Test
    void shouldFailWhenInputIsNull() {
        // Given - Null input
        MockOrderRepository orderRepo = new MockOrderRepository();
        ValidateOrderUseCaseControl useCase = new ValidateOrderUseCaseControl(null, orderRepo);

        // When
        ValidateOrderOutputData outputData = useCase.validateInternal(null);

        // Then
        assertFalse(outputData.isValid());
        assertNotNull(outputData.getMessage());
    }
}
