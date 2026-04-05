package com.motorbike.business.usecase.control;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.motorbike.adapters.presenters.UpdateAccessoryPresenter;
import com.motorbike.adapters.viewmodels.UpdateAccessoryViewModel;
import com.motorbike.business.dto.accessory.UpdateAccessoryInputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.output.UpdateAccessoryOutputBoundary;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.entities.PhuKienXeMay;

public class UpdateAccessoryUseCaseControlTest {

	@Test
	public void testExecute_ValidUpdate_Success() {
		UpdateAccessoryInputData inputData = new UpdateAccessoryInputData(
			1L,
			"Mũ bảo hiểm Fullface Updated",
			"Mũ bảo hiểm cao cấp - Phiên bản mới",
			new BigDecimal("600000"),
			"helmet-new.jpg",
			150,
			"Mũ bảo hiểm",
			"Royal",
			"Carbon",
			"XL"
		);
		
		ProductRepository productRepo = new MockProductRepository();
		UpdateAccessoryViewModel viewModel = new UpdateAccessoryViewModel();
		UpdateAccessoryOutputBoundary outputBoundary = new UpdateAccessoryPresenter(viewModel);
		
		UpdateAccessoryUseCaseControl control = new UpdateAccessoryUseCaseControl(outputBoundary, productRepo);
		control.execute(inputData);
		
		assertTrue(viewModel.success);
		assertFalse(viewModel.hasError);
	}

	@Test
	public void testExecute_ProductNotFound() {
		UpdateAccessoryInputData inputData = new UpdateAccessoryInputData(
			999L,
			"Mũ",
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
		UpdateAccessoryViewModel viewModel = new UpdateAccessoryViewModel();
		UpdateAccessoryOutputBoundary outputBoundary = new UpdateAccessoryPresenter(viewModel);
		
		UpdateAccessoryUseCaseControl control = new UpdateAccessoryUseCaseControl(outputBoundary, productRepo);
		control.execute(inputData);
		
		assertFalse(viewModel.success);
		assertTrue(viewModel.hasError);
	}

	private static class MockProductRepository implements ProductRepository {
		@Override
		public Optional<SanPham> findById(Long id) {
			if (id == 1L) {
				PhuKienXeMay phuKien = new PhuKienXeMay(
					1L,
					"Mũ bảo hiểm",
					"Mô tả",
					new BigDecimal("500000"),
					"helmet.jpg",
					100,
					true,
					LocalDateTime.now(),
					LocalDateTime.now(),
					"Mũ bảo hiểm",
					"Royal",
					"Nhựa ABS",
					"L"
				);
				return Optional.of(phuKien);
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
