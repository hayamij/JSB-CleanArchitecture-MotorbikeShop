package com.motorbike.business.usecase.control;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.motorbike.adapters.presenters.CancelOrderPresenter;
import com.motorbike.adapters.viewmodels.CancelOrderViewModel;
import com.motorbike.business.dto.cancelorder.CancelOrderInputData;
import com.motorbike.business.ports.repository.OrderRepository;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.output.CancelOrderOutputBoundary;

public class CancelOrderUseCaseControlTest {

	@Test
	@DisplayName("Hủy đơn thành công")
	public void testExecute_ValidCancelOrder_Success() {
		CancelOrderInputData inputData = new CancelOrderInputData(
			1L,
			2L,
			"Tôi muốn hủy đơn hàng"
		);
		
		OrderRepository orderRepo = new MockOrderRepository();
		ProductRepository productRepo = new MockProductRepository();
		
		CancelOrderViewModel viewModel = new CancelOrderViewModel();
		CancelOrderOutputBoundary outputBoundary = new CancelOrderPresenter(viewModel);
		
		CancelOrderUseCaseControl control = new CancelOrderUseCaseControl(
			outputBoundary, orderRepo, productRepo
		);
		control.execute(inputData);
		
		assertEquals(true, viewModel.success);
		assertEquals(false, viewModel.hasError);
	}

	@Test
	@DisplayName("Dữ liệu nhập vào là null")
	public void testExecute_NullInputData() {
		CancelOrderInputData inputData = null;
		
		OrderRepository orderRepo = new MockOrderRepository();
		ProductRepository productRepo = new MockProductRepository();
		
		CancelOrderViewModel viewModel = new CancelOrderViewModel();
		CancelOrderOutputBoundary outputBoundary = new CancelOrderPresenter(viewModel);
		
		CancelOrderUseCaseControl control = new CancelOrderUseCaseControl(
			outputBoundary, orderRepo, productRepo
		);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
	}
	
	
	@Test
	@DisplayName("Đơn hàng không tìm thấy")
	public void testExecute_OrderNotFound() {
		CancelOrderInputData inputData = new CancelOrderInputData(
			999L,
			2L,
			"Hủy đơn"
		);
		
		OrderRepository orderRepo = new MockOrderRepository();
		ProductRepository productRepo = new MockProductRepository();
		
		CancelOrderViewModel viewModel = new CancelOrderViewModel();
		CancelOrderOutputBoundary outputBoundary = new CancelOrderPresenter(viewModel);
		
		CancelOrderUseCaseControl control = new CancelOrderUseCaseControl(
			outputBoundary, orderRepo, productRepo
		);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
	}
	
	@Test
	@DisplayName("Người dùng không có quyền")
	public void testExecute_UserUnauthorized() {
		CancelOrderInputData inputData = new CancelOrderInputData(
			1L,
			999L,
			"Hủy đơn"
		);
		
		OrderRepository orderRepo = new MockOrderRepository();
		ProductRepository productRepo = new MockProductRepository();
		
		CancelOrderViewModel viewModel = new CancelOrderViewModel();
		CancelOrderOutputBoundary outputBoundary = new CancelOrderPresenter(viewModel);
		
		CancelOrderUseCaseControl control = new CancelOrderUseCaseControl(
			outputBoundary, orderRepo, productRepo
		);
		control.execute(inputData);
		
		assertEquals(false, viewModel.success);
		assertEquals(true, viewModel.hasError);
	}
	
	@Test
	@DisplayName("Trạng thái đơn hàng không hợp lệ")
	public void testExecute_InvalidOrderStatus() {
		CancelOrderInputData inputData = new CancelOrderInputData(
			1L,
			2L,
			"Hủy đơn"
		);
		
		OrderRepository orderRepo = new MockOrderRepository();
		ProductRepository productRepo = new MockProductRepository();
		
		CancelOrderViewModel viewModel = new CancelOrderViewModel();
		CancelOrderOutputBoundary outputBoundary = new CancelOrderPresenter(viewModel);
		
		CancelOrderUseCaseControl control = new CancelOrderUseCaseControl(
			outputBoundary, orderRepo, productRepo
		);
		control.execute(inputData);
		
		assertEquals(true, viewModel.success);
		assertEquals(false, viewModel.hasError);
	}

	@Test
	@DisplayName("Sản phẩm trong đơn không có")
	public void testExecute_ProductNotFound() {
		CancelOrderInputData inputData = new CancelOrderInputData(
			1L,
			2L,
			"Hủy đơn"
		);
		
		OrderRepository orderRepo = new MockOrderRepository();
		ProductRepository productRepo = new MockProductRepositoryNotFound();
		
		CancelOrderViewModel viewModel = new CancelOrderViewModel();
		CancelOrderOutputBoundary outputBoundary = new CancelOrderPresenter(viewModel);
		
		CancelOrderUseCaseControl control = new CancelOrderUseCaseControl(
			outputBoundary, orderRepo, productRepo
		);
		control.execute(inputData);
		
		assertEquals(true, viewModel.success);
		assertEquals(false, viewModel.hasError);
	}

	private static class MockOrderRepository implements OrderRepository {
		@Override
		public Optional<com.motorbike.domain.entities.DonHang> findById(Long id) {
			if (id == null || id == 999L) {
				return Optional.empty();
			}
			
			com.motorbike.domain.entities.DonHang order = new com.motorbike.domain.entities.DonHang(
				id,
				2L,
				null,
				new java.math.BigDecimal("50000000"),
				com.motorbike.domain.entities.TrangThaiDonHang.CHO_XAC_NHAN,
				"Nguyễn Văn A",
				"0912345678",
				"123 Main St",
				null,
				java.time.LocalDateTime.now(),
				java.time.LocalDateTime.now()
			);
			return Optional.of(order);
		}
		
		@Override
		public com.motorbike.domain.entities.DonHang save(com.motorbike.domain.entities.DonHang donHang) {
			return donHang;
		}
		
		@Override
		public java.util.List<com.motorbike.domain.entities.DonHang> findByUserId(Long userId) {
			return new java.util.ArrayList<>();
		}
		
		@Override
		public java.util.List<com.motorbike.domain.entities.DonHang> findByStatus(com.motorbike.domain.entities.TrangThaiDonHang trangThai) {
			return new java.util.ArrayList<>();
		}
		
		@Override
		public java.util.List<com.motorbike.domain.entities.DonHang> findByUserIdAndStatus(Long userId, com.motorbike.domain.entities.TrangThaiDonHang trangThai) {
			return new java.util.ArrayList<>();
		}
		
		@Override
		public java.util.List<com.motorbike.domain.entities.DonHang> findAll() {
			return new java.util.ArrayList<>();
		}

		@Override
		public java.util.List<com.motorbike.domain.entities.DonHang> searchForAdmin(String keyword) {
			return new java.util.ArrayList<>();
		}
		
		@Override
		public void deleteById(Long orderId) {
		}
		
		@Override
		public boolean existsById(Long orderId) {
			return false;
		}
	}
	
	private static class MockProductRepository implements ProductRepository {
		@Override
		public Optional<com.motorbike.domain.entities.SanPham> findById(Long id) {
			if (id == null) {
				return Optional.empty();
			}
			com.motorbike.domain.entities.XeMay product = new com.motorbike.domain.entities.XeMay(
				"Honda Wave",
				"Xe số tiết kiệm nhiên liệu",
				new java.math.BigDecimal("30000000"),
				"honda-wave.jpg",
				100,
				"Honda",
				"Wave Alpha",
				"Đỏ",
				2024,
				110
			);
			product.setMaSanPham(id);
			return Optional.of(product);
		}
		
		@Override
		public com.motorbike.domain.entities.SanPham save(com.motorbike.domain.entities.SanPham product) {
			return product;
		}
		
		@Override
		public boolean existsById(Long productId) {
			return productId != null;
		}
		
		@Override
		public java.util.List<com.motorbike.domain.entities.SanPham> findAll() {
			return new java.util.ArrayList<>();
		}
	}

	private static class MockProductRepositoryNotFound implements ProductRepository {
		@Override
		public Optional<com.motorbike.domain.entities.SanPham> findById(Long id) {
			return Optional.empty();
		}
		
		@Override
		public com.motorbike.domain.entities.SanPham save(com.motorbike.domain.entities.SanPham product) {
			return product;
		}
		
		@Override
		public boolean existsById(Long productId) {
			return false;
		}
		
		@Override
		public java.util.List<com.motorbike.domain.entities.SanPham> findAll() {
			return new java.util.ArrayList<>();
		}
	}
}
