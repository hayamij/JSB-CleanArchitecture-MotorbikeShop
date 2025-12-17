package com.motorbike.business.usecase.control;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.motorbike.adapters.presenters.UpdateMotorbikePresenter;
import com.motorbike.adapters.viewmodels.UpdateMotorbikeViewModel;
import com.motorbike.business.dto.motorbike.UpdateMotorbikeInputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.output.UpdateMotorbikeOutputBoundary;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.entities.XeMay;
import com.motorbike.domain.entities.PhuKienXeMay;

public class UpdateMotorbikeUseCaseControlTest {

	@Test
	public void testExecute_ValidUpdate_Success() {
		UpdateMotorbikeInputData inputData = new UpdateMotorbikeInputData(
			1L,
			"Honda Wave Alpha Updated",
			"Xe số tiết kiệm nhiên liệu - Phiên bản mới",
			new BigDecimal("32000000"),
			"honda-wave-new.jpg",
			100,
			"Honda",
			"Wave Alpha",
			"Xanh",
			2024,
			110
		);
		
		ProductRepository productRepo = new MockProductRepository();
		UpdateMotorbikeViewModel viewModel = new UpdateMotorbikeViewModel();
		UpdateMotorbikeOutputBoundary outputBoundary = new UpdateMotorbikePresenter(viewModel);
		
		UpdateMotorbikeUseCaseControl control = new UpdateMotorbikeUseCaseControl(outputBoundary, productRepo);
		control.execute(inputData);
		
		assertTrue(viewModel.success);
		assertFalse(viewModel.hasError);
		assertEquals(1L, viewModel.maSanPham);
	}

	@Test
	public void testExecute_ProductNotFound() {
		UpdateMotorbikeInputData inputData = new UpdateMotorbikeInputData(
			999L,
			"Honda Wave",
			"Mô tả",
			new BigDecimal("30000000"),
			"image.jpg",
			50,
			"Honda",
			"Wave",
			"Đỏ",
			2024,
			110
		);
		
		ProductRepository productRepo = new MockProductRepository();
		UpdateMotorbikeViewModel viewModel = new UpdateMotorbikeViewModel();
		UpdateMotorbikeOutputBoundary outputBoundary = new UpdateMotorbikePresenter(viewModel);
		
		UpdateMotorbikeUseCaseControl control = new UpdateMotorbikeUseCaseControl(outputBoundary, productRepo);
		control.execute(inputData);
		
		assertFalse(viewModel.success);
		assertTrue(viewModel.hasError);
		assertEquals("PRODUCT_NOT_FOUND", viewModel.errorCode);
	}

	@Test
	public void testExecute_NullInputData() {
		ProductRepository productRepo = new MockProductRepository();
		UpdateMotorbikeViewModel viewModel = new UpdateMotorbikeViewModel();
		UpdateMotorbikeOutputBoundary outputBoundary = new UpdateMotorbikePresenter(viewModel);
		
		UpdateMotorbikeUseCaseControl control = new UpdateMotorbikeUseCaseControl(outputBoundary, productRepo);
		control.execute(null);
		
		assertFalse(viewModel.success);
		assertTrue(viewModel.hasError);
	}

	private static class MockProductRepository implements ProductRepository {
		@Override
		public Optional<SanPham> findById(Long id) {
			if (id == 1L) {
				XeMay xeMay = new XeMay(
					1L,
					"Honda Wave Alpha",
					"Xe số tiết kiệm",
					new BigDecimal("30000000"),
					"honda.jpg",
					50,
					true,
					LocalDateTime.now(),
					LocalDateTime.now(),
					"Honda",
					"Wave Alpha",
					"Đỏ",
					2024,
					110
				);
				return Optional.of(xeMay);
			}
			return Optional.empty();
		}

		@Override
		public SanPham save(SanPham sanPham) {
			return sanPham;
		}

		@Override
		public boolean existsById(Long productId) {
			return productId == 1L;
		}

		@Override
		public java.util.List<SanPham> findAll() {
			return new java.util.ArrayList<>();
		}

		@Override
		public void deleteById(Long productId) {
		}

		@Override
		public java.util.List<PhuKienXeMay> findAllAccessories() {
			return new java.util.ArrayList<>();
		}

		@Override
		public java.util.List<PhuKienXeMay> searchAccessories(String keyword) {
			return new java.util.ArrayList<>();
		}

		@Override
		public java.util.List<com.motorbike.domain.entities.XeMay> findAllMotorbikes() {
			return new java.util.ArrayList<>();
		}

		@Override
		public java.util.List<com.motorbike.domain.entities.XeMay> searchMotorbikes(String keyword) {
			return new java.util.ArrayList<>();
		}
		
		@Override
		public Optional<SanPham> findByTenSanPham(String tenSanPham) {
			return Optional.empty();
		}
		
		@Override
		public Optional<SanPham> findByMaSanPham(String maSanPham) {
			return Optional.empty();
		}
	}
}
