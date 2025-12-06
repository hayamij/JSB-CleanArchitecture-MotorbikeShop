package com.motorbike.business.usecase.control;

import com.motorbike.business.dto.motorbike.UpdateMotorbikeInputData;
import com.motorbike.business.dto.motorbike.UpdateMotorbikeOutputData;
import com.motorbike.business.dto.motorbike.UpdateMotorbikeOutputData.MotorbikeItem;
import com.motorbike.business.ports.repository.MotorbikeRepository;
import com.motorbike.business.usecase.output.UpdateMotorbikeOutputBoundary;
import com.motorbike.domain.entities.XeMay;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UpdateMotorbikeUseCaseControlTest {

    // =======================
    // Mock Presenter
    // =======================
    private static class MockPresenter implements UpdateMotorbikeOutputBoundary {
        public UpdateMotorbikeOutputData received;

        @Override
        public void present(UpdateMotorbikeOutputData outputData) {
            this.received = outputData;
        }
    }

    // =======================
    // Mock Repository
    // =======================
    private static class MockRepo implements MotorbikeRepository {

        public List<XeMay> store = new ArrayList<>();
        public boolean throwException = false;

        @Override
        public Optional<XeMay> findById(Long id) {
            if (throwException) throw new RuntimeException("DB error");

            return store.stream()
                    .filter(x -> x.getMaSanPham().equals(id))
                    .findFirst();
        }

        @Override
        public XeMay save(XeMay xe) {
            if (throwException) throw new RuntimeException("Save failed");

            store.removeIf(x -> x.getMaSanPham().equals(xe.getMaSanPham()));
            this.store.add(xe);
            return xe;
        }

        @Override
        public List<XeMay> findAllMotorbikes() { return store; }

        @Override
        public void deleteById(Long id) {}
    }

    // ----------------------------------------------------
    // 1) Update thành công
    // ----------------------------------------------------
    @Test
    void testUpdate_Success() {
        MockPresenter presenter = new MockPresenter();
        MockRepo repo = new MockRepo();

        XeMay xe = new XeMay(
                "Old Name", "Old Desc", new BigDecimal("1000"),
                "old.jpg", 5, "Honda", "Wave", "Đỏ", 2020, 110
        );
        xe.setMaSanPham(1L);
        repo.store.add(xe);

        UpdateMotorbikeUseCaseControl control =
                new UpdateMotorbikeUseCaseControl(presenter, repo);

        UpdateMotorbikeInputData input = new UpdateMotorbikeInputData(
                1L,
                "New Name",
                null,
                new BigDecimal("2000"),
                null,
                10,
                null,
                null,
                null,
                null,
                null
        );

        control.execute(input);

        assertNotNull(presenter.received);
        assertNull(presenter.received.getErrorCode());

        MotorbikeItem item = presenter.received.getMotorbike();
        assertEquals("New Name", item.name);
        assertEquals(new BigDecimal("2000"), item.price);
        assertEquals(10, item.stock);
    }

    // ----------------------------------------------------
    // 2) Không tìm thấy → NOT_FOUND
    // ----------------------------------------------------
    @Test
    void testUpdate_NotFound() {
        MockPresenter presenter = new MockPresenter();
        MockRepo repo = new MockRepo();

        UpdateMotorbikeUseCaseControl control =
                new UpdateMotorbikeUseCaseControl(presenter, repo);

        UpdateMotorbikeInputData input = new UpdateMotorbikeInputData(
                999L, "X", null, null, null, null,
                null, null, null, null, null
        );

        control.execute(input);

        assertEquals("NOT_FOUND", presenter.received.getErrorCode());
        assertEquals("Motorbike not found", presenter.received.getErrorMessage());
    }

    // ----------------------------------------------------
    // 3) Update chỉ 1 field → không thay đổi field khác
    // ----------------------------------------------------
    @Test
    void testUpdate_OnlyOneField() {
        MockPresenter presenter = new MockPresenter();
        MockRepo repo = new MockRepo();

        XeMay xe = new XeMay(
                "Name", "Desc", new BigDecimal("1000"),
                "img.jpg", 5, "Yamaha", "Sirius", "Xanh", 2021, 110
        );
        xe.setMaSanPham(10L);
        repo.store.add(xe);

        UpdateMotorbikeUseCaseControl control =
                new UpdateMotorbikeUseCaseControl(presenter, repo);

        UpdateMotorbikeInputData input = new UpdateMotorbikeInputData(
                10L,
                null,            // KHÔNG đổi tên
                "New Desc",      // Chỉ đổi description
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        control.execute(input);

        MotorbikeItem item = presenter.received.getMotorbike();
        assertEquals("Name", item.name); // giữ nguyên
        assertEquals("New Desc", item.description); // thay đổi
    }

    // ----------------------------------------------------
    // 4) Repo ném exception → SYSTEM_ERROR
    // ----------------------------------------------------
    @Test
    void testUpdate_RepositoryThrows() {
        MockPresenter presenter = new MockPresenter();
        MockRepo repo = new MockRepo();
        repo.throwException = true;

        UpdateMotorbikeUseCaseControl control =
                new UpdateMotorbikeUseCaseControl(presenter, repo);

        UpdateMotorbikeInputData input = new UpdateMotorbikeInputData(
                1L,
                "Test", null, null, null, null,
                null, null, null, null, null
        );

        control.execute(input);

        assertEquals("SYSTEM_ERROR", presenter.received.getErrorCode());
        assertNotNull(presenter.received.getErrorMessage());
    }
}
