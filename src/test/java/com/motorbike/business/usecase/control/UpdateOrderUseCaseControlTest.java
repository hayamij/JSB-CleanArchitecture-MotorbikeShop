package com.motorbike.business.usecase.control;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.motorbike.adapters.presenters.UpdateOrderPresenter;
import com.motorbike.adapters.viewmodels.UpdateOrderViewModel;
import com.motorbike.business.dto.order.UpdateOrderInputData;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.business.usecase.output.UpdateOrderOutputBoundary;
import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.entities.PhuongThucThanhToan;
import com.motorbike.domain.entities.TrangThaiDonHang;

public class UpdateOrderUseCaseControlTest {

	@Test
	public void testExecute_ValidUpdate_Success() {
		UpdateOrderInputData inputData = new UpdateOrderInputData(
			1L,
			"DA_XAC_NHAN",
			null
		);
		
		OrderRepository orderRepo = new MockOrderRepository();
		UpdateOrderViewModel viewModel = new UpdateOrderViewModel();
		UpdateOrderOutputBoundary outputBoundary = new UpdateOrderPresenter(viewModel);
		
		UpdateOrderUseCaseControl control = new UpdateOrderUseCaseControl(outputBoundary, orderRepo);
		control.execute(inputData);
		
		assertTrue(viewModel.success);
		assertFalse(viewModel.hasError);
		assertNotNull(viewModel.maDonHang);
	}

	@Test
	public void testExecute_OrderNotFound() {
		UpdateOrderInputData inputData = new UpdateOrderInputData(
			999L,
			"DANG_GIAO_HANG",
			null
		);
		
		OrderRepository orderRepo = new MockOrderRepository();
		UpdateOrderViewModel viewModel = new UpdateOrderViewModel();
		UpdateOrderOutputBoundary outputBoundary = new UpdateOrderPresenter(viewModel);
		
		UpdateOrderUseCaseControl control = new UpdateOrderUseCaseControl(outputBoundary, orderRepo);
		control.execute(inputData);
		
		assertFalse(viewModel.success);
		assertTrue(viewModel.hasError);
	}

	@Test
	public void testExecute_NullInputData() {
		OrderRepository orderRepo = new MockOrderRepository();
		UpdateOrderViewModel viewModel = new UpdateOrderViewModel();
		UpdateOrderOutputBoundary outputBoundary = new UpdateOrderPresenter(viewModel);
		
		UpdateOrderUseCaseControl control = new UpdateOrderUseCaseControl(outputBoundary, orderRepo);
		control.execute(null);
		
		assertFalse(viewModel.success);
		assertTrue(viewModel.hasError);
	}

	private static class MockOrderRepository implements OrderRepository {
		@Override
		public Optional<DonHang> findById(Long id) {
			if (id == 1L) {
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
		public boolean existsById(Long orderId) {
			return orderId == 1L;
		}

		@Override
		public List<DonHang> searchOrders(String keyword) {
			return new ArrayList<>();
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
}
