package com.motorbike.business.usecase.control;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.motorbike.adapters.presenters.OrderDetailPresenter;
import com.motorbike.adapters.viewmodels.OrderDetailViewModel;
import com.motorbike.business.dto.orderdetail.OrderDetailInputData;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.business.usecase.output.OrderDetailOutputBoundary;
import com.motorbike.domain.entities.ChiTietDonHang;
import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.entities.TrangThaiDonHang;

public class OrderDetailUseCaseControlTest {

	// Kịch bản 1: Không có dấu vào
	@Test
	@DisplayName("Kịch bản 1: Không có dấu vào")
	public void testExecute_NullInputData_ShouldReturnError() {
		OrderRepository orderRepo = new MockOrderRepository();
		
		OrderDetailViewModel viewModel = new OrderDetailViewModel();
		OrderDetailOutputBoundary outputBoundary = new OrderDetailPresenter(viewModel);
		
		OrderDetailUseCaseControl control = new OrderDetailUseCaseControl(
			outputBoundary, orderRepo
		);
		control.execute(null);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
	}

	// Kịch bản 2: Không tìm thấy đơn hàng đơn hàng
	@Test
	@DisplayName("Kịch bản 2: Không tìm thấy đơn hàng đơn hàng")
	public void testExecute_OrderNotFound_ShouldReturnError() {
		Long orderId = 999L; // Non-existent order
		
		OrderRepository orderRepo = new MockOrderRepository();
		
		OrderDetailViewModel viewModel = new OrderDetailViewModel();
		OrderDetailOutputBoundary outputBoundary = new OrderDetailPresenter(viewModel);
		
		OrderDetailUseCaseControl control = new OrderDetailUseCaseControl(
			outputBoundary, orderRepo
		);
		control.execute(OrderDetailInputData.forAdmin(orderId));
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
	}

	// Kịch bản 3: Quyền truy cập không hợp lệ
	@Test
	@DisplayName("Kịch bản 3: Quyền truy cập không hợp lệ")
	public void testExecute_InvalidPermission_ShouldReturnError() {
		Long orderId = 1L;
		Long differentUserId = 99L; // Different user trying to access
		
		OrderRepository orderRepo = new MockOrderRepository();
		
		OrderDetailViewModel viewModel = new OrderDetailViewModel();
		OrderDetailOutputBoundary outputBoundary = new OrderDetailPresenter(viewModel);
		
		OrderDetailUseCaseControl control = new OrderDetailUseCaseControl(
			outputBoundary, orderRepo
		);
		// User trying to access order that doesn't belong to them
		control.execute(OrderDetailInputData.forUser(orderId, differentUserId));
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
	}

	// Kịch bản 5: Xem chi tiết đơn hàng thành công
	@Test
	@DisplayName("Kịch bản 4: Xem chi tiết đơn hàng thành công")
	public void testExecute_ValidInput_ShouldReturnOrderDetail() {
		Long orderId = 1L;
		
		OrderRepository orderRepo = new MockOrderRepository();
		
		OrderDetailViewModel viewModel = new OrderDetailViewModel();
		OrderDetailOutputBoundary outputBoundary = new OrderDetailPresenter(viewModel);
		
		OrderDetailUseCaseControl control = new OrderDetailUseCaseControl(
			outputBoundary, orderRepo
		);
		control.execute(OrderDetailInputData.forAdmin(orderId));
		
		assertEquals(true, viewModel.success);
		assertEquals(false, viewModel.hasError);
		assertNotNull(viewModel.orderId);
		assertEquals(orderId, viewModel.orderId);
	}

	// Mock OrderRepository implementation
	private static class MockOrderRepository implements OrderRepository {
		@Override
		public Optional<DonHang> findById(Long orderId) {
			if (orderId == null || orderId == 999L) {
				return Optional.empty();
			}
			
			if (orderId == 1L) {
				DonHang order = new DonHang(
					1L,
					1L, // Owner user ID is 1
					new ArrayList<>(),
					new BigDecimal("500000"),
					TrangThaiDonHang.CHO_XAC_NHAN,
					"Nguyen Van A",
					"0123456789",
					"123 Street",
					"Note",
					LocalDateTime.now().minusDays(1),
					LocalDateTime.now().minusDays(1)
				);
				
				// Add order items
				List<ChiTietDonHang> items = new ArrayList<>();
				items.add(new ChiTietDonHang(1L, "Product A", new BigDecimal("200000"), 1));
				items.add(new ChiTietDonHang(2L, "Product B", new BigDecimal("300000"), 1));
				order.setDanhSachSanPham(items);
				
				return Optional.of(order);
			}
			
			return Optional.empty();
		}

		@Override
		public DonHang save(DonHang donHang) {
			return donHang;
		}

		@Override
		public List<DonHang> findByUserId(Long userId) {
			return new ArrayList<>();
		}

		@Override
		public List<DonHang> findByStatus(TrangThaiDonHang trangThai) {
			return new ArrayList<>();
		}

		@Override
		public List<DonHang> findByUserIdAndStatus(Long userId, TrangThaiDonHang trangThai) {
			return new ArrayList<>();
		}

		@Override
		public List<DonHang> findAll() {
			return new ArrayList<>();
		}

		@Override
		public List<DonHang> searchForAdmin(String keyword) {
			return new ArrayList<>();
		}

		@Override
		public void deleteById(Long orderId) {
		}

		@Override
		public boolean existsById(Long orderId) {
			return false;
		}
	}
}
