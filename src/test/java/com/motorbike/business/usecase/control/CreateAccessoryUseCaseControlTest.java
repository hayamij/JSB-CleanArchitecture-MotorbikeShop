package com.motorbike.business.usecase.control;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.motorbike.adapters.presenters.CreateAccessoryPresenter;
import com.motorbike.adapters.viewmodels.CreateAccessoryViewModel;
import com.motorbike.business.dto.accessory.CreateAccessoryInputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.output.CreateAccessoryOutputBoundary;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.entities.PhuKienXeMay;

public class CreateAccessoryUseCaseControlTest {

	@Test
	public void testExecute_ValidAccessory_Success() {
		CreateAccessoryInputData inputData = new CreateAccessoryInputData(
			"Mũ bảo hiểm Fullface",
			"Mũ bảo hiểm cao cấp",
			new BigDecimal("500000"),
			"helmet.jpg",
			100,
			"Mũ bảo hiểm",
			"Royal",
			"Nhựa ABS",
			"L"
		);
		
		ProductRepository productRepo = new MockProductRepository();
		CreateAccessoryViewModel viewModel = new CreateAccessoryViewModel();
		CreateAccessoryOutputBoundary outputBoundary = new CreateAccessoryPresenter(viewModel);
		
		CreateAccessoryUseCaseControl control = new CreateAccessoryUseCaseControl(outputBoundary, productRepo);
		control.execute(inputData);
		
		assertTrue(viewModel.success);
		assertFalse(viewModel.hasError);
		assertNotNull(viewModel.maSanPham);
	}

	@Test
	public void testExecute_NullInputData() {
		ProductRepository productRepo = new MockProductRepository();
		CreateAccessoryViewModel viewModel = new CreateAccessoryViewModel();
		CreateAccessoryOutputBoundary outputBoundary = new CreateAccessoryPresenter(viewModel);
		
		CreateAccessoryUseCaseControl control = new CreateAccessoryUseCaseControl(outputBoundary, productRepo);
		control.execute(null);
		
		assertFalse(viewModel.success);
		assertTrue(viewModel.hasError);
	}

	@Test
	public void testExecute_EmptyProductName() {
		CreateAccessoryInputData inputData = new CreateAccessoryInputData(
			"",
			"Mô tả",
			new BigDecimal("500000"),
			"image.jpg",
			100,
			"Mũ",
			"Brand",
			"Plastic",
			"M"
		);
		
		ProductRepository productRepo = new MockProductRepository();
		CreateAccessoryViewModel viewModel = new CreateAccessoryViewModel();
		CreateAccessoryOutputBoundary outputBoundary = new CreateAccessoryPresenter(viewModel);
		
		CreateAccessoryUseCaseControl control = new CreateAccessoryUseCaseControl(outputBoundary, productRepo);
		control.execute(inputData);
		
		assertFalse(viewModel.success);
		assertTrue(viewModel.hasError);
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
	}
}
