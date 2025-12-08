package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.motorbike.GetAllMotorbikesOutputData;
import com.motorbike.business.dto.motorbike.GetAllMotorbikesOutputData.MotorbikeItem;
import com.motorbike.business.ports.repository.ProductRepository;
import com.motorbike.business.usecase.output.GetAllMotorbikesOutputBoundary;
import com.motorbike.domain.entities.PhuKien;
import com.motorbike.domain.entities.SanPham;
import com.motorbike.domain.entities.XeMay;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GetAllMotorbikesUseCaseControlTest {

    private static class MockPresenter implements GetAllMotorbikesOutputBoundary {
        public GetAllMotorbikesOutputData receivedData;

        @Override
        public void present(GetAllMotorbikesOutputData outputData) {
            this.receivedData = outputData;
        }
    }

    private static class MockProductRepository implements ProductRepository {

        private final List<SanPham> products;
        private final boolean throwException;

        public MockProductRepository(List<SanPham> products) {
            this.products = products;
            this.throwException = false;
        }

        public MockProductRepository(boolean throwException) {
            this.products = new ArrayList<>();
            this.throwException = throwException;
        }

        @Override
        public List<SanPham> findAll() {
            if (throwException) {
                throw new RuntimeException("Database error");
            }
            return products;
        }

        @Override
        public boolean existsById(Long productId) { return false; }

        @Override
        public java.util.Optional<SanPham> findById(Long id) { return java.util.Optional.empty(); }

        @Override
        public SanPham save(SanPham product) { return null; }
    }

    @Test
    void testGetAllMotorbikes_Success() {

        List<SanPham> mockProducts = new ArrayList<>();

        XeMay xe1 = new XeMay(
                "Honda Wave", "Xe tiết kiệm",
                new BigDecimal("20000000"), "wave.jpg",
                10, "Honda", "Wave Alpha", "Đỏ", 2024, 110
        );
        xe1.setMaSanPham(1L);

        XeMay xe2 = new XeMay(
                "Yamaha Exciter", "Xe thể thao",
                new BigDecimal("45000000"), "exciter.jpg",
                5, "Yamaha", "Exciter 155", "Xanh", 2025, 155
        );
        xe2.setMaSanPham(2L);

        mockProducts.add(xe1);
        mockProducts.add(xe2);

        MockPresenter presenter = new MockPresenter();

        GetAllMotorbikesUseCaseControl control =
                new GetAllMotorbikesUseCaseControl(presenter, new MockProductRepository(mockProducts));

        control.execute(null);

        assertNotNull(presenter.receivedData);
        assertEquals(2, presenter.receivedData.getMotorbikes().size());

        MotorbikeItem item1 = presenter.receivedData.getMotorbikes().get(0);
        assertEquals("Honda Wave", item1.getName());
    }

    @Test
    void testGetAllMotorbikes_IgnoreNonMotorbikeProducts() {

        List<SanPham> mockProducts = new ArrayList<>();

        XeMay xe1 = new XeMay("Honda Wave", "Test",
                new BigDecimal("20000000"), "img",
                10, "Honda", "Wave", "Đỏ", 2024, 110);
        xe1.setMaSanPham(1L);

        PhuKien pk = new PhuKien("Mũ bảo hiểm", "PK",
                new BigDecimal("300000"), "helmet.jpg", 10, "Phụ kiện");
        pk.setMaSanPham(3L);

        mockProducts.add(xe1);
        mockProducts.add(pk);

        MockPresenter presenter = new MockPresenter();

        GetAllMotorbikesUseCaseControl control =
                new GetAllMotorbikesUseCaseControl(presenter, new MockProductRepository(mockProducts));

        control.execute(null);

        assertEquals(1, presenter.receivedData.getMotorbikes().size());
        assertEquals("Honda Wave", presenter.receivedData.getMotorbikes().get(0).getName());
    }

    @Test
    void testGetAllMotorbikes_EmptyList() {

        MockPresenter presenter = new MockPresenter();

        GetAllMotorbikesUseCaseControl control =
                new GetAllMotorbikesUseCaseControl(presenter, new MockProductRepository(new ArrayList<>()));

        control.execute(null);

        assertNotNull(presenter.receivedData);
        assertEquals(0, presenter.receivedData.getMotorbikes().size());
    }

    @Test
    void testGetAllMotorbikes_RepositoryThrowsException() {

        MockPresenter presenter = new MockPresenter();

        MockProductRepository brokenRepo = new MockProductRepository(true);

        GetAllMotorbikesUseCaseControl control =
                new GetAllMotorbikesUseCaseControl(presenter, brokenRepo);

        control.execute(null);

        assertNotNull(presenter.receivedData);
        assertEquals("SYSTEM_ERROR", presenter.receivedData.getErrorCode());
        assertNotNull(presenter.receivedData.getErrorMessage());
    }
}
