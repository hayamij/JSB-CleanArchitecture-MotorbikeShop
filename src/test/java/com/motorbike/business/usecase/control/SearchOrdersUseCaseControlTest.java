package com.motorbike.business.usecase.control;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.motorbike.adapters.presenters.SearchOrdersPresenter;
import com.motorbike.adapters.viewmodels.SearchOrdersViewModel;
import com.motorbike.business.dto.order.SearchOrdersInputData;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.business.ports.repository.UserRepository;
import com.motorbike.business.usecase.output.SearchOrdersOutputBoundary;
import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.entities.PhuongThucThanhToan;
import com.motorbike.domain.entities.TrangThaiDonHang;

public class SearchOrdersUseCaseControlTest {

	@Test
	public void testExecute_WithKeyword_Success() {
		SearchOrdersInputData inputData = new SearchOrdersInputData("DH001", null, null);
		
		OrderRepository orderRepo = new MockOrderRepository();
		UserRepository userRepo = new MockUserRepository();
		SearchOrdersViewModel viewModel = new SearchOrdersViewModel();
		SearchOrdersOutputBoundary outputBoundary = new SearchOrdersPresenter(viewModel);
		
		SearchOrdersUseCaseControl control = new SearchOrdersUseCaseControl(outputBoundary, orderRepo, userRepo);
		control.execute(inputData);
		
		assertTrue(viewModel.success);
		assertFalse(viewModel.hasError);
		assertNotNull(viewModel.orders);
		assertEquals(1, viewModel.orders.size());
	}

	@Test
	public void testExecute_NoResults() {
		SearchOrdersInputData inputData = new SearchOrdersInputData("xyz123", null, null);
		
		OrderRepository orderRepo = new MockOrderRepository();
		UserRepository userRepo = new MockUserRepository();
		SearchOrdersViewModel viewModel = new SearchOrdersViewModel();
		SearchOrdersOutputBoundary outputBoundary = new SearchOrdersPresenter(viewModel);
		
		SearchOrdersUseCaseControl control = new SearchOrdersUseCaseControl(outputBoundary, orderRepo, userRepo);
		control.execute(inputData);
		
		assertTrue(viewModel.success);
		assertFalse(viewModel.hasError);
		assertNotNull(viewModel.orders);
		assertEquals(0, viewModel.orders.size());
	}

	@Test
	public void testExecute_NullInputData() {
		OrderRepository orderRepo = new MockOrderRepository();
		UserRepository userRepo = new MockUserRepository();
		SearchOrdersViewModel viewModel = new SearchOrdersViewModel();
		SearchOrdersOutputBoundary outputBoundary = new SearchOrdersPresenter(viewModel);
		
		SearchOrdersUseCaseControl control = new SearchOrdersUseCaseControl(outputBoundary, orderRepo, userRepo);
		control.execute(null);
		
		assertFalse(viewModel.success);
		assertTrue(viewModel.hasError);
	}

	private static class MockOrderRepository implements OrderRepository {
		@Override
		public List<DonHang> searchOrders(String keyword) {
			List<DonHang> orders = new ArrayList<>();
			if ("DH001".equals(keyword)) {
				DonHang order = new DonHang(
					1L,
					1L,
					new ArrayList<>(),
					java.math.BigDecimal.valueOf(5000000),
					TrangThaiDonHang.CHO_XAC_NHAN,
					"Nguyen Van A",
					"0912345678",
					"123 Street",
					"",
					PhuongThucThanhToan.THANH_TOAN_TRUC_TIEP,
					LocalDateTime.now(),
					LocalDateTime.now()
				);
				orders.add(order);
			}
			return orders;
		}

		@Override
		public Optional<DonHang> findById(Long id) {
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
		public boolean existsById(Long orderId) {
			return false;
		}

		@Override
		public void deleteById(Long orderId) {
		}

		@Override
		public List<DonHang> findAll() {
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
	}

	private static class MockUserRepository implements UserRepository {
	@Override
	public Optional<com.motorbike.domain.entities.TaiKhoan> findByEmail(String email) {
		return Optional.empty();
	}

	@Override
	public Optional<com.motorbike.domain.entities.TaiKhoan> findByUsernameOrEmailOrPhone(String username) {
		return findByEmail(username);
	}

	@Override
	public Optional<com.motorbike.domain.entities.TaiKhoan> findById(Long id) {
		return Optional.empty();
	}		@Override
		public boolean existsByEmail(String email) {
			return false;
		}

		@Override
		public boolean existsByUsername(String username) {
			return false;
		}

		@Override
		public com.motorbike.domain.entities.TaiKhoan save(com.motorbike.domain.entities.TaiKhoan taiKhoan) {
			return taiKhoan;
		}

		@Override
		public void updateLastLogin(Long userId) {
		}

		@Override
		public List<com.motorbike.domain.entities.TaiKhoan> findAll() {
			return new ArrayList<>();
		}

		@Override
		public void deleteById(Long userId) {
		}

		@Override
		public boolean existsById(Long userId) {
			return false;
		}

		@Override
		public List<com.motorbike.domain.entities.TaiKhoan> searchUsers(String keyword) {
			return new ArrayList<>();
		}
	}
}
