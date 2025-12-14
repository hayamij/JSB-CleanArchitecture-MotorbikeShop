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

import com.motorbike.adapters.presenters.ListAllOrdersPresenter;
import com.motorbike.adapters.viewmodels.ListAllOrdersViewModel;
import com.motorbike.business.dto.listallorders.ListAllOrdersInputData;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.business.usecase.output.ListAllOrdersOutputBoundary;
import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.entities.TrangThaiDonHang;

public class ListAllOrdersUseCaseControlTest {

	// Kịch bản 1: Không có dấu vào
	@Test
	@DisplayName("Kịch bản 1: Không có dấu vào")
	public void testExecute_NullInputData_ShouldReturnError() {
		OrderRepository orderRepo = new MockOrderRepository();
		
		ListAllOrdersViewModel viewModel = new ListAllOrdersViewModel();
		ListAllOrdersOutputBoundary outputBoundary = new ListAllOrdersPresenter(viewModel);
		
		ListAllOrdersUseCaseControl control = new ListAllOrdersUseCaseControl(
			outputBoundary, orderRepo
		);
		control.execute(null);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
	}

	// Kịch bản 2: Không phải là Admin
	@Test
	@DisplayName("Kịch bản 2: Không phải là Admin")
	public void testExecute_NotAdmin_ShouldReturnError() {
		OrderRepository orderRepo = new MockOrderRepository();
		
		ListAllOrdersViewModel viewModel = new ListAllOrdersViewModel();
		ListAllOrdersOutputBoundary outputBoundary = new ListAllOrdersPresenter(viewModel);
		
		ListAllOrdersUseCaseControl control = new ListAllOrdersUseCaseControl(
			outputBoundary, orderRepo
		);
		// Using forNonAdmin() to test non-admin user
		control.execute(ListAllOrdersInputData.forNonAdmin());
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
	}

	// Kịch bản 3: Lấy đơn hàng thất bại
	@Test
	@DisplayName("Kịch bản 3: Lấy đơn hàng thất bại")
	public void testExecute_RepositoryError_ShouldReturnError() {
		OrderRepository orderRepo = new MockOrderRepositoryWithError();
		
		ListAllOrdersViewModel viewModel = new ListAllOrdersViewModel();
		ListAllOrdersOutputBoundary outputBoundary = new ListAllOrdersPresenter(viewModel);
		
		ListAllOrdersUseCaseControl control = new ListAllOrdersUseCaseControl(
			outputBoundary, orderRepo
		);
		control.execute(ListAllOrdersInputData.forAdmin());
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
	}

	// Kịch bản 4: Lấy đơn hàng thành công
	@Test
	@DisplayName("Kịch bản 4: Lấy đơn hàng thành công")
	public void testExecute_ValidInput_ShouldReturnOrders() {
		OrderRepository orderRepo = new MockOrderRepository();
		
		ListAllOrdersViewModel viewModel = new ListAllOrdersViewModel();
		ListAllOrdersOutputBoundary outputBoundary = new ListAllOrdersPresenter(viewModel);
		
		ListAllOrdersUseCaseControl control = new ListAllOrdersUseCaseControl(
			outputBoundary, orderRepo
		);
		control.execute(ListAllOrdersInputData.forAdmin());
		
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
			List<DonHang> orders = new ArrayList<>();
			
			for (int i = 1; i <= 3; i++) {
				DonHang order = new DonHang(
					(long) i,
					(long) i,
					new ArrayList<>(),
					new BigDecimal(String.valueOf(1000000 * i)),
					TrangThaiDonHang.CHO_XAC_NHAN,
					"Customer " + i,
					"012345678" + i,
					"Address " + i,
					"Note " + i,
					LocalDateTime.now().minusDays(i),
					LocalDateTime.now().minusDays(i)
				);
				orders.add(order);
			}
			
			return orders;
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

	// Mock OrderRepository that throws error
	private static class MockOrderRepositoryWithError implements OrderRepository {
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
			throw new RuntimeException("Database connection error");
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
