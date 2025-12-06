package com.motorbike.business.usecase.control;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.motorbike.adapters.presenters.ToggleProductVisibilityPresenter;
import com.motorbike.adapters.viewmodels.ToggleProductVisibilityViewModel;
import com.motorbike.business.dto.product.ToggleProductVisibilityInputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.output.ToggleProductVisibilityOutputBoundary;
import com.motorbike.domain.entities.PhuKienXeMay;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.entities.XeMay;

public class ToggleProductVisibilityUseCaseControlTest {

	@Test
	public void testExecute_HideMotorbike_Success() {
		ProductRepository productRepo = new MockProductRepositoryWithMotorbike();
		ToggleProductVisibilityViewModel viewModel = new ToggleProductVisibilityViewModel();
		ToggleProductVisibilityOutputBoundary outputBoundary = new ToggleProductVisibilityPresenter(viewModel);
		
		ToggleProductVisibilityUseCaseControl control = new ToggleProductVisibilityUseCaseControl(outputBoundary, productRepo);
		
		ToggleProductVisibilityInputData inputData = new ToggleProductVisibilityInputData(1L);
		control.execute(inputData);
		
		assertTrue(viewModel.success);
		assertNotNull(viewModel.message);
	}

	@Test
	public void testExecute_ShowMotorbike_Success() {
		ProductRepository productRepo = new MockProductRepositoryWithHiddenMotorbike();
		ToggleProductVisibilityViewModel viewModel = new ToggleProductVisibilityViewModel();
		ToggleProductVisibilityOutputBoundary outputBoundary = new ToggleProductVisibilityPresenter(viewModel);
		
		ToggleProductVisibilityUseCaseControl control = new ToggleProductVisibilityUseCaseControl(outputBoundary, productRepo);
		
		ToggleProductVisibilityInputData inputData = new ToggleProductVisibilityInputData(2L);
		control.execute(inputData);
		
		assertTrue(viewModel.success);
		assertNotNull(viewModel.message);
	}

	@Test
	public void testExecute_HideAccessory_Success() {
		ProductRepository productRepo = new MockProductRepositoryWithAccessory();
		ToggleProductVisibilityViewModel viewModel = new ToggleProductVisibilityViewModel();
		ToggleProductVisibilityOutputBoundary outputBoundary = new ToggleProductVisibilityPresenter(viewModel);
		
		ToggleProductVisibilityUseCaseControl control = new ToggleProductVisibilityUseCaseControl(outputBoundary, productRepo);
		
		ToggleProductVisibilityInputData inputData = new ToggleProductVisibilityInputData(3L);
		control.execute(inputData);
		
		assertTrue(viewModel.success);
	}

	@Test
	public void testExecute_ShowAccessory_Success() {
		ProductRepository productRepo = new MockProductRepositoryWithHiddenAccessory();
		ToggleProductVisibilityViewModel viewModel = new ToggleProductVisibilityViewModel();
		ToggleProductVisibilityOutputBoundary outputBoundary = new ToggleProductVisibilityPresenter(viewModel);
		
		ToggleProductVisibilityUseCaseControl control = new ToggleProductVisibilityUseCaseControl(outputBoundary, productRepo);
		
		ToggleProductVisibilityInputData inputData = new ToggleProductVisibilityInputData(4L);
		control.execute(inputData);
		
		assertTrue(viewModel.success);
	}

	@Test
	public void testExecute_ProductNotFound_Error() {
		ProductRepository productRepo = new MockProductRepositoryEmpty();
		ToggleProductVisibilityViewModel viewModel = new ToggleProductVisibilityViewModel();
		ToggleProductVisibilityOutputBoundary outputBoundary = new ToggleProductVisibilityPresenter(viewModel);
		
		ToggleProductVisibilityUseCaseControl control = new ToggleProductVisibilityUseCaseControl(outputBoundary, productRepo);
		
		ToggleProductVisibilityInputData inputData = new ToggleProductVisibilityInputData(999L);
		control.execute(inputData);
		
		assertFalse(viewModel.success);
		assertNotNull(viewModel.errorCode);
	}

	@Test
	public void testExecute_NullProductId_ValidationError() {
		ProductRepository productRepo = new MockProductRepositoryWithMotorbike();
		ToggleProductVisibilityViewModel viewModel = new ToggleProductVisibilityViewModel();
		ToggleProductVisibilityOutputBoundary outputBoundary = new ToggleProductVisibilityPresenter(viewModel);
		
		ToggleProductVisibilityUseCaseControl control = new ToggleProductVisibilityUseCaseControl(outputBoundary, productRepo);
		
		ToggleProductVisibilityInputData inputData = new ToggleProductVisibilityInputData(null);
		control.execute(inputData);
		
		assertFalse(viewModel.success);
		assertEquals("INVALID_INPUT", viewModel.errorCode);
	}

	@Test
	public void testExecute_NullInputData_ValidationError() {
		ProductRepository productRepo = new MockProductRepositoryWithMotorbike();
		ToggleProductVisibilityViewModel viewModel = new ToggleProductVisibilityViewModel();
		ToggleProductVisibilityOutputBoundary outputBoundary = new ToggleProductVisibilityPresenter(viewModel);
		
		ToggleProductVisibilityUseCaseControl control = new ToggleProductVisibilityUseCaseControl(outputBoundary, productRepo);
		
		control.execute(null);
		
		assertFalse(viewModel.success);
		assertEquals("INVALID_INPUT", viewModel.errorCode);
	}

	// Mock repositories
	private static class MockProductRepositoryWithMotorbike implements ProductRepository {
		@Override
		public Optional<SanPham> findById(Long id) {
			if (id.equals(1L)) {
				return Optional.of(new XeMay("Honda Wave", "Xe số tiết kiệm", 
					new BigDecimal("20000000"), "wave.jpg", 10, 
					"Honda", "Wave", "Đỏ", 2024, 110));
			}
			return Optional.empty();
		}

		@Override
		public SanPham save(SanPham sanPham) {
			return sanPham;
		}

		@Override
		public void deleteById(Long id) {
		}

		@Override
		public List<SanPham> findAll() {
			return List.of();
		}

		@Override
		public boolean existsById(Long id) {
			return id.equals(1L);
		}

		@Override
		public List<XeMay> findAllMotorbikes() {
			return List.of();
		}

		@Override
		public List<PhuKienXeMay> findAllAccessories() {
			return List.of();
		}

		@Override
		public List<XeMay> searchMotorbikes(String keyword) {
			return List.of();
		}

		@Override
		public List<PhuKienXeMay> searchAccessories(String keyword) {
			return List.of();
		}
	}

	private static class MockProductRepositoryWithHiddenMotorbike implements ProductRepository {
		@Override
		public Optional<SanPham> findById(Long id) {
			if (id.equals(2L)) {
				XeMay motorbike = new XeMay("Yamaha Exciter", "Xe thể thao", 
					new BigDecimal("50000000"), "exciter.jpg", 5, 
					"Yamaha", "Exciter", "Xanh", 2024, 155);
				motorbike.ngungKinhDoanh();
				return Optional.of(motorbike);
			}
			return Optional.empty();
		}

		@Override
		public SanPham save(SanPham sanPham) {
			return sanPham;
		}

		@Override
		public void deleteById(Long id) {
		}

		@Override
		public List<SanPham> findAll() {
			return List.of();
		}

		@Override
		public boolean existsById(Long id) {
			return id.equals(2L);
		}

		@Override
		public List<XeMay> findAllMotorbikes() {
			return List.of();
		}

		@Override
		public List<PhuKienXeMay> findAllAccessories() {
			return List.of();
		}

		@Override
		public List<XeMay> searchMotorbikes(String keyword) {
			return List.of();
		}

		@Override
		public List<PhuKienXeMay> searchAccessories(String keyword) {
			return List.of();
		}
	}

	private static class MockProductRepositoryWithAccessory implements ProductRepository {
		@Override
		public Optional<SanPham> findById(Long id) {
			if (id.equals(3L)) {
				return Optional.of(new PhuKienXeMay("Mũ bảo hiểm", "Mũ fullface", 
					new BigDecimal("500000"), "helmet.jpg", 20, "Nón", "Royal", "Plastic", "L"));
			}
			return Optional.empty();
		}

		@Override
		public SanPham save(SanPham sanPham) {
			return sanPham;
		}

		@Override
		public void deleteById(Long id) {
		}

		@Override
		public List<SanPham> findAll() {
			return List.of();
		}

		@Override
		public boolean existsById(Long id) {
			return id.equals(3L);
		}

		@Override
		public List<XeMay> findAllMotorbikes() {
			return List.of();
		}

		@Override
		public List<PhuKienXeMay> findAllAccessories() {
			return List.of();
		}

		@Override
		public List<XeMay> searchMotorbikes(String keyword) {
			return List.of();
		}

		@Override
		public List<PhuKienXeMay> searchAccessories(String keyword) {
			return List.of();
		}
	}

	private static class MockProductRepositoryWithHiddenAccessory implements ProductRepository {
		@Override
		public Optional<SanPham> findById(Long id) {
			if (id.equals(4L)) {
				PhuKienXeMay accessory = new PhuKienXeMay("Găng tay", "Găng tay da", 
					new BigDecimal("200000"), "gloves.jpg", 15, "Găng", "Dainese", "Da", "M");
				accessory.ngungKinhDoanh();
				return Optional.of(accessory);
			}
			return Optional.empty();
		}

		@Override
		public SanPham save(SanPham sanPham) {
			return sanPham;
		}

		@Override
		public void deleteById(Long id) {
		}

		@Override
		public List<SanPham> findAll() {
			return List.of();
		}

		@Override
		public boolean existsById(Long id) {
			return id.equals(4L);
		}

		@Override
		public List<XeMay> findAllMotorbikes() {
			return List.of();
		}

		@Override
		public List<PhuKienXeMay> findAllAccessories() {
			return List.of();
		}

		@Override
		public List<XeMay> searchMotorbikes(String keyword) {
			return List.of();
		}

		@Override
		public List<PhuKienXeMay> searchAccessories(String keyword) {
			return List.of();
		}
	}

	private static class MockProductRepositoryEmpty implements ProductRepository {
		@Override
		public Optional<SanPham> findById(Long id) {
			return Optional.empty();
		}

		@Override
		public SanPham save(SanPham sanPham) {
			return sanPham;
		}

		@Override
		public void deleteById(Long id) {
		}

		@Override
		public List<SanPham> findAll() {
			return List.of();
		}

		@Override
		public boolean existsById(Long id) {
			return false;
		}

		@Override
		public List<XeMay> findAllMotorbikes() {
			return List.of();
		}

		@Override
		public List<PhuKienXeMay> findAllAccessories() {
			return List.of();
		}

		@Override
		public List<XeMay> searchMotorbikes(String keyword) {
			return List.of();
		}

		@Override
		public List<PhuKienXeMay> searchAccessories(String keyword) {
			return List.of();
		}
	}
}
