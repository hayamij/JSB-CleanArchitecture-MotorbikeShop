package com.motorbike.business.usecase.control;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.motorbike.adapters.presenters.SearchAdminOrderPresenter;
import com.motorbike.adapters.viewmodels.SearchAdminOrderViewModel;
import com.motorbike.business.dto.searchadminorder.SearchAdminOrderInputData;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.entities.TrangThaiDonHang;

public class SearchAdminOrderUseCaseControlTest {

    @Test
    @DisplayName("Kịch bản 1: Quyền truy cập không hợp lệ")
    public void testExecute_InvalidAccess() {
        SearchAdminOrderInputData inputData = SearchAdminOrderInputData.forNonAdmin("nguyen");
        
        OrderRepository orderRepo = new MockOrderRepository();
        SearchAdminOrderViewModel viewModel = new SearchAdminOrderViewModel();
        SearchAdminOrderPresenter presenter = new SearchAdminOrderPresenter(viewModel);
        
        SearchAdminOrderUseCaseControl useCase = new SearchAdminOrderUseCaseControl(presenter, orderRepo);
        useCase.execute(inputData);
        
        assertEquals(false, viewModel.success);
        assertEquals(true, viewModel.errorCode != null);
    }

    @Test
    @DisplayName("Kịch bản 2: Không có dữ liệu vào")
    public void testExecute_NullInputData() {
        SearchAdminOrderInputData inputData = null;
        
        OrderRepository orderRepo = new MockOrderRepository();
        SearchAdminOrderViewModel viewModel = new SearchAdminOrderViewModel();
        SearchAdminOrderPresenter presenter = new SearchAdminOrderPresenter(viewModel);
        
        SearchAdminOrderUseCaseControl useCase = new SearchAdminOrderUseCaseControl(presenter, orderRepo);
        useCase.execute(inputData);
        
        assertEquals(false, viewModel.success);
        assertEquals(true, viewModel.errorCode != null);
    }

    @Test
    @DisplayName("Kịch bản 3: Không tìm thấy đơn hàng")
    public void testExecute_OrderNotFound() {
        SearchAdminOrderInputData inputData = SearchAdminOrderInputData.forAdmin("notfound");
        
        OrderRepository orderRepo = new MockOrderRepository();
        SearchAdminOrderViewModel viewModel = new SearchAdminOrderViewModel();
        SearchAdminOrderPresenter presenter = new SearchAdminOrderPresenter(viewModel);
        
        SearchAdminOrderUseCaseControl useCase = new SearchAdminOrderUseCaseControl(presenter, orderRepo);
        useCase.execute(inputData);
        
        assertEquals(true, viewModel.success);
        assertEquals(true, viewModel.orders != null);
        assertEquals(0, viewModel.orders.size());
    }

    @Test
    @DisplayName("Kịch bản 4: Tìm đơn hàng thành công")
    public void testExecute_FindOrderSuccessfully() {
        SearchAdminOrderInputData inputData = SearchAdminOrderInputData.forAdmin("nguyen");
        
        OrderRepository orderRepo = new MockOrderRepository();
        SearchAdminOrderViewModel viewModel = new SearchAdminOrderViewModel();
        SearchAdminOrderPresenter presenter = new SearchAdminOrderPresenter(viewModel);
        
        SearchAdminOrderUseCaseControl useCase = new SearchAdminOrderUseCaseControl(presenter, orderRepo);
        useCase.execute(inputData);
        
        assertEquals(true, viewModel.success);
        assertEquals(true, viewModel.orders != null);
        assertEquals(2, viewModel.orders.size());
    }

    // Mock implementation of OrderRepository for testing
    private static class MockOrderRepository implements OrderRepository {
        @Override
        public DonHang save(DonHang donHang) {
            return donHang;
        }

        @Override
        public Optional<DonHang> findById(Long id) {
            return Optional.empty();
        }

        @Override
        public List<DonHang> findByUserId(Long userId) {
            return List.of();
        }

        @Override
        public List<DonHang> findByStatus(TrangThaiDonHang trangThai) {
            return List.of();
        }

        @Override
        public List<DonHang> findByUserIdAndStatus(Long userId, TrangThaiDonHang trangThai) {
            return List.of();
        }

        @Override
        public List<DonHang> findAll() {
            return List.of();
        }

        @Override
        public void deleteById(Long orderId) {
            // No-op for testing
        }

        @Override
        public boolean existsById(Long orderId) {
            return false;
        }

        @Override
        public List<DonHang> searchForAdmin(String query) {
            if (query == null || query.isEmpty()) {
                return List.of();
            }
            
            if (query.equalsIgnoreCase("nguyen")) {
                List<DonHang> orders = new ArrayList<>();
                
                DonHang order1 = new DonHang(
                    1L,
                    1L,
                    new ArrayList<>(),
                    new BigDecimal("50000000"),
                    TrangThaiDonHang.CHO_XAC_NHAN,
                    "Nguyen Van A",
                    "0123456789",
                    "123 Test Street",
                    "Test note",
                    LocalDateTime.now(),
                    LocalDateTime.now()
                );
                
                DonHang order2 = new DonHang(
                    3L,
                    3L,
                    new ArrayList<>(),
                    new BigDecimal("70000000"),
                    TrangThaiDonHang.DANG_GIAO,
                    "Nguyen Thi C",
                    "0123999888",
                    "789 Test Street",
                    "Another note",
                    LocalDateTime.now(),
                    LocalDateTime.now()
                );
                
                orders.add(order1);
                orders.add(order2);
                return orders;
            }
            
            return List.of();
        }
    }
}
