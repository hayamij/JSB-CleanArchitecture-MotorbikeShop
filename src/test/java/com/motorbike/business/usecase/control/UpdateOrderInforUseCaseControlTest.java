package com.motorbike.business.usecase.control;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.motorbike.adapters.presenters.UpdateOrderInforPresenter;
import com.motorbike.adapters.viewmodels.UpdateOrderInforViewModel;
import com.motorbike.business.dto.updateorderinfor.UpdateOrderInforInputData;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.business.usecase.output.UpdateOrderInforOutputBoundary;
import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.entities.TrangThaiDonHang;

public class UpdateOrderInforUseCaseControlTest {

	// Kịch bản 1: Không có dấu vào
	@Test
	@DisplayName("Kịch bản 1: Không có dấu vào")
	public void testExecute_NullInputData_ShouldReturnError() {
		OrderRepository orderRepo = new MockOrderRepository();
		
		UpdateOrderInforViewModel viewModel = new UpdateOrderInforViewModel();
		UpdateOrderInforOutputBoundary outputBoundary = new UpdateOrderInforPresenter(viewModel);
		
		UpdateOrderInforUseCaseControl control = new UpdateOrderInforUseCaseControl(
			outputBoundary, orderRepo
		);
		control.execute(null);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
	}

	// Kịch bản 2: Lỗi nhập liệu
	@Test
	@DisplayName("Kịch bản 2: Lỗi nhập liệu")
	public void testExecute_InvalidInput_ShouldReturnError() {
		Long orderId = 1L;
		Long userId = 1L;
		
		// Test with invalid receiver name (empty)
		UpdateOrderInforInputData inputData = new UpdateOrderInforInputData(
			orderId,
			userId,
			"",  // Empty name
			"0123456789",
			"123 Street",
			"Note"
		);
		
		OrderRepository orderRepo = new MockOrderRepository();
		
		UpdateOrderInforViewModel viewModel = new UpdateOrderInforViewModel();
		UpdateOrderInforOutputBoundary outputBoundary = new UpdateOrderInforPresenter(viewModel);
		
		UpdateOrderInforUseCaseControl control = new UpdateOrderInforUseCaseControl(
			outputBoundary, orderRepo
		);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
	}

	// Kịch bản 3: Không tìm thấy đơn hàng
	@Test
	@DisplayName("Kịch bản 3: Không tìm thấy đơn hàng")
	public void testExecute_OrderNotFound_ShouldReturnError() {
		Long orderId = 999L;  // Non-existent order
		Long userId = 1L;
		
		UpdateOrderInforInputData inputData = new UpdateOrderInforInputData(
			orderId,
			userId,
			"Nguyen Van A",
			"0123456789",
			"123 Street",
			"Note"
		);
		
		OrderRepository orderRepo = new MockOrderRepository();
		
		UpdateOrderInforViewModel viewModel = new UpdateOrderInforViewModel();
		UpdateOrderInforOutputBoundary outputBoundary = new UpdateOrderInforPresenter(viewModel);
		
		UpdateOrderInforUseCaseControl control = new UpdateOrderInforUseCaseControl(
			outputBoundary, orderRepo
		);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
	}

	// Kịch bản 4: Quyền truy cập không hợp lệ
	@Test
	@DisplayName("Kịch bản 4: Quyền truy cập không hợp lệ")
	public void testExecute_InvalidPermission_ShouldReturnError() {
		Long orderId = 1L;
		Long differentUserId = 99L;  // Different user trying to update
		
		UpdateOrderInforInputData inputData = new UpdateOrderInforInputData(
			orderId,
			differentUserId,
			"Nguyen Van A",
			"0123456789",
			"123 Street",
			"Note"
		);
		
		OrderRepository orderRepo = new MockOrderRepository();
		
		UpdateOrderInforViewModel viewModel = new UpdateOrderInforViewModel();
		UpdateOrderInforOutputBoundary outputBoundary = new UpdateOrderInforPresenter(viewModel);
		
		UpdateOrderInforUseCaseControl control = new UpdateOrderInforUseCaseControl(
			outputBoundary, orderRepo
		);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
	}

	// Kịch bản 5: Sửa thông tin đơn hàng thành công
	@Test
	@DisplayName("Kịch bản 5: Sửa thông tin đơn hàng thành công")
	public void testExecute_ValidData_ShouldUpdateSuccessfully() {
		Long orderId = 1L;
		Long userId = 1L;
		
		UpdateOrderInforInputData inputData = new UpdateOrderInforInputData(
			orderId,
			userId,
			"Tran Thi B",
			"0987654321",
			"456 Avenue",
			"Giao trong giờ hành chính"
		);
		
		OrderRepository orderRepo = new MockOrderRepository();
		
		UpdateOrderInforViewModel viewModel = new UpdateOrderInforViewModel();
		UpdateOrderInforOutputBoundary outputBoundary = new UpdateOrderInforPresenter(viewModel);
		
		UpdateOrderInforUseCaseControl control = new UpdateOrderInforUseCaseControl(
			outputBoundary, orderRepo
		);
		control.execute(inputData);
		
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
					1L,
					new ArrayList<>(),
					new BigDecimal("1000000"),
					TrangThaiDonHang.CHO_XAC_NHAN,
					"Nguyen Van A",
					"0123456789",
					"123 Street",
					"Old Note",
					LocalDateTime.now().minusDays(1),
					LocalDateTime.now().minusDays(1)
				);
				return Optional.of(order);
			}
			
			return Optional.empty();
		}

		@Override
		public DonHang save(DonHang donHang) {
			return donHang;
		}

		@Override
		public java.util.List<DonHang> findByUserId(Long userId) {
			return new ArrayList<>();
		}

		@Override
		public java.util.List<DonHang> findByStatus(TrangThaiDonHang trangThai) {
			return new ArrayList<>();
		}

		@Override
		public java.util.List<DonHang> findByUserIdAndStatus(Long userId, TrangThaiDonHang trangThai) {
			return new ArrayList<>();
		}

		@Override
		public java.util.List<DonHang> findAll() {
			return new ArrayList<>();
		}

		@Override
		public java.util.List<DonHang> searchForAdmin(String keyword) {
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
