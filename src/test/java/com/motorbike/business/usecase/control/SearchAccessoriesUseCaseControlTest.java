package com.motorbike.business.usecase.control;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.motorbike.adapters.presenters.SearchAccessoriesPresenter;
import com.motorbike.adapters.viewmodels.SearchAccessoriesViewModel;
import com.motorbike.business.dto.accessory.SearchAccessoriesInputData;
import com.motorbike.business.ports.repository.AccessoryRepository;
import com.motorbike.business.usecase.output.SearchAccessoriesOutputBoundary;
import com.motorbike.domain.entities.PhuKienXeMay;

public class SearchAccessoriesUseCaseControlTest {

	@Test
	public void testExecute_WithKeyword_Success() {
		SearchAccessoriesInputData inputData = new SearchAccessoriesInputData("mũ", null, null, null, null, null);
		
		AccessoryRepository accessoryRepo = new MockAccessoryRepository();
		SearchAccessoriesViewModel viewModel = new SearchAccessoriesViewModel();
		SearchAccessoriesOutputBoundary outputBoundary = new SearchAccessoriesPresenter(viewModel);
		
		SearchAccessoriesUseCaseControl control = new SearchAccessoriesUseCaseControl(outputBoundary, accessoryRepo);
		control.execute(inputData);
		
		assertFalse(viewModel.hasError);
		assertEquals(1, viewModel.accessories.size());
	}

	@Test
	public void testExecute_NoResults_Success() {
		SearchAccessoriesInputData inputData = new SearchAccessoriesInputData("xyz123", null, null, null, null, null);
		
		AccessoryRepository accessoryRepo = new MockAccessoryRepository();
		SearchAccessoriesViewModel viewModel = new SearchAccessoriesViewModel();
		SearchAccessoriesOutputBoundary outputBoundary = new SearchAccessoriesPresenter(viewModel);
		
		SearchAccessoriesUseCaseControl control = new SearchAccessoriesUseCaseControl(outputBoundary, accessoryRepo);
		control.execute(inputData);
		
		assertFalse(viewModel.hasError);
		assertEquals(0, viewModel.accessories.size());
	}

	@Test
	public void testExecute_NullInputData() {
		AccessoryRepository accessoryRepo = new MockAccessoryRepository();
		SearchAccessoriesViewModel viewModel = new SearchAccessoriesViewModel();
		SearchAccessoriesOutputBoundary outputBoundary = new SearchAccessoriesPresenter(viewModel);
		
		SearchAccessoriesUseCaseControl control = new SearchAccessoriesUseCaseControl(outputBoundary, accessoryRepo);
		control.execute(null);
		
		// Use case catches NullPointerException and returns system error
		assertTrue(viewModel.hasError);
		assertNotNull(viewModel.errorMessage);
	}

	private static class MockAccessoryRepository implements AccessoryRepository {
		@Override
		public List<PhuKienXeMay> findAllAccessories() {
			List<PhuKienXeMay> accessories = new ArrayList<>();
			accessories.add(new PhuKienXeMay(
				1L, "Mũ bảo hiểm Fullface", "Mũ cao cấp", new BigDecimal("500000"),
				"helmet.jpg", 100, true, LocalDateTime.now(), LocalDateTime.now(),
				"Mũ bảo hiểm", "Royal", "ABS", "L"
			));
			accessories.add(new PhuKienXeMay(
				2L, "Găng tay", "Găng tay cao cấp", new BigDecimal("200000"),
				"glove.jpg", 50, true, LocalDateTime.now(), LocalDateTime.now(),
				"Găng tay", "Scoyco", "Da", "M"
			));
			return accessories;
		}

		@Override
		public Optional<PhuKienXeMay> findById(Long id) {
			return Optional.empty();
		}

		@Override
		public PhuKienXeMay save(PhuKienXeMay phuKien) {
			return phuKien;
		}

		@Override
		public void deleteById(Long id) {
		}

		@Override
		public boolean existsById(Long id) {
			return false;
		}

		@Override
		public List<PhuKienXeMay> searchAccessories(String keyword) {
			return new ArrayList<>();
		}

		@Override
		public List<PhuKienXeMay> search(String keyword, String loaiPhuKien, String thuongHieu) {
			List<PhuKienXeMay> result = new ArrayList<>();
			if (keyword != null && keyword.toLowerCase().contains("mũ")) {
				result.add(new PhuKienXeMay(
					1L, "Mũ bảo hiểm Fullface", "Mũ cao cấp", new BigDecimal("500000"),
					"helmet.jpg", 100, true, LocalDateTime.now(), LocalDateTime.now(),
					"Mũ bảo hiểm", "Royal", "ABS", "L"
				));
			}
			return result;
		}
	}
}
