package com.motorbike.business.usecase.control;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.motorbike.adapters.presenters.CreateMotorbikePresenter;
import com.motorbike.adapters.viewmodels.CreateMotorbikeViewModel;
import com.motorbike.business.dto.motorbike.CreateMotorbikeInputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.output.CreateMotorbikeOutputBoundary;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.entities.PhuKienXeMay;

public class CreateMotorbikeUseCaseControlTest {

	@Test
	public void testExecute_ValidMotorbike_Success() {
		CreateMotorbikeInputData inputData = new CreateMotorbikeInputData(
			"Honda Wave Alpha",
			"Xe số tiết kiệm nhiên liệu",
			new BigDecimal("30000000"),
			"honda-wave.jpg",
			50,
			"Honda",
			"Wave Alpha",
			"Đỏ",
			2024,
			110
		);
		
		ProductRepository productRepo = new MockProductRepository();
		CreateMotorbikeViewModel viewModel = new CreateMotorbikeViewModel();
		CreateMotorbikeOutputBoundary outputBoundary = new CreateMotorbikePresenter(viewModel);
		
		CreateMotorbikeUseCaseControl control = new CreateMotorbikeUseCaseControl(outputBoundary, productRepo);
		control.execute(inputData);
		
		assertTrue(viewModel.success);
		assertFalse(viewModel.hasError);
		assertNotNull(viewModel.maSanPham);
		assertEquals("Honda Wave Alpha", viewModel.tenSanPham);
	}

	@Test
	public void testExecute_NullInputData() {
		ProductRepository productRepo = new MockProductRepository();
		CreateMotorbikeViewModel viewModel = new CreateMotorbikeViewModel();
		CreateMotorbikeOutputBoundary outputBoundary = new CreateMotorbikePresenter(viewModel);
		
		CreateMotorbikeUseCaseControl control = new CreateMotorbikeUseCaseControl(outputBoundary, productRepo);
		control.execute(null);
		
		assertFalse(viewModel.success);
		assertTrue(viewModel.hasError);
		assertEquals("INVALID_INPUT", viewModel.errorCode);
	}

	@Test
	public void testExecute_EmptyProductName() {
		CreateMotorbikeInputData inputData = new CreateMotorbikeInputData(
			"",
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
		CreateMotorbikeViewModel viewModel = new CreateMotorbikeViewModel();
		CreateMotorbikeOutputBoundary outputBoundary = new CreateMotorbikePresenter(viewModel);
		
		CreateMotorbikeUseCaseControl control = new CreateMotorbikeUseCaseControl(outputBoundary, productRepo);
		control.execute(inputData);
		
		assertFalse(viewModel.success);
		assertTrue(viewModel.hasError);
		assertEquals("EMPTY_NAME", viewModel.errorCode);
	}

	@Test
	public void testExecute_NegativePrice() {
		CreateMotorbikeInputData inputData = new CreateMotorbikeInputData(
			"Honda Wave",
			"Mô tả",
			new BigDecimal("-1000"),
			"image.jpg",
			50,
			"Honda",
			"Wave",
			"Đỏ",
			2024,
			110
		);
		
		ProductRepository productRepo = new MockProductRepository();
		CreateMotorbikeViewModel viewModel = new CreateMotorbikeViewModel();
		CreateMotorbikeOutputBoundary outputBoundary = new CreateMotorbikePresenter(viewModel);
		
		CreateMotorbikeUseCaseControl control = new CreateMotorbikeUseCaseControl(outputBoundary, productRepo);
		control.execute(inputData);
		
		assertFalse(viewModel.success);
		assertTrue(viewModel.hasError);
		assertEquals("INVALID_PRICE", viewModel.errorCode);
	}

	@Test
	public void testExecute_NegativeStock() {
		CreateMotorbikeInputData inputData = new CreateMotorbikeInputData(
			"Honda Wave",
			"Mô tả",
			new BigDecimal("30000000"),
			"image.jpg",
			-5,
			"Honda",
			"Wave",
			"Đỏ",
			2024,
			110
		);
		
		ProductRepository productRepo = new MockProductRepository();
		CreateMotorbikeViewModel viewModel = new CreateMotorbikeViewModel();
		CreateMotorbikeOutputBoundary outputBoundary = new CreateMotorbikePresenter(viewModel);
		
		CreateMotorbikeUseCaseControl control = new CreateMotorbikeUseCaseControl(outputBoundary, productRepo);
		control.execute(inputData);
		
		assertFalse(viewModel.success);
		assertTrue(viewModel.hasError);
		assertEquals("INVALID_STOCK", viewModel.errorCode);
	}

	private static class MockProductRepository implements ProductRepository {
		private Long nextId = 1L;

		@Override
		public Optional<SanPham> findById(Long id) {
			return Optional.empty();
		}

		@Override
		public SanPham save(SanPham sanPham) {
			if (sanPham.getMaSanPham() == null) {
				sanPham.setMaSanPham(nextId++);
			}
			return sanPham;
		}

		@Override
		public boolean existsById(Long productId) {
			return false;
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
	}
}
