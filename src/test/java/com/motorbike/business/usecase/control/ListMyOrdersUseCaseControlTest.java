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

import com.motorbike.adapters.presenters.ListMyOrdersPresenter;
import com.motorbike.adapters.viewmodels.ListMyOrdersViewModel;
import com.motorbike.business.dto.listmyorders.ListMyOrdersInputData;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.business.usecase.output.ListMyOrdersOutputBoundary;
import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.entities.TrangThaiDonHang;

public class ListMyOrdersUseCaseControlTest {

	// Kịch bản 1: Không có dấu vào (Null input data)
	@Test
	@DisplayName("Kịch bản 1: Không có dấu vào")
	public void testExecute_NullInputData_ShouldReturnError() {
		ListMyOrdersInputData inputData = null;
		
		OrderRepository orderRepo = new MockOrderRepository();
		
		ListMyOrdersViewModel viewModel = new ListMyOrdersViewModel();
		ListMyOrdersOutputBoundary outputBoundary = new ListMyOrdersPresenter(viewModel);
		
		ListMyOrdersUseCaseControl control = new ListMyOrdersUseCaseControl(
			outputBoundary, orderRepo
		);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
		assertEquals("INVALID_USER_ID", viewModel.errorCode);
	}

	// Kịch bản 2: Không có id người dùng (Null user ID)
	@Test
	@DisplayName("Kịch bản 2: Không có id người dùng")
	public void testExecute_NullUserId_ShouldReturnError() {
		ListMyOrdersInputData inputData = ListMyOrdersInputData.forUser(null);
		
		OrderRepository orderRepo = new MockOrderRepository();
		
		ListMyOrdersViewModel viewModel = new ListMyOrdersViewModel();
		ListMyOrdersOutputBoundary outputBoundary = new ListMyOrdersPresenter(viewModel);
		
		ListMyOrdersUseCaseControl control = new ListMyOrdersUseCaseControl(
			outputBoundary, orderRepo
		);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
		assertEquals("INVALID_USER_ID", viewModel.errorCode);
	}

	// Kịch bản 3: Lấy đơn hàng thất bại (Get order failed - Repository throws exception)
	@Test
	@DisplayName("Kịch bản 3: Lấy đơn hàng thất bại")
	public void testExecute_RepositoryThrowsException_ShouldReturnError() {
		Long userId = 1L;
		ListMyOrdersInputData inputData = ListMyOrdersInputData.forUser(userId);
		
		OrderRepository orderRepo = new MockOrderRepository();
		// Mock repository throws exception
		
		ListMyOrdersViewModel viewModel = new ListMyOrdersViewModel();
		ListMyOrdersOutputBoundary outputBoundary = new ListMyOrdersPresenter(viewModel);
		
		ListMyOrdersUseCaseControl control = new ListMyOrdersUseCaseControl(
			outputBoundary, orderRepo
		);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
		assertEquals("SYSTEM_ERROR", viewModel.errorCode);
	}

	// Kịch bản 4: Lấy đơn hàng thành công (Get order successfully)
	@Test
	@DisplayName("Kịch bản 4: Lấy đơn hàng thành công")
	public void testExecute_ValidUserId_ShouldReturnOrders() {
		Long userId = 1L;
		ListMyOrdersInputData inputData = ListMyOrdersInputData.forUser(userId);
		
		OrderRepository orderRepo = new MockOrderRepository();
		
		ListMyOrdersViewModel viewModel = new ListMyOrdersViewModel();
		ListMyOrdersOutputBoundary outputBoundary = new ListMyOrdersPresenter(viewModel);
		
		ListMyOrdersUseCaseControl control = new ListMyOrdersUseCaseControl(
			outputBoundary, orderRepo
		);
		control.execute(inputData);
		
		assertEquals(true, viewModel.success);
		assertEquals(false, viewModel.hasError);
		assertNotNull(viewModel.orders);
	}


	// Mock OrderRepository implementation
	private static class MockOrderRepository implements OrderRepository {
		@Override
		public Optional<DonHang> findById(Long orderId) {
			return Optional.empty();
		}

		@Override
		public DonHang save(DonHang donHang) {
			return donHang;
		}

		@Override
		public List<DonHang> findByUserId(Long userId) {
			if (userId == null || userId == 999L) {
				return new ArrayList<>();
			}
			
			List<DonHang> orders = new ArrayList<>();
			
			if (userId == 1L) {
				DonHang order1 = new DonHang(
					1L,
					userId,
					new ArrayList<>(),
					new BigDecimal("1000000"),
					TrangThaiDonHang.CHO_XAC_NHAN,
					"Nguyen Van A",
					"0123456789",
					"123 Street",
					"Note 1",
					LocalDateTime.now().minusDays(1),
					LocalDateTime.now().minusDays(1)
				);
				orders.add(order1);
				
				DonHang order2 = new DonHang(
					2L,
					userId,
					new ArrayList<>(),
					new BigDecimal("2000000"),
					TrangThaiDonHang.DA_GIAO,
					"Nguyen Van A",
					"0123456789",
					"456 Avenue",
					"Note 2",
					LocalDateTime.now().minusDays(2),
					LocalDateTime.now().minusDays(2)
				);
				orders.add(order2);
			}
			
			return orders;
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
