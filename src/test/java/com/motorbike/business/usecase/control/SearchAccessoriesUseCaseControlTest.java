package com.motorbike.business.usecase.control;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.motorbike.adapters.presenters.SearchAccessoriesPresenter;
import com.motorbike.adapters.viewmodels.SearchAccessoriesViewModel;
import com.motorbike.business.dto.accessory.SearchAccessoriesInputData;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.output.SearchAccessoriesOutputBoundary;
import com.motorbike.domain.entities.PhuKienXeMay;

public class SearchAccessoriesUseCaseControlTest {

    @Test
    public void testSearchByKeyword() {
        ProductRepository repo = new MockProductRepo();
        SearchAccessoriesViewModel vm = new SearchAccessoriesViewModel();
        SearchAccessoriesOutputBoundary presenter = new SearchAccessoriesPresenter(vm);

        SearchAccessoriesUseCaseControl control = new SearchAccessoriesUseCaseControl(presenter, repo);
        SearchAccessoriesInputData input = new SearchAccessoriesInputData("mũ", null, null, null, null, null);
        control.execute(input);

        assertFalse(vm.hasError);
        assertEquals(1, vm.accessories.size());
    }

    @Test
    public void testSearchByTypeAndBrand() {
        ProductRepository repo = new MockProductRepo();
        SearchAccessoriesViewModel vm = new SearchAccessoriesViewModel();
        SearchAccessoriesOutputBoundary presenter = new SearchAccessoriesPresenter(vm);

        SearchAccessoriesUseCaseControl control = new SearchAccessoriesUseCaseControl(presenter, repo);
        SearchAccessoriesInputData input = new SearchAccessoriesInputData(null, "Găng tay bảo hộ", "SafeHand", null, null, null);
        control.execute(input);

        assertFalse(vm.hasError);
        assertEquals(1, vm.accessories.size());
    }

    @Test
    public void testSearchByPriceRange() {
        ProductRepository repo = new MockProductRepo();
        SearchAccessoriesViewModel vm = new SearchAccessoriesViewModel();
        SearchAccessoriesOutputBoundary presenter = new SearchAccessoriesPresenter(vm);

        SearchAccessoriesUseCaseControl control = new SearchAccessoriesUseCaseControl(presenter, repo);
        SearchAccessoriesInputData input = new SearchAccessoriesInputData(null, null, null, null, 100000.0, 300000.0);
        control.execute(input);

        assertFalse(vm.hasError);
        assertEquals(2, vm.accessories.size());
    }

    @Test
    public void testSearch_NullInput_ReturnsAll() {
        ProductRepository repo = new MockProductRepo();
        SearchAccessoriesViewModel vm = new SearchAccessoriesViewModel();
        SearchAccessoriesOutputBoundary presenter = new SearchAccessoriesPresenter(vm);

        SearchAccessoriesUseCaseControl control = new SearchAccessoriesUseCaseControl(presenter, repo);
        control.execute(null);

        assertFalse(vm.hasError);
        assertEquals(3, vm.accessories.size());
    }

    @Test
    public void testSearch_CaseInsensitive() {
        ProductRepository repo = new MockProductRepo();
        SearchAccessoriesViewModel vm = new SearchAccessoriesViewModel();
        SearchAccessoriesOutputBoundary presenter = new SearchAccessoriesPresenter(vm);

        SearchAccessoriesUseCaseControl control = new SearchAccessoriesUseCaseControl(presenter, repo);
        SearchAccessoriesInputData input = new SearchAccessoriesInputData("MŨ", null, null, null, null, null);
        control.execute(input);

        assertFalse(vm.hasError);
        assertEquals(1, vm.accessories.size());
    }

    @Test
    public void testSearch_NoMatches() {
        ProductRepository repo = new MockProductRepo();
        SearchAccessoriesViewModel vm = new SearchAccessoriesViewModel();
        SearchAccessoriesOutputBoundary presenter = new SearchAccessoriesPresenter(vm);

        SearchAccessoriesUseCaseControl control = new SearchAccessoriesUseCaseControl(presenter, repo);
        SearchAccessoriesInputData input = new SearchAccessoriesInputData("khongtontai", null, null, null, null, null);
        control.execute(input);

        assertFalse(vm.hasError);
        assertEquals(0, vm.accessories.size());
    }

    @Test
    public void testSearch_MinGreaterThanMax() {
        ProductRepository repo = new MockProductRepo();
        SearchAccessoriesViewModel vm = new SearchAccessoriesViewModel();
        SearchAccessoriesOutputBoundary presenter = new SearchAccessoriesPresenter(vm);

        SearchAccessoriesUseCaseControl control = new SearchAccessoriesUseCaseControl(presenter, repo);
        SearchAccessoriesInputData input = new SearchAccessoriesInputData(null, null, null, null, 500000.0, 100000.0);
        control.execute(input);

        assertFalse(vm.hasError);
        assertEquals(0, vm.accessories.size());
    }

    @Test
    public void testSearch_ByMaterial() {
        ProductRepository repo = new MockProductRepo();
        SearchAccessoriesViewModel vm = new SearchAccessoriesViewModel();
        SearchAccessoriesOutputBoundary presenter = new SearchAccessoriesPresenter(vm);

        SearchAccessoriesUseCaseControl control = new SearchAccessoriesUseCaseControl(presenter, repo);
        SearchAccessoriesInputData input = new SearchAccessoriesInputData(null, null, null, "Da", null, null);
        control.execute(input);

        assertFalse(vm.hasError);
        assertEquals(1, vm.accessories.size());
    }

    private static class MockProductRepo implements ProductRepository {

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
            PhuKienXeMay p2 = new PhuKienXeMay("Găng tay bảo hộ","Găng tay da", new BigDecimal("250000"), "/img/gloves.jpg", 100, "Găng tay bảo hộ", "SafeHand", "Da", "M");
            PhuKienXeMay p3 = new PhuKienXeMay("Áo mưa","Áo mưa chống nước", new BigDecimal("120000"), "/img/raincoat.jpg", 200, "Áo mưa", "RainPro", "Vải", "XL");
            list.add(p1);
            list.add(p2);
            list.add(p3);
            return list;
        }
    }
}
