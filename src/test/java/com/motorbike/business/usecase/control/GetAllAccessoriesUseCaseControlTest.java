package com.motorbike.business.usecase.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.motorbike.adapters.presenters.GetAllAccessoriesPresenter;
import com.motorbike.adapters.viewmodels.GetAllAccessoriesViewModel;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.output.GetAllAccessoriesOutputBoundary;
import com.motorbike.domain.entities.PhuKienXeMay;

public class GetAllAccessoriesUseCaseControlTest {

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
}
