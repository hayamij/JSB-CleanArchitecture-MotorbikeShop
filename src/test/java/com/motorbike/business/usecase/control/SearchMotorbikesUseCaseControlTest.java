package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.motorbike.SearchMotorbikesInputData;
import com.motorbike.business.dto.motorbike.SearchMotorbikesOutputData;
import com.motorbike.business.dto.motorbike.SearchMotorbikesOutputData.MotorbikeItem;
import com.motorbike.business.ports.repository.MotorbikeRepository;
import com.motorbike.business.usecase.output.SearchMotorbikesOutputBoundary;
import com.motorbike.domain.entities.XeMay;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SearchMotorbikesUseCaseControlTest {

    // ==========================
    // Mock Presenter
    // ==========================
    private static class MockPresenter implements SearchMotorbikesOutputBoundary {
        public SearchMotorbikesOutputData receivedData;

        @Override
        public void present(SearchMotorbikesOutputData outputData) {
            this.receivedData = outputData;
        }
    }

    // ==========================
    // Mock MotorbikeRepository
    // ==========================
    private static class MockMotorbikeRepository implements MotorbikeRepository {

        private final List<XeMay> data;
        private final boolean throwException;

        public MockMotorbikeRepository(List<XeMay> data) {
            this.data = data;
            this.throwException = false;
        }

        public MockMotorbikeRepository(boolean throwException) {
            this.data = new ArrayList<>();
            this.throwException = throwException;
        }

        @Override
        public List<XeMay> findAllMotorbikes() {
            if (throwException) {
                throw new RuntimeException("DB error");
            }
            return data;
        }

        // Các method còn lại của MotorbikeRepository
        // (định nghĩa vừa đủ cho compiler, usecase không dùng tới)

        @Override
        public Optional<XeMay> findById(Long id) {
            return Optional.empty();
        }

        @Override
        public XeMay save(XeMay xeMay) {
            return xeMay;
        }

        @Override
        public void deleteById(Long id) {
            // no-op
        }

        // ❌ KHÔNG có existsById trong interface → đừng override
        // Nếu bạn lỡ thêm thì xóa hẳn method này:
        // public boolean existsById(Long id) { return false; }
    }

    // Helper: tạo danh sách mẫu
    private List<XeMay> sampleData() {
        List<XeMay> list = new ArrayList<>();

        XeMay xe1 = new XeMay("Honda Wave", "Xe số",
                BigDecimal.valueOf(20000000), "wave.jpg",
                10, "Honda", "Wave Alpha", "Đỏ", 2024, 110);
        xe1.setMaSanPham(1L);

        XeMay xe2 = new XeMay("Exciter 155", "Xe côn tay",
                BigDecimal.valueOf(45000000), "exc.jpg",
                5, "Yamaha", "Exciter", "Xanh", 2025, 155);
        xe2.setMaSanPham(2L);

        XeMay xe3 = new XeMay("Yamaha Sirius", "Xe số phổ thông",
                BigDecimal.valueOf(21000000), "sir.jpg",
                7, "Yamaha", "Sirius", "Đen", 2023, 110);
        xe3.setMaSanPham(3L);

        list.add(xe1);
        list.add(xe2);
        list.add(xe3);

        return list;
    }

    // --------------------------------------------------------------
    // 1) Tất cả filter = null → trả về toàn bộ xe
    // --------------------------------------------------------------
    @Test
    void testSearch_AllNullFilters_ReturnsAll() {
        MockPresenter presenter = new MockPresenter();
        SearchMotorbikesUseCaseControl control =
                new SearchMotorbikesUseCaseControl(presenter, new MockMotorbikeRepository(sampleData()));

        SearchMotorbikesInputData input =
                new SearchMotorbikesInputData(null, null, null, null, null, null);

        control.execute(input);

        assertNotNull(presenter.receivedData);
        assertEquals(3, presenter.receivedData.motorbikes.size());
    }

    // --------------------------------------------------------------
    // 2) Keyword filter
    // --------------------------------------------------------------
    @Test
    void testSearch_ByKeyword() {
        MockPresenter presenter = new MockPresenter();
        SearchMotorbikesUseCaseControl control =
                new SearchMotorbikesUseCaseControl(presenter, new MockMotorbikeRepository(sampleData()));

        SearchMotorbikesInputData input =
                new SearchMotorbikesInputData("exciter", null, null, null, null, null);

        control.execute(input);

        assertEquals(1, presenter.receivedData.motorbikes.size());
        assertTrue(presenter.receivedData.motorbikes.get(0).name.toLowerCase().contains("exciter"));
    }

    // --------------------------------------------------------------
    // 3) Brand + CC Range
    // --------------------------------------------------------------
    @Test
    void testSearch_ByBrandAndCCRange() {
        MockPresenter presenter = new MockPresenter();
        SearchMotorbikesUseCaseControl control =
                new SearchMotorbikesUseCaseControl(presenter, new MockMotorbikeRepository(sampleData()));

        SearchMotorbikesInputData input =
                new SearchMotorbikesInputData(null, "Yamaha", null, null, 150, 160);

        control.execute(input);

        assertEquals(1, presenter.receivedData.motorbikes.size());
        MotorbikeItem result = presenter.receivedData.motorbikes.get(0);

        assertEquals("Yamaha", result.brand);
        assertEquals(155, result.displacement);
    }

    // --------------------------------------------------------------
    // 4) Không tìm thấy xe phù hợp
    // --------------------------------------------------------------
    @Test
    void testSearch_NoResult() {
        MockPresenter presenter = new MockPresenter();
        SearchMotorbikesUseCaseControl control =
                new SearchMotorbikesUseCaseControl(presenter, new MockMotorbikeRepository(sampleData()));

        SearchMotorbikesInputData input =
                new SearchMotorbikesInputData("abc", "Suzuki", null, null, 400, 500);

        control.execute(input);

        assertEquals(0, presenter.receivedData.motorbikes.size());
    }

    // --------------------------------------------------------------
    // 5) Repository ném exception → SYSTEM_ERROR
    // --------------------------------------------------------------
    @Test
    void testSearch_RepositoryException() {
        MockPresenter presenter = new MockPresenter();
        SearchMotorbikesUseCaseControl control =
                new SearchMotorbikesUseCaseControl(presenter, new MockMotorbikeRepository(true));

        SearchMotorbikesInputData input =
                new SearchMotorbikesInputData(null, null, null, null, null, null);

        control.execute(input);

        assertEquals("SYSTEM_ERROR", presenter.receivedData.errorCode);
        assertNotNull(presenter.receivedData.errorMessage);
    }
}
