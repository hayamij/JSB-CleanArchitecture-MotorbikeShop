package com.motorbike.business.usecase.control;

<<<<<<< HEAD
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
=======
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
>>>>>>> 8dcc07fa4d37eb42bd8eead969b5dc0579148b25

import org.junit.jupiter.api.Test;

import com.motorbike.adapters.presenters.GetAllAccessoriesPresenter;
import com.motorbike.adapters.viewmodels.GetAllAccessoriesViewModel;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.output.GetAllAccessoriesOutputBoundary;
<<<<<<< HEAD
import com.motorbike.domain.entities.SanPham;
=======
>>>>>>> 8dcc07fa4d37eb42bd8eead969b5dc0579148b25
import com.motorbike.domain.entities.PhuKienXeMay;

public class GetAllAccessoriesUseCaseControlTest {

<<<<<<< HEAD
	@Test
	public void testExecute_WithAccessories_Success() {
		ProductRepository productRepo = new MockProductRepository();
		GetAllAccessoriesViewModel viewModel = new GetAllAccessoriesViewModel();
		GetAllAccessoriesOutputBoundary outputBoundary = new GetAllAccessoriesPresenter(viewModel);
		
		GetAllAccessoriesUseCaseControl control = new GetAllAccessoriesUseCaseControl(outputBoundary, productRepo);
		control.execute();
		
		assertFalse(viewModel.hasError);
		assertEquals(2, viewModel.accessories.size());
	}

	@Test
	public void testExecute_EmptyList_Success() {
		ProductRepository productRepo = new EmptyProductRepository();
		GetAllAccessoriesViewModel viewModel = new GetAllAccessoriesViewModel();
		GetAllAccessoriesOutputBoundary outputBoundary = new GetAllAccessoriesPresenter(viewModel);
		
		GetAllAccessoriesUseCaseControl control = new GetAllAccessoriesUseCaseControl(outputBoundary, productRepo);
		control.execute();
		
		assertFalse(viewModel.hasError);
		assertEquals(0, viewModel.accessories.size());
	}

	private static class MockProductRepository implements ProductRepository {
		@Override
		public Optional<SanPham> findById(Long id) {
			return Optional.empty();
		}

		@Override
		public SanPham save(SanPham sanPham) {
			return sanPham;
		}

		@Override
		public boolean existsById(Long productId) {
			return false;
		}

		@Override
		public List<SanPham> findAll() {
			return new ArrayList<>();
		}

		@Override
		public void deleteById(Long productId) {
		}

		@Override
		public List<PhuKienXeMay> findAllAccessories() {
			List<PhuKienXeMay> accessories = new ArrayList<>();
			accessories.add(new PhuKienXeMay(
				1L, "Mũ bảo hiểm", "Mũ Fullface", new BigDecimal("500000"), 
				"helmet.jpg", 100, true, LocalDateTime.now(), LocalDateTime.now(),
				"Mũ bảo hiểm", "Royal", "ABS", "L"
			));
			accessories.add(new PhuKienXeMay(
				2L, "Găng tay", "Găng tay da", new BigDecimal("200000"), 
				"glove.jpg", 50, true, LocalDateTime.now(), LocalDateTime.now(),
				"Găng tay", "Alpinestars", "Da", "M"
			));
			return accessories;
		}

		@Override
		public List<PhuKienXeMay> searchAccessories(String keyword) {
			return new ArrayList<>();
		}
	}

	private static class EmptyProductRepository implements ProductRepository {
		@Override
		public Optional<SanPham> findById(Long id) {
			return Optional.empty();
		}

		@Override
		public SanPham save(SanPham sanPham) {
			return sanPham;
		}

		@Override
		public boolean existsById(Long productId) {
			return false;
		}

		@Override
		public List<SanPham> findAll() {
			return new ArrayList<>();
		}

		@Override
		public void deleteById(Long productId) {
		}

		@Override
		public List<PhuKienXeMay> findAllAccessories() {
			return new ArrayList<>();
		}

		@Override
		public List<PhuKienXeMay> searchAccessories(String keyword) {
			return new ArrayList<>();
		}
	}
=======
    @Test
    public void testExecute_ReturnsAllAccessories() {
        ProductRepository productRepo = new MockProductRepository();

        GetAllAccessoriesViewModel viewModel = new GetAllAccessoriesViewModel();
        GetAllAccessoriesOutputBoundary presenter = new GetAllAccessoriesPresenter(viewModel);

        GetAllAccessoriesUseCaseControl control = new GetAllAccessoriesUseCaseControl(presenter, productRepo);
        control.execute(null);

        assertFalse(viewModel.hasError);
        assertEquals(3, viewModel.accessories.size());
    }

    @Test
    public void testExecute_EmptyList() {
        ProductRepository productRepo = new ProductRepository() {
            @Override
            public java.util.Optional<com.motorbike.domain.entities.SanPham> findById(Long id) { return java.util.Optional.empty(); }
            @Override
            public com.motorbike.domain.entities.SanPham save(com.motorbike.domain.entities.SanPham product) { return product; }
            @Override
            public boolean existsById(Long productId) { return false; }
            @Override
            public List<com.motorbike.domain.entities.SanPham> findAll() { return new ArrayList<>(); }
        };

        GetAllAccessoriesViewModel viewModel = new GetAllAccessoriesViewModel();
        GetAllAccessoriesOutputBoundary presenter = new GetAllAccessoriesPresenter(viewModel);

        GetAllAccessoriesUseCaseControl control = new GetAllAccessoriesUseCaseControl(presenter, productRepo);
        control.execute(null);

        assertFalse(viewModel.hasError);
        assertEquals(0, viewModel.accessories.size());
    }

    @Test
    public void testExecute_RepositoryThrows_SystemError() {
        ProductRepository productRepo = new ProductRepository() {
            @Override
            public java.util.Optional<com.motorbike.domain.entities.SanPham> findById(Long id) { return java.util.Optional.empty(); }
            @Override
            public com.motorbike.domain.entities.SanPham save(com.motorbike.domain.entities.SanPham product) { return product; }
            @Override
            public boolean existsById(Long productId) { return false; }
            @Override
            public List<com.motorbike.domain.entities.SanPham> findAll() { throw new RuntimeException("DB down"); }
        };

        GetAllAccessoriesViewModel viewModel = new GetAllAccessoriesViewModel();
        GetAllAccessoriesOutputBoundary presenter = new GetAllAccessoriesPresenter(viewModel);

        GetAllAccessoriesUseCaseControl control = new GetAllAccessoriesUseCaseControl(presenter, productRepo);
        control.execute(null);

        assertEquals(true, viewModel.hasError);
        org.junit.jupiter.api.Assertions.assertNotNull(viewModel.errorMessage);
    }

    private static class MockProductRepository implements ProductRepository {

        @Override
        public java.util.Optional<com.motorbike.domain.entities.SanPham> findById(Long id) {
            return java.util.Optional.empty();
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
        public List<com.motorbike.domain.entities.SanPham> findAll() {
            List<com.motorbike.domain.entities.SanPham> list = new ArrayList<>();
            PhuKienXeMay p1 = new PhuKienXeMay("Mũ bảo hiểm Royal","Mũ fullface", new BigDecimal("900000"), "/img/helmet.jpg", 50, "Mũ bảo hiểm", "Royal", "Nhựa", "L");
            p1.setMaSanPham(1L);
            PhuKienXeMay p2 = new PhuKienXeMay("Găng tay bảo hộ","Găng tay da", new BigDecimal("250000"), "/img/gloves.jpg", 100, "Găng tay bảo hộ", "SafeHand", "Da", "M");
            p2.setMaSanPham(2L);
            PhuKienXeMay p3 = new PhuKienXeMay("Áo mưa","Áo mưa chống nước", new BigDecimal("120000"), "/img/raincoat.jpg", 200, "Áo mưa", "RainPro", "Vải", "XL");
            p3.setMaSanPham(3L);
            list.add(p1);
            list.add(p2);
            list.add(p3);
            return list;
        }
    }
>>>>>>> 8dcc07fa4d37eb42bd8eead969b5dc0579148b25
}
