package com.motorbike.business.usecase.control;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.motorbike.adapters.presenters.GetOrderDetailPresenter;
import com.motorbike.adapters.viewmodels.GetOrderDetailViewModel;
import com.motorbike.business.dto.order.GetOrderDetailInputData;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.business.usecase.output.GetOrderDetailOutputBoundary;
import com.motorbike.domain.entities.ChiTietDonHang;
import com.motorbike.domain.entities.DonHang;
import com.motorbike.domain.entities.PhuongThucThanhToan;
import com.motorbike.domain.entities.TrangThaiDonHang;

public class GetOrderDetailUseCaseControlTest {

	@Test
	public void testExecute_OrderExists_Success() {
		OrderRepository orderRepo = new MockOrderRepositoryWithOrder();
		GetOrderDetailViewModel viewModel = new GetOrderDetailViewModel();
		GetOrderDetailOutputBoundary outputBoundary = new GetOrderDetailPresenter(viewModel);
		
		GetOrderDetailUseCaseControl control = new GetOrderDetailUseCaseControl(outputBoundary, orderRepo);
		
		GetOrderDetailInputData inputData = new GetOrderDetailInputData(1L);
		control.execute(inputData);
		
		assertTrue(viewModel.success);
		assertNotNull(viewModel.orderDetail);
		assertEquals(1L, viewModel.orderDetail.orderId);
		assertEquals("Nguyễn Văn A", viewModel.orderDetail.customerName);
		assertEquals(2, viewModel.orderDetail.items.size());
	}

	@Test
	public void testExecute_OrderNotFound_Error() {
		OrderRepository orderRepo = new MockOrderRepositoryEmpty();
		GetOrderDetailViewModel viewModel = new GetOrderDetailViewModel();
		GetOrderDetailOutputBoundary outputBoundary = new GetOrderDetailPresenter(viewModel);
		
		GetOrderDetailUseCaseControl control = new GetOrderDetailUseCaseControl(outputBoundary, orderRepo);
		
		GetOrderDetailInputData inputData = new GetOrderDetailInputData(999L);
		control.execute(inputData);
		
		assertFalse(viewModel.success);
		assertNotNull(viewModel.errorCode);
	}

	@Test
	public void testExecute_NullOrderId_ValidationError() {
		OrderRepository orderRepo = new MockOrderRepositoryWithOrder();
		GetOrderDetailViewModel viewModel = new GetOrderDetailViewModel();
		GetOrderDetailOutputBoundary outputBoundary = new GetOrderDetailPresenter(viewModel);
		
		GetOrderDetailUseCaseControl control = new GetOrderDetailUseCaseControl(outputBoundary, orderRepo);
		
		GetOrderDetailInputData inputData = new GetOrderDetailInputData(null);
		control.execute(inputData);
		
		assertFalse(viewModel.success);
		assertEquals("INVALID_INPUT", viewModel.errorCode);
	}

	// Mock repositories
	private static class MockOrderRepositoryWithOrder implements OrderRepository {
		@Override
		public Optional<DonHang> findById(Long id) {
			if (id.equals(1L)) {
				List<ChiTietDonHang> items = new ArrayList<>();
				items.add(new ChiTietDonHang(1L, "Honda Wave", new BigDecimal("20000000"), 2));
				items.add(new ChiTietDonHang(2L, "Yamaha Exciter", new BigDecimal("50000000"), 1));
				
				DonHang order = new DonHang(
					1L,
					1L,
					items,
					new BigDecimal("70000000"),
					TrangThaiDonHang.CHO_XAC_NHAN,
					"Nguyễn Văn A",
					"0123456789",
					"123 Đường ABC, TP.HCM",
					"Giao giờ hành chính",
					PhuongThucThanhToan.THANH_TOAN_TRUC_TIEP,
					java.time.LocalDateTime.now(),
					java.time.LocalDateTime.now()
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
		public void deleteById(Long id) {
		}

		@Override
		public List<DonHang> findAll() {
			return List.of();
		}

		@Override
		public boolean existsById(Long id) {
			return id.equals(1L);
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
		public List<DonHang> searchOrders(String keyword) {
			return List.of();
		}

		@Override
		public java.util.List<com.motorbike.domain.entities.ProductSalesStats> getTopSellingProducts(int limit) {
			return new java.util.ArrayList<>();
		}
	}

	private static class MockOrderRepositoryEmpty implements OrderRepository {
		@Override
		public Optional<DonHang> findById(Long id) {
			return Optional.empty();
		}

		@Override
		public DonHang save(DonHang donHang) {
			return donHang;
		}

		@Override
		public void deleteById(Long id) {
		}

		@Override
		public List<DonHang> findAll() {
			return List.of();
		}

		@Override
		public boolean existsById(Long id) {
			return false;
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
	public List<DonHang> searchOrders(String keyword) {
		return List.of();
	}

	@Override
	public java.util.List<com.motorbike.domain.entities.ProductSalesStats> getTopSellingProducts(int limit) {
		return new java.util.ArrayList<>();
	}
}
}